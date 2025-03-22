package com.adrian.ddd.application.handler;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.query.GetGameByIdQuery;
import com.adrian.ddd.domain.Result;
import com.adrian.ddd.infrastructure.presistence.mappers.game.GameMapperImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.game.GameRepositoryImpl;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetGameByIdHandler {
    private final GameRepositoryImpl gameRepositoryImpl;
    private final GameMapperImpl mapper;

    public GetGameByIdHandler(GameRepositoryImpl gameRepositoryImpl, GameMapperImpl mapper) {
        this.gameRepositoryImpl = gameRepositoryImpl;
        this.mapper = mapper;
    }

    public Mono<Result<GameDto>> handle(GetGameByIdQuery query) {
        return gameRepositoryImpl.findById(query.getGameId())
                .map(game -> Result.success(mapper.toDto(game)));
    }
}
