package com.adrian.ddd.infrastructure.presistence.mappers.game;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.domain.models.entities.Game;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(source = "id.id", target = "id")
    GameDto toDto(Game game);

    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToGameId")
    Game toEntity(GameDto gameDto);

    @Named("uuidToGameId")
    default GameId uuidToGameId(UUID id) {
        if (id == null) {
            return null;
        }
        return new GameId(id);
    }
}
