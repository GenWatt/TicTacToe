package com.adrian.ddd.infrastructure.config;

import com.adrian.ddd.infrastructure.presistence.converters.board.BoardReadingConverter;
import com.adrian.ddd.infrastructure.presistence.converters.board.BoardWritingConverter;
import com.adrian.ddd.infrastructure.presistence.converters.game.StringToGameIdConverter;
import com.adrian.ddd.infrastructure.presistence.converters.game.GameIdToStringConverter;
import com.adrian.ddd.infrastructure.presistence.converters.game.GameStatusToShortConverter;
import com.adrian.ddd.infrastructure.presistence.converters.game.ShortToGameStatus;
import com.adrian.ddd.infrastructure.presistence.converters.player.PlayerToShortConverter;
import com.adrian.ddd.infrastructure.presistence.converters.player.ShortToPlayerConverter;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.SqlServerDialect;

import java.util.Arrays;
import java.util.List;

@Configuration
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory;

    public R2dbcConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Bean
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<?> converters = Arrays.asList(
                new StringToGameIdConverter(),
                new GameIdToStringConverter(),
                new BoardReadingConverter(),
                new BoardWritingConverter(),
                new PlayerToShortConverter(),
                new ShortToPlayerConverter(),
                new ShortToGameStatus(),
                new GameStatusToShortConverter()
        );

        return R2dbcCustomConversions.of(SqlServerDialect.INSTANCE, converters);
    }
}