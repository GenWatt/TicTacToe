package com.adrian.ddd.infrastructure.presistence.converters.player;

import com.adrian.ddd.domain.models.valueObject.player.Score;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ScoreToIntConverter implements Converter<Score, Integer> {
    @Override
    public Integer convert(Score source) {
        return source.value();
    }
}
