package com.truckhelper.admin.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RentalDto {
    private String id;

    private String status;

    private UserDto user;

    private PlaceDto place;

    private String plan;

    private String beginningDate;

    private String endDate;

    private long price;

    private String method;

    private String receiptId;

    private String paidAt;

    private String createdAt;

    @Builder
    public RentalDto(
            String id,
            String status,
            UserDto user,
            PlaceDto place,
            String plan,
            String beginningDate,
            String endDate,
            long price,
            String method,
            String receiptId,
            String paidAt,
            String createdAt
    ) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.place = place;
        this.plan = plan;
        this.beginningDate = beginningDate;
        this.endDate = endDate;
        this.price = price;
        this.method = method;
        this.receiptId = receiptId;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
    }

    public static RentalDto fake() {
        return RentalDto.builder()
                .build();
    }
}
