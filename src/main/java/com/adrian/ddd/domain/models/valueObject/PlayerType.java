package com.adrian.ddd.domain.models.valueObject;

public enum PlayerType {
    X, O;

    public static PlayerType fromDatabase(Integer playerType) {
        if (playerType == null) {
            return null;
        }

        return playerType == 0 ? X : O;
    }
}
