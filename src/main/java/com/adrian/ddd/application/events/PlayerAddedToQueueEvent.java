package com.adrian.ddd.application.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PlayerAddedToQueueEvent {
    private final UUID playerId;
    private final WebSocketSession session;
}