package com.truckhelper.core.models;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Builder;

@Entity
@Table(name = "place_images")
public class PlaceImage {
    @Id
    private PlaceImageId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "place_id"))
    private PlaceId placeId;

    private String url;

    @Column(name = "sequence", nullable = false)
    @ColumnDefault("0")
    private int sequence = 0;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private boolean deleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private PlaceImage() {
    }

    public PlaceImage(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }

    @Builder
    public PlaceImage(
            PlaceImageId id, PlaceId placeId, String url, int sequence, boolean deleted
    ) {
        this.id = id;
        this.placeId = placeId;
        this.url = url;
        this.sequence = sequence;
        this.deleted = deleted;
    }

    public PlaceImageId id() {
        return this.id;
    }

    public int sequence() {
        return sequence;
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public PlaceId placeId() {
        return placeId;
    }

    public void changeSequence(int i) {
        this.sequence = i;
    }

    public static PlaceImage fake(String placeId, int sequence) {
        return PlaceImage.builder()
                .id(PlaceImageId.generate())
                .placeId(new PlaceId(placeId))
                .url("url")
                .sequence(sequence)
                .build();
    }

    public static PlaceImage fake(String placeId, String placeImageId, int sequence) {
        return PlaceImage.builder()
                .id(new PlaceImageId(placeImageId))
                .placeId(new PlaceId(placeId))
                .url("url")
                .sequence(sequence)
                .build();
    }

    public static PlaceImage fake(
            String placeId, int sequence, boolean deleted
    ) {
        return PlaceImage.builder()
                .id(PlaceImageId.generate())
                .placeId(new PlaceId(placeId))
                .url("url")
                .sequence(sequence)
                .deleted(deleted)
                .build();
    }
}
