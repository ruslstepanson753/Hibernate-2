package org.example.com.javarush.dao;

import jakarta.persistence.AttributeConverter;

import java.time.Year;

public class YearAttributeConverter implements AttributeConverter<Year, Short> {

    @Override
    public Short convertToDatabaseColumn(Year year) {
        if (year == null) {
            return null;
        }
        return Short.valueOf((short) year.getValue());
    }

    @Override
    public Year convertToEntityAttribute(Short numder) {
        if (numder == null) {
            return null;
        }
        return Year.of(numder);
    }
}
