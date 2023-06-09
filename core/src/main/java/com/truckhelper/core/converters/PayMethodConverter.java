package com.truckhelper.core.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.truckhelper.core.models.PayMethod;

@Converter(autoApply = true)
public class PayMethodConverter
        implements AttributeConverter<PayMethod, String> {
    @Override
    public String convertToDatabaseColumn(PayMethod payMethod) {
        return payMethod.toString();
    }

    @Override
    public PayMethod convertToEntityAttribute(String value) {
        return PayMethod.of(value);
    }
}
