package com.adrian.ddd.infrastructure.presistence.converters.board;

import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class BoardReadingConverter implements Converter<String, Board> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Board convert(String source) {
        try {
            PlayerType[][] cells = objectMapper.readValue(source, PlayerType[][].class);
            return new Board(cells);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize board", e);
        }
    }
}