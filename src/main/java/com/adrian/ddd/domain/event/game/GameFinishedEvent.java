package com.adrian.ddd.domain.event.game;

import com.adrian.ddd.domain.event.DomainEvent;
import com.adrian.ddd.domain.models.valueObject.Player;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import lombok.Getter;

public class GameFinishedEvent implements DomainEvent {
    @Getter
    private final GameId gameId;
    @Getter
    private final Player winner;
    private final long occurredOn;

    public GameFinishedEvent(GameId gameId, Player winner) {
        this.gameId = gameId;
        this.winner = winner;
        this.occurredOn = System.currentTimeMillis();
    }

    @Override
    public long occurredOn() {
        return occurredOn;
    }
}
