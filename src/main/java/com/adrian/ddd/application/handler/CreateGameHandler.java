package com.adrian.ddd.application.handler;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.CreateGameCommand;
import com.adrian.ddd.domain.models.entities.Game;
import com.adrian.ddd.infrastructure.presistence.mappers.game.GameMapperImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.game.GameRepositoryImpl;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CreateGameHandler {
    private final GameRepositoryImpl gameRepository;
    private final GameMapperImpl mapper;

    public CreateGameHandler(GameRepositoryImpl gameRepository, GameMapperImpl mapper) {
        this.gameRepository = gameRepository;
        this.mapper = mapper;
    }

    public Mono<GameDto> handle(CreateGameCommand command) {
        Game game = new Game();
        return gameRepository.create(game).thenReturn(mapper.toDto(game));
    }
 }
