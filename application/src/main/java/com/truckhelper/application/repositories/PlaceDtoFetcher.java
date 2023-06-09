package com.truckhelper.application.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.truckhelper.application.dtos.GeoPositionDto;
import com.truckhelper.application.dtos.PlaceDescriptionDto;
import com.truckhelper.application.dtos.PlaceDto;
import com.truckhelper.application.dtos.PlaceImageDto;
import com.truckhelper.application.dtos.PlaceSpacesDto;
import com.truckhelper.application.dtos.PlanDto;
import com.truckhelper.core.models.PlaceId;

@Component
public class PlaceDtoFetcher {
    private final JdbcTemplate jdbcTemplate;

    public PlaceDtoFetcher(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PlaceDto> fetchAll(Double latitude, Double longitude,
                                   String planType) {
        String sql = """
                SELECT
                    *,
                    (
                        ll_to_earth(places.latitude, places.longitude)
                        <-> ll_to_earth(?, ?)
                    ) / 1000 AS distance,
                    (
                        SELECT url FROM place_images
                            WHERE place_images.place_id = places.id
                                AND place_images.deleted = false
                            ORDER BY sequence LIMIT 1
                    ) AS imageUrl
                FROM places
                WHERE places.hidden = FALSE
                """;

        if (List.of("monthly", "yearly").contains(planType)) {
            sql += " AND places." + planType + "_price > 0";
        }

        sql += " ORDER BY distance";

        return jdbcTemplate.query(
                sql,
                (ResultSet rs, int rowNum) -> PlaceDto.builder()
                        .id(rs.getString("id"))
                        .distance(rs.getDouble("distance"))
                        .name(rs.getString("name"))
                        .images(List.of(new PlaceImageDto(
                                rs.getString("imageUrl")
                        )))
                        .plans(Stream.of("monthly", "yearly")
                                .map(type -> {
                                    try {
                                        long price = rs.getLong(
                                                type + "_price");
                                        if (price <= 0) {
                                            return null;
                                        }
                                        return new PlanDto(type, price);
                                    } catch (SQLException e) {
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .toList()
                        )
                        .address(rs.getString("address1"))
                        .position(new GeoPositionDto(
                                rs.getDouble("latitude"),
                                rs.getDouble("longitude")
                        ))
                        .description(new PlaceDescriptionDto(
                                rs.getString("introduction"),
                                rs.getString("precautions")
                        ))
                        .spaces(new PlaceSpacesDto(
                                rs.getInt("total_spaces"),
                                rs.getInt("free_spaces")
                        ))
                        .build(),
                latitude, longitude
        );
    }

    public Double fetchDistance(PlaceId placeId, Double latitude, Double longitude) {
        List<Double> result = jdbcTemplate.query(
                """
                        SELECT
                            (
                                ll_to_earth(places.latitude, places.longitude)
                                <-> ll_to_earth(?, ?)
                            ) / 1000 AS distance
                        FROM places
                        WHERE places.id = ?
                        """,
                (ResultSet rs, int rowNum) -> rs.getDouble("distance"),
                latitude, longitude, placeId.toString()
        );
        return result.get(0);
    }
}
