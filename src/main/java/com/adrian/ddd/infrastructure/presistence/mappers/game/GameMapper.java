package com.adrian.ddd.infrastructure.presistence.mappers.game;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.domain.models.aggregate.game.Game;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(source = "id.id", target = "id")
    GameDto toDto(Game game);

    default Game toEntity(GameDto gameDto) {
        return Game.reconstruct(new GameId(gameDto.id()), gameDto.board(), gameDto.currentPlayerType(), gameDto.winner(), gameDto.gameStatus());
    }
}
