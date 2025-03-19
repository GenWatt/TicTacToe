package com.adrian.ddd.application.command;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MakeMoveCommand {
    private GameId gameId;
    private int x;
    private int y;

    public MakeMoveCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }
}