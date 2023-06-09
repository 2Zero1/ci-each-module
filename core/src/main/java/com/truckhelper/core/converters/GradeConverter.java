package com.truckhelper.core.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.truckhelper.core.models.Grade;


@Converter(autoApply = true)
public class GradeConverter implements AttributeConverter<Grade, String> {
    @Override
    public String convertToDatabaseColumn(Grade grade) {
        return grade.toString();
    }

    @Override
    public Grade convertToEntityAttribute(String value) {
        return Grade.of(value);
    }
}
