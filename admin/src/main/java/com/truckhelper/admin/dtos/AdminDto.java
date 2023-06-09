package com.truckhelper.admin.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminDto {
    private String id;

    private String name;

    private String kakaoId;

    private String phoneNumber;

    private String grade;

    private boolean inactive;

    private AdminDto() {
    }

    @Builder
    public AdminDto(
            String id,
            String name,
            String kakaoId,
            String phoneNumber,
            String grade,
            boolean inactive
    ) {
        this.id = id;
        this.name = name;
        this.kakaoId = kakaoId;
        this.phoneNumber = phoneNumber;
        this.grade = grade;
        this.inactive = inactive;
    }
}
