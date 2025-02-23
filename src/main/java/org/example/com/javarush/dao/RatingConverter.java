package org.example.com.javarush.dao;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.com.javarush.domain.Rating;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        if (rating == null) {
            return null;
        }
        return rating.getValue();
    }

    @Override
    public Rating convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        Rating[] values = Rating.values();
        for (Rating value : values) {
            if (value.getValue().equals(s)) {
                return value;
            }
        }
        return null;
    }
}
