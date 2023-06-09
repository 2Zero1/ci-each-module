package com.truckhelper.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truckhelper.core.models.PlaceImage;
import com.truckhelper.core.models.PlaceImageId;

public interface PlaceImageRepository
        extends JpaRepository<PlaceImage, PlaceImageId> {
}
