package com.truckhelper.admin.dtos;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class AdminUpdateDto {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private boolean inactive;

    public AdminUpdateDto(
            String name,
            String phoneNumber,
            boolean inactive
    ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.inactive = inactive;
    }

    public static AdminUpdateDto fake(String phoneNumber) {
        return new AdminUpdateDto("test-dev", phoneNumber, false);
    }
}
