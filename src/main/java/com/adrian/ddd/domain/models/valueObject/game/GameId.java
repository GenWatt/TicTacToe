package com.adrian.ddd.domain.models.valueObject.game;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class GameId implements Serializable {
    UUID id;

    public GameId(UUID id) {
        this.id = id;
    }
}