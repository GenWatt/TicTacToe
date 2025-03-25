package com.adrian.ddd.application.handler.player;

import com.adrian.ddd.api.dto.PlayerDto;
import com.adrian.ddd.application.command.player.CreatePlayerCommand;
import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.infrastructure.presistence.mappers.player.PlayerMapperDTOImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.player.PlayerRepositoryImpl;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CreatePlayerHandler {
    private final PlayerRepositoryImpl playerRepository;
    private final PlayerMapperDTOImpl playerMapper;

    public CreatePlayerHandler(PlayerRepositoryImpl playerRepository, PlayerMapperDTOImpl playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public Mono<Either<String, PlayerDto>> handle(CreatePlayerCommand command) {
        return playerRepository.findByUsername(command.username())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.just(Either.left("Username already exists."));
                    } else {
                        Either<String, Player> playerEither = Player.create(command.username());
                        return playerEither.fold(
                                error -> Mono.just(Either.left(error)),
                                player -> playerRepository.createPlayer(player)
                                        .map(playerMapper::toDto)
                                        .map(Either::right)
                        );
                    }
                });
    }
}
