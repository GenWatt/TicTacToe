package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.UUID;

@WritingConverter
public class PlayerIdToUuidConverter implements Converter<PlayerId, UUID> {
    @Override
    public UUID convert(PlayerId source) {
        return source.id();
    }
}
