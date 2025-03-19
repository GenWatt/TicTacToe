package com.adrian.ddd.infrastructure.presistence.mappers.game;

import com.adrian.ddd.domain.models.entities.Game;
import com.adrian.ddd.infrastructure.presistence.entities.game.GameEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapperEntityDomain {

    Game toDomain(GameEntity gameEntity);

    GameEntity toEntity(Game game);
}
