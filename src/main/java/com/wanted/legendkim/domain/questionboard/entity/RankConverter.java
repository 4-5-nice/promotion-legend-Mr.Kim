package com.wanted.legendkim.domain.questionboard.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RankConverter implements AttributeConverter<Rank, String> {

    // entity 값을 DB에 저장할 수 있게 변환
    @Override
    public String convertToDatabaseColumn(Rank attribute) {

        return attribute.getLabel(); // DB용 문자열을 반환
    }

    // DB 의 문자열을 entity 값으로 변환
    @Override
    public Rank convertToEntityAttribute(String dbData) {
                                        // DB 에서 읽어온 값

        return Rank.fromLabel(dbData); // enum으로 바꿔서 반환
    }
}
