package com.adrian.ddd.application.command.player;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateScoreCommand {
    UUID playerId;
    int score;
}
