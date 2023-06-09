package com.truckhelper.application.utils;

import java.lang.reflect.Field;

public class FieldUtils {
    public static <SourceType, DestType> DestType getField(
            SourceType object, String fieldName, Class<DestType> destTypeRef) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (DestType) field.get(object);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
