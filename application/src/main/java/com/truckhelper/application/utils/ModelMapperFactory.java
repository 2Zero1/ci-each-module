package com.truckhelper.application.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import static org.modelmapper.config.Configuration.AccessLevel;

import com.truckhelper.core.models.Address;
import com.truckhelper.core.models.EmailAddress;
import com.truckhelper.core.models.Money;
import com.truckhelper.core.models.PhoneNumber;
import com.truckhelper.core.models.RentalId;

public class ModelMapperFactory {
    public ModelMapper create() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        modelMapper.addConverter(rentalIdConverter(),
                RentalId.class, String.class);

        modelMapper.addConverter(localDateTimeConverter(),
                LocalDateTime.class, String.class);

        modelMapper.addConverter(moneyToLongConverter(),
                Money.class, Long.class);

        modelMapper.addConverter(addressToStringConverter(),
                Address.class, String.class);

        modelMapper.addConverter(
                context -> PhoneNumber.of(context.getSource()),
                String.class, PhoneNumber.class);

        modelMapper.addConverter(
                context -> EmailAddress.of(context.getSource()),
                String.class, EmailAddress.class);

        return modelMapper;
    }

    private static Converter<RentalId, String> rentalIdConverter() {
        return context -> context.getSource().toString();
    }

    private static Converter<LocalDateTime, String> localDateTimeConverter() {
        return context -> {
            LocalDateTime source = context.getSource();
            if (source == null) {
                return null;
            }
            ZonedDateTime dateTime = source.atZone(ZoneOffset.systemDefault());
            return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        };
    }

    private static Converter<Money, Long> moneyToLongConverter() {
        return context -> FieldUtils.getField(
                context.getSource(), "amount", Long.class);
    }

    private static Converter<Address, String> addressToStringConverter() {
        return context -> FieldUtils.getField(
                context.getSource(), "address1", String.class);
    }
}
