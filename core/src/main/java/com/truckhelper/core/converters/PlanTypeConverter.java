package com.truckhelper.core.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.truckhelper.core.models.PlanType;

@Converter(autoApply = true)
public class PlanTypeConverter implements AttributeConverter<PlanType, String> {
    @Override
    public String convertToDatabaseColumn(PlanType planType) {
        return planType.toString();
    }

    @Override
    public PlanType convertToEntityAttribute(String value) {
        return PlanType.of(value);
    }
}
