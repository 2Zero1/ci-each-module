package com.truckhelper.admin.repositories;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.truckhelper.admin.dtos.AddressDto;
import com.truckhelper.admin.dtos.PersonDto;
import com.truckhelper.admin.dtos.PlaceDto;
import com.truckhelper.admin.dtos.RentalDto;
import com.truckhelper.admin.dtos.UserDto;
import com.truckhelper.core.models.RentalStatus;

@Component
public class RentalDtoFetcher {
    private final JdbcTemplate jdbcTemplate;

    public RentalDtoFetcher(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RentalDto> fetchAll(RentalStatus status) {
        String sql = """
                SELECT
                    *, places.name as place_name, users.name as user_name
                FROM rentals join places on rentals.place_id = places.id join users on rentals.user_id = users.id where rentals.status =
                """;

        sql += "'"+status+"'";

        return jdbcTemplate.query(
                sql,
                (ResultSet rs, int rowNum) -> {
                    PersonDto personDto = PersonDto.builder()
                            .name(rs.getString("user_name"))
                            .phoneNumber(rs.getString("phone_number"))
                            .build();

                    UserDto userDto = new UserDto(
                            rs.getString("user_id"),
                            null,personDto);

                    return RentalDto.builder()
                            .id(rs.getString("id"))
                            .status(rs.getString("status"))
                            .user(userDto)
                            .place(
                                    PlaceDto.builder()
                                            .name(rs.getString("place_name"))
                                            .address(new AddressDto(rs.getString("address1"), rs.getString("address2"))
                                            ).build()
                            )
                            .plan(rs.getString("plan"))
                            .beginningDate(rs.getString("beginning_date"))
                            .endDate(rs.getString("end_date"))
                            .price(rs.getLong("price"))
                            .method(rs.getString("method"))
                            .receiptId(rs.getString("receipt_id"))
                            .paidAt(rs.getString("paid_at"))
                            .createdAt(rs.getString("created_at"))
                            .build();
                }
        );
    }
}
