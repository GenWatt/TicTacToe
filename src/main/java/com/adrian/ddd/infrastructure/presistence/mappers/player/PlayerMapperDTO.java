package com.adrian.ddd.infrastructure.presistence.mappers.player;

import com.adrian.ddd.api.dto.PlayerDto;
import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.domain.models.valueObject.player.Score;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapperDTO {
    @Mapping(source = "playerId.id", target = "id")
    @Mapping(source = "score.value", target = "score")
    PlayerDto toDto(Player player);

    default Player toDomain(PlayerDto playerDto) {
        return Player.reconstruct(
                new PlayerId(playerDto.id()),
                playerDto.username(),
                playerDto.playerType(),
                new Score(playerDto.score()));
    }
}
