package com.truckhelper.core.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.truckhelper.core.models.Money;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Long> {
    @Override
    public Long convertToDatabaseColumn(Money money) {
        return money.asLong();
    }


    @Override
    public Money convertToEntityAttribute(Long value) {
        return Money.krw(value);
    }
}
