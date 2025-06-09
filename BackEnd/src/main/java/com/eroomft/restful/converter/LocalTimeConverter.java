package com.eroomft.restful.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Time;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {

    @Override
    public Time convertToDatabaseColumn(LocalTime localTime) {
        return localTime != null ? Time.valueOf(localTime) : null;
    }

    @Override
    public LocalTime convertToEntityAttribute(Time time) {
        return time != null ? time.toLocalTime() : null;
    }
}