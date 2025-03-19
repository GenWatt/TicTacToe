package com.adrian.ddd.infrastructure.presistence.converters.board;

import com.adrian.ddd.domain.models.valueObject.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class BoardWritingConverter implements Converter<Board, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(Board board) {
        try {
            return objectMapper.writeValueAsString(board.getCells());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize board", e);
        }
    }
}