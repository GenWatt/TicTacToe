package com.adrian.ddd.application.events;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayerAlreadyInQueueEvent {
    private final UUID playerId;

    public PlayerAlreadyInQueueEvent(UUID playerId) {
        this.playerId = playerId;
    }
}
