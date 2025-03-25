package com.adrian.ddd.application.command.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameCommand {
    UUID player1Id;
    UUID player2Id;
}
