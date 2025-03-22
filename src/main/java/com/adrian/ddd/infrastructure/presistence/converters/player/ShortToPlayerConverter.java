package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.PlayerType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class ShortToPlayerConverter implements Converter<Short, PlayerType> {
    @Override
    public PlayerType convert(Short source) {
        return PlayerType.values()[source.intValue()];
    }
}