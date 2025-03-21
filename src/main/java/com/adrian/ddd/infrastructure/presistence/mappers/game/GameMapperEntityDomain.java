package com.adrian.ddd.infrastructure.presistence.mappers.game;

import com.adrian.ddd.domain.models.aggregate.game.Game;
import com.adrian.ddd.infrastructure.presistence.entities.game.GameEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapperEntityDomain {

    default Game toDomain(GameEntity gameEntity) {
        return Game.reconstruct(gameEntity.getId(), gameEntity.getBoard(), gameEntity.getCurrentPlayer(), gameEntity.getWinner(), gameEntity.getFinished());
    }

    default GameEntity toEntity(Game game) {
        return new GameEntity(game);
    }
}
