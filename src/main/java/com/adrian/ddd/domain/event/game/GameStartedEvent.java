package com.adrian.ddd.domain.event.game;

import com.adrian.ddd.domain.event.DomainEvent;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import lombok.Getter;

public class GameStartedEvent implements DomainEvent {
    @Getter
    GameId gameId;
    Long occurredOn;

    public GameStartedEvent(GameId gameId) {
        this.gameId = gameId;
        this.occurredOn = System.currentTimeMillis();
    }

    @Override
    public long occurredOn() {
        return occurredOn;
    }
}
