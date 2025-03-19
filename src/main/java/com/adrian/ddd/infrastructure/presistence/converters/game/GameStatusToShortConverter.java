package com.adrian.ddd.infrastructure.presistence.converters.game;

import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class GameStatusToShortConverter implements Converter<GameStatus, Short> {
    @Override
    public Short convert(GameStatus source) {
        return (short) source.ordinal();
    }
}
