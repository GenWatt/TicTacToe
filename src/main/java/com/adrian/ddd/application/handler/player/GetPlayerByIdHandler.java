package com.adrian.ddd.application.handler.player;

import com.adrian.ddd.api.dto.PlayerDto;
import com.adrian.ddd.application.query.player.GetPlayerByIdQuery;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.infrastructure.presistence.mappers.player.PlayerMapperDTOImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.player.PlayerRepositoryImpl;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetPlayerByIdHandler {

    private final PlayerRepositoryImpl playerRepository;
    private final PlayerMapperDTOImpl playerMapper;

    public GetPlayerByIdHandler(PlayerRepositoryImpl playerRepository, PlayerMapperDTOImpl playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public Mono<Either<String, PlayerDto>> handle(GetPlayerByIdQuery command) {
        return playerRepository.findPlayerById(new PlayerId(command.playerId()))
                .flatMap(player ->  {
                    if (player.isEmpty()) {
                        return Mono.just(Either.left("Player not found"));
                    }
                    return Mono.just(Either.right(playerMapper.toDto(player.get())));
                });
    }
}
