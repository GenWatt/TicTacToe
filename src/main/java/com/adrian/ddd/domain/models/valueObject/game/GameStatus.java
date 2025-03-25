package com.adrian.ddd.domain.models.valueObject.game;

public enum GameStatus {
    CREATED,
    IN_PROGRESS,
    FINISHED;

    public static GameStatus fromDb(Integer dbValue) {
        return GameStatus.values()[dbValue];
    }
}
