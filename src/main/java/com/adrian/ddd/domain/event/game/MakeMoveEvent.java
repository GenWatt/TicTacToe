package com.adrian.ddd.domain.event.game;

import com.adrian.ddd.domain.event.DomainEvent;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import lombok.Getter;

public class MakeMoveEvent implements DomainEvent {
    @Getter
    private final GameId gameId;
    @Getter
    private final int x;
    @Getter
    private final int y;
    private final long occurredOn;
    @Getter
    private final PlayerType playerType;

    public MakeMoveEvent(GameId gameId, PlayerType playerType, int x, int y) {
        this.gameId = gameId;
        this.playerType = playerType;
        this.x = x;
        this.y = y;
        this.occurredOn = System.currentTimeMillis();
    }

    @Override
    public long occurredOn() {
        return occurredOn;
    }
}
