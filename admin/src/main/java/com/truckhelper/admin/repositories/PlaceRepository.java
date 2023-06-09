package com.truckhelper.admin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.PlaceId;

public interface PlaceRepository extends JpaRepository<Place, PlaceId> {
    @Query(value = """
            SELECT p FROM Place p
            WHERE (p.name LIKE %:query%) OR (p.address.address1 LIKE %:query%)
            OR (p.address.address2 LIKE %:query%)
            """)
    List<Place> findAll(@Param("query") String query);

    @Query("SELECT DISTINCT p FROM Place p LEFT JOIN FETCH p.images img WHERE p.id = :placeId AND (img IS NULL OR img.deleted = false) order by img.sequence asc")
    Optional<Place> findPlaceWithNonDeletedImages(@Param("placeId") PlaceId placeId);
}
