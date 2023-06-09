package com.truckhelper.admin.dtos;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class AdminCreateDto {
    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    private boolean inactive;

    public AdminCreateDto(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public static AdminCreateDto fake(String phoneNumber) {
        return new AdminCreateDto("gildong", phoneNumber);
    }
}
