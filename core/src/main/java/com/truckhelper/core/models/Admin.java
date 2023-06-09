package com.truckhelper.core.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Builder;

@Entity
@Table(name = "admins")
public class Admin {
    @EmbeddedId
    private AdminId id;

    private String name;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private KakaoId kakaoId;

    private Grade grade;

    @Column(name = "inactive", nullable = false)
    @ColumnDefault("false")
    private boolean inactive = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Admin() {
    }

    @Builder
    public Admin(
            AdminId id,
            String name,
            PhoneNumber phoneNumber,
            Grade grade,
            boolean inactive
    ) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.grade = grade;
        this.inactive = inactive;
    }

    public static Admin fake(String id, String phoneNumber) {
        return new Admin(
                new AdminId(id),
                "TEST-NAME",
                PhoneNumber.of(phoneNumber),
                Grade.LV1,
                false
        );
    }

    public static Admin fake(String phoneNumber) {
        return new Admin(
                new AdminId("ADMIN-ID"),
                "TEST-NAME",
                PhoneNumber.of(phoneNumber),
                Grade.LV1,
                false
        );
    }

    public static Admin fake(String phoneNumber, Grade grade) {
        return new Admin(
                new AdminId("ADMIN-ID"),
                "TEST-NAME",
                PhoneNumber.of(phoneNumber),
                grade,
                false
        );
    }

    public AdminId id() {
        return id;
    }

    public Grade grade() {
        return this.grade;
    }

    public PhoneNumber phoneNumber() {
        return phoneNumber;
    }

    public void changeKakaoId(KakaoId kakaoId) {
        this.kakaoId = kakaoId;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeInactive(boolean inactive) {
        this.inactive = inactive;
    }
}
