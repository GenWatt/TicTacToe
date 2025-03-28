package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.PlayerType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class PlayerToShortConverter implements Converter<PlayerType, Short> {
    @Override
    public Short convert(PlayerType source) {
        return (short) source.ordinal();
    }
}
