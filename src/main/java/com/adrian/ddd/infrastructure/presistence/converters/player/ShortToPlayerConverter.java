package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.Player;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class ShortToPlayerConverter implements Converter<Short, Player> {
    @Override
    public Player convert(Short source) {
        return Player.values()[source.intValue()];
    }
}