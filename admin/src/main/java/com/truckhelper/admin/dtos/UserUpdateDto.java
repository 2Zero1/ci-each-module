package com.truckhelper.admin.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UserUpdateDto {
    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address1;

    private String address2;

    @NotBlank
    private String vehiclePlate;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String carModel;

    @NotBlank
    private String vehicleType;

    @NotBlank
    private String loadingWeight;

    @NotBlank
    private String loadingLength;

    @NotBlank
    private String length;

    @NotBlank
    private String height;

    @NotBlank
    private String width;
}
