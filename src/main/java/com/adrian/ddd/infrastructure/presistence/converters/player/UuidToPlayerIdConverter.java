package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.UUID;

@ReadingConverter
public class UuidToPlayerIdConverter implements Converter<UUID, PlayerId> {
    @Override
    public PlayerId convert(UUID source) {
        return new PlayerId(source);
    }
}
