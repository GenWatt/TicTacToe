package com.adrian.ddd.infrastructure.websocket;

import com.adrian.ddd.application.command.game.MakeMoveCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakeMoveMessage extends BaseSocketMessage {
    private MakeMoveCommand makeMoveCommand;

    public MakeMoveCommand getMakeMoveCommand() {
        return makeMoveCommand;
    }
}
