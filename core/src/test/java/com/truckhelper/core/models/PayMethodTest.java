package com.truckhelper.core.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PayMethodTest {
    @Test
    void convert() {
        assertThat(PayMethod.of("card")).isEqualTo(PayMethod.Card);
    }
}
