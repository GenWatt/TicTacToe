package com.adrian.ddd.application.handler.player;

import com.adrian.ddd.api.dto.PlayerDto;
import com.adrian.ddd.application.command.player.UpdatePlayerTypeCommand;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.infrastructure.presistence.mappers.player.PlayerMapperDTOImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.player.PlayerRepositoryImpl;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UpdatePlayerTypeHandler {
    private final PlayerRepositoryImpl playerRepository;
    private final PlayerMapperDTOImpl playerMapper;

    public UpdatePlayerTypeHandler(PlayerRepositoryImpl playerRepository, PlayerMapperDTOImpl playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public Mono<Either<String, PlayerDto>> handle(UpdatePlayerTypeCommand command) {
        return playerRepository.findPlayerById(new PlayerId(command.getPlayerId()))
                .flatMap(player -> {
                    return playerRepository.updatePlayerType(new PlayerId(command.getPlayerId()), command.getPlayerType())
                            .map(playerMapper::toDto);
                })
                .map(Either::right);
    }
}