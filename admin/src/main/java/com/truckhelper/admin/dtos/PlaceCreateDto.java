package com.truckhelper.admin.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class PlaceCreateDto {
    @NotBlank
    private String name;

    @NotBlank
    private String address1;

    private String address2;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotBlank
    private String introduction;

    @NotBlank
    private String precautions;

    @NotNull
    private PlaceSpacesDto spaces;

    @Size(min = 1)
    private List<PlanDto> plans;

    @NotNull
    private boolean hidden;
}
