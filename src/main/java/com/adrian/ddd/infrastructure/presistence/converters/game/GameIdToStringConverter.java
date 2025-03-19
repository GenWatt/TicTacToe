package com.adrian.ddd.infrastructure.presistence.converters.game;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class GameIdToStringConverter implements Converter<GameId, String> {
    @Override
    public String convert(GameId source) {
        return source.getId().toString();
    }
}