package com.truckhelper.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class KakaoId {
    @Column(name = "kakao_id")
    private String value;

    private KakaoId() {
    }

    public KakaoId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
