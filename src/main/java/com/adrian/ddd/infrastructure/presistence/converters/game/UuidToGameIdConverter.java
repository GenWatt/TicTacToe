package com.adrian.ddd.infrastructure.presistence.converters.game;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.UUID;

@ReadingConverter
public class UuidToGameIdConverter implements Converter<UUID, GameId> {
    @Override
    public GameId convert(UUID source) {
        return new GameId(source);
    }
}