package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.player.Score;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class IntToScoreConverter implements Converter<Integer, Score> {
    @Override
    public Score convert(Integer source) {
        return new Score(source);
    }
}
