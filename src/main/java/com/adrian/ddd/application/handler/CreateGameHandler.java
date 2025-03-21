package com.adrian.ddd.application.handler;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.CreateGameCommand;
import com.adrian.ddd.domain.models.aggregate.game.Game;
import com.adrian.ddd.infrastructure.presistence.mappers.game.GameMapperImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.game.GameRepositoryImpl;
import io.vavr.control.Either;
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

    public Mono<Either<String, GameDto>> handle(CreateGameCommand command) {
        return Mono.fromSupplier(Game::createGame)
                .flatMap(game -> gameRepository.create(game.get())
                        .map(savedGame -> Either.right(mapper.toDto(savedGame))));
    }
 }
