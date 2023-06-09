package com.truckhelper.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;

public interface PlaceRepository extends JpaRepository<Place, PlaceId> {
    @Query(value = """
            SELECT *,
                (
                    ll_to_earth(place.latitude, place.longitude)
                    <-> ll_to_earth(:latitude, :longitude)
                ) / 1000 AS distance
            FROM Place place
            ORDER BY distance
            """, nativeQuery = true)
    List<Place> findAll(@Param("latitude") Double latitude,
                        @Param("longitude") Double longitude);
}
