package com.adrian.ddd.infrastructure.presistence.converters.game;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.UUID;

@ReadingConverter
public class StringToGameIdConverter implements Converter<String, GameId> {
    @Override
    public GameId convert(String source) {
        return new GameId(UUID.fromString(source));
    }
}