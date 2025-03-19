package com.adrian.ddd.application.handler;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.MakeMoveCommand;
import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.infrastructure.presistence.mappers.game.GameMapperImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.game.GameRepositoryImpl;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MakeMoveHandler {
    private final GameRepositoryImpl gameRepository;
    private final GameMapperImpl mapper;

    @Autowired
    public MakeMoveHandler(GameRepositoryImpl gameRepository, GameMapperImpl mapper) {
        this.gameRepository = gameRepository;
        this.mapper = mapper;
    }

    public Mono<Either<String, GameDto>> handle(MakeMoveCommand command) {
        return gameRepository.findById(command.getGameId())
                .flatMap(game -> {
                    Either<String, Board> result = game.makeMove(command.getX(), command.getY());
                    if (result.isLeft()) {
                        return Mono.just(Either.left(result.getLeft()));
                    } else {
                        return gameRepository.update(game).thenReturn(Either.right(mapper.toDto(game)));
                    }
                });
    }
}
