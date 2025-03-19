package com.adrian.ddd.application.query;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetGameByIdQuery {
    private GameId gameId;

    public GetGameByIdQuery(GameId gameId) {
        this.gameId = gameId;
    }
}
