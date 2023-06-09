package com.truckhelper.admin.utils;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import com.truckhelper.core.models.Address;
import com.truckhelper.core.models.EmailAddress;
import com.truckhelper.core.models.Money;
import com.truckhelper.core.models.PhoneNumber;
import com.truckhelper.core.models.PlaceId;
import com.truckhelper.core.models.PlaceImageId;
import com.truckhelper.core.models.RentalId;
import com.truckhelper.core.models.UserId;

public class ModelMapperFactory {
    private static Converter<Money, Long> moneyToLongConverter() {
        return context -> FieldUtils.getField(
                context.getSource(), "amount", Long.class);
    }

    private static Converter<Address, String> addressToStringConverter() {
        return context -> FieldUtils.getField(
                context.getSource(), "address1", String.class);
    }

    public ModelMapper create() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        modelMapper.addConverter(moneyToLongConverter(),
                Money.class, Long.class);

        modelMapper.addConverter(
                context -> PhoneNumber.of(context.getSource()),
                String.class, PhoneNumber.class);

        modelMapper.addConverter(
                context -> EmailAddress.of(context.getSource()),
                String.class, EmailAddress.class);

        modelMapper.addConverter(addressToStringConverter(),
                Address.class, String.class);

        modelMapper.addConverter(context -> context.getSource().toString(),
                RentalId.class, String.class);

        modelMapper.addConverter(context -> context.getSource().toString(),
                PlaceId.class, String.class);

        modelMapper.addConverter(context -> context.getSource().toString(),
                UserId.class, String.class);

        modelMapper.addConverter(context -> context.getSource().toString(),
                PlaceImageId.class, String.class);

        return modelMapper;
    }
}
