package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.Player;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class PlayerToShortConverter implements Converter<Player, Short> {
    @Override
    public Short convert(Player source) {
        return (short) source.ordinal();
    }
}
