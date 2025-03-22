package com.adrian.ddd.infrastructure.presistence.converters.game;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.UUID;

@WritingConverter
public class GameIdToUuidConverter implements Converter<GameId, UUID> {
    @Override
    public UUID convert(GameId source) {
        return source.getId();
    }
}