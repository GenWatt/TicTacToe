package com.adrian.ddd.infrastructure.presistence.mappers.player;

import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.infrastructure.presistence.entities.player.PlayerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapperEntityDomain {
    default Player toDomain(PlayerEntity playerEntity) {
        return Player.reconstruct(playerEntity.getId(), playerEntity.getUsername(), playerEntity.getPlayerType(), playerEntity.getScore());
    }

    default PlayerEntity toEntity(Player player) {
        return new PlayerEntity(player);
    }
}
