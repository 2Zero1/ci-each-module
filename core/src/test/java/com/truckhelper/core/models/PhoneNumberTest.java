package com.truckhelper.core.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberTest {
    @Test
    void removeHyphen() {
        assertThat(PhoneNumber.of("010-1234-5678"))
                .isEqualTo(PhoneNumber.of("01012345678"));
    }
}
