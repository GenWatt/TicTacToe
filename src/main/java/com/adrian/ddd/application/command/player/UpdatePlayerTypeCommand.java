package com.adrian.ddd.application.command.player;

import com.adrian.ddd.domain.models.valueObject.PlayerType;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdatePlayerTypeCommand {
    UUID playerId;
    PlayerType playerType;

    public UpdatePlayerTypeCommand(UUID playerId, PlayerType playerType) {
        this.playerId = playerId;
        this.playerType = playerType;
    }
}
