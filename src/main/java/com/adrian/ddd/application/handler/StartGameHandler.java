package com.adrian.ddd.application.handler;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.StartGameCommand;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.infrastructure.presistence.mappers.game.GameMapperImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.game.GameRepositoryImpl;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.NotActiveException;

@Component
public class StartGameHandler {
    private final GameRepositoryImpl gameRepository;
    private final GameMapperImpl mapper;

    public StartGameHandler(GameRepositoryImpl gameRepository, GameMapperImpl mapper) {
        this.gameRepository = gameRepository;
        this.mapper = mapper;
    }

    public Mono<GameDto> handle(StartGameCommand command) {
        GameId gameId = command.getGameId();

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new NotActiveException()))
                .flatMap(game -> {
                    game.startGame();
                    return gameRepository.update(game).thenReturn(mapper.toDto(game));
                });
    }
}
