package com.adrian.ddd.application.handler.player;

import com.adrian.ddd.api.dto.PlayerDto;
import com.adrian.ddd.application.query.player.GetPlayerByUsernameQuery;
import com.adrian.ddd.infrastructure.presistence.mappers.player.PlayerMapperDTOImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.player.PlayerRepositoryImpl;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetPlayerByUsernameHandler {

    private final PlayerRepositoryImpl playerRepository;
    private final PlayerMapperDTOImpl playerMapper;

    public GetPlayerByUsernameHandler(PlayerRepositoryImpl playerRepository, PlayerMapperDTOImpl playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public Mono<Either<String, PlayerDto>> handle(GetPlayerByUsernameQuery command) {
        return playerRepository.findByUsername(command.username())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return playerRepository.findByUsername(command.username())
                                .map(optionalPlayer -> {
                                    return optionalPlayer.<Either<String, PlayerDto>>map(player -> Either.right(playerMapper.toDto(player))).orElseGet(() -> Either.left("Player not found."));
                                });
                    } else {
                        return Mono.just(Either.left("Player not found."));
                    }
                });
    }
}
