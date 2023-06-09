package com.truckhelper.core.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {
    @Test
    void equals() {
        assertThat(Money.krw(10L)).isEqualTo(Money.krw(10L));
    }

    @Test
    void plus() {
        Money five = Money.krw(5L);
        Money ten = Money.krw(10L);

        assertThat(five.plus(five)).isEqualTo(ten);
    }

    @Test
    void times() {
        Money five = Money.krw(5L);
        Money ten = Money.krw(10L);

        assertThat(five.times(2)).isEqualTo(ten);
    }
}
