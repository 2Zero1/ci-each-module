package com.truckhelper.application.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

import com.truckhelper.core.models.Place;
import com.truckhelper.core.models.Plan;
import com.truckhelper.core.models.PlanType;

@Getter
public class ReserveDto {
    @NotBlank
    private String placeId;

    @NotBlank
    private String method;

    @NotBlank
    private String plan;

    @Min(0)
    private Long price;

    @NotBlank
    private String beginningDate;

    @NotBlank
    private String receiptId;

    private ReserveDto() {
    }

    @Builder
    public ReserveDto(String placeId, String method, String plan, Long price,
                      String beginningDate, String receiptId) {
        this.placeId = placeId;
        this.method = method;
        this.plan = plan;
        this.price = price;
        this.beginningDate = beginningDate;
        this.receiptId = receiptId;
    }

    public static ReserveDto fake(Place place) {
        Plan plan = place.plan(PlanType.Monthly).get();

        return ReserveDto.builder()
                .placeId(place.id().toString())
                .method("card")
                .plan("monthly")
                .price(plan.priceWithVAT().asLong())
                .beginningDate("2022-11-20")
                .receiptId("624e4f7c1fc19202e4746f91")
                .build();
    }
}
