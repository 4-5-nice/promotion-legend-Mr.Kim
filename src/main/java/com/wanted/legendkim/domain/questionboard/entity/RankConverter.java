package com.wanted.legendkim.domain.questionboard.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RankConverter implements AttributeConverter<Rank, String> {

    @Override
    public String convertToDatabaseColumn(Rank attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getLabel();
    }

    @Override
    public Rank convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Rank.fromLabel(dbData);
    }
}
