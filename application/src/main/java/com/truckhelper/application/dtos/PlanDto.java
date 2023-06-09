package com.truckhelper.application.dtos;

import lombok.Getter;

@Getter
public class PlanDto {
    private String type;

    private Long price;

    private PlanDto() {
    }

    public PlanDto(String type, Long price) {
        this.type = type;
        this.price = price;
    }
}
