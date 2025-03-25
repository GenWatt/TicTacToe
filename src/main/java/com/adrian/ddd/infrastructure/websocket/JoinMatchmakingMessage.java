package com.adrian.ddd.infrastructure.websocket;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class JoinMatchmakingMessage extends BaseSocketMessage {
    private UUID playerId;
}