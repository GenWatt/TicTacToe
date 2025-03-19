package com.adrian.ddd.infrastructure.presistence.converters.game;

import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class ShortToGameStatus implements Converter<Short, GameStatus> {
    @Override
    public GameStatus convert(Short source) {
        return GameStatus.values()[source.intValue()];
    }
}