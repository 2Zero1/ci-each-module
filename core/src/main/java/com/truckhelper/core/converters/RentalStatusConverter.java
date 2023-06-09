package com.truckhelper.core.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.truckhelper.core.models.RentalStatus;

@Converter(autoApply = true)
public class RentalStatusConverter
        implements AttributeConverter<RentalStatus, String> {
    @Override
    public String convertToDatabaseColumn(RentalStatus rentalStatus) {
        return rentalStatus.toString();
    }

    @Override
    public RentalStatus convertToEntityAttribute(String value) {
        return RentalStatus.of(value);
    }
}
