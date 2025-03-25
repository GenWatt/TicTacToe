package com.adrian.ddd.application.events;

import com.adrian.ddd.api.dto.PlayerGameDto;
import com.adrian.ddd.application.messages.GameFoundResponseMessage;
import com.adrian.ddd.application.messages.InQueueResponseMessage;
import com.adrian.ddd.application.services.MatchmakingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.util.UUID;

@Component
public class MatchmakingEventListener {

    private final MatchmakingService matchmakingService;
    private final ObjectMapper objectMapper;

    public MatchmakingEventListener(MatchmakingService matchmakingService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.matchmakingService = matchmakingService;
    }

    @EventListener
    public void onPlayerAlreadyInQueue(PlayerAlreadyInQueueEvent event) {
        System.out.println("Player already in queue: " + event.getPlayerId());

        // Send a message to the player
        WebSocketSession session = matchmakingService.getSession(event.getPlayerId());
        session.getAttributes().put("state", "IN_QUEUE");
        session.getAttributes().put("playerId", event.getPlayerId());

        // Send a message to the player
        InQueueResponseMessage message = new InQueueResponseMessage();
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        @SuppressWarnings("unchecked")
        FluxSink<WebSocketMessage> sink = (FluxSink<WebSocketMessage>) session.getAttributes().get("outgoingSink");
        sink.next(session.textMessage(jsonMessage));
    }

    // When a player is added to the queue, try to match players
    @EventListener
    public void onPlayerAddedToQueue(PlayerAddedToQueueEvent event) {
        System.out.println("Player added to queue: " + event.getPlayerId());

        // Send a message to the player
        WebSocketSession session = event.getSession();
        session.getAttributes().put("state", "IN_QUEUE");
        session.getAttributes().put("playerId", event.getPlayerId());

        // Send a message to the player
        InQueueResponseMessage message = new InQueueResponseMessage();
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        @SuppressWarnings("unchecked")
        FluxSink<WebSocketMessage> sink = (FluxSink<WebSocketMessage>) session.getAttributes().get("outgoingSink");
        sink.next(session.textMessage(jsonMessage));

        matchmakingService.tryMatchPlayers();
    }

    // When a game is found, send the game details to both players
    @EventListener
    public void onGameFound(GameFoundEvent event) throws JsonProcessingException {
        PlayerGameDto gameDto = event.getPlayerGameDto();
        UUID gameId = gameDto.getGameDto().id();
        UUID player1Id = event.getPlayer1Id();
        UUID player2Id = event.getPlayer2Id();

        // Retrieve sessions from the service (or inject a shared session store)
        WebSocketSession session1 = matchmakingService.getSession(player1Id);
        WebSocketSession session2 = matchmakingService.getSession(player2Id);

        if (session1 != null && session2 != null) {
            session1.getAttributes().put("state", "IN_GAME");
            session1.getAttributes().put("gameId", gameId);
            session2.getAttributes().put("state", "IN_GAME");
            session2.getAttributes().put("gameId", gameId);

            GameFoundResponseMessage message = new GameFoundResponseMessage(gameDto, player1Id, player2Id);
            String jsonMessage = objectMapper.writeValueAsString(message);

            @SuppressWarnings("unchecked")
            FluxSink<WebSocketMessage> sink1 = (FluxSink<WebSocketMessage>) session1.getAttributes().get("outgoingSink");
            @SuppressWarnings("unchecked")
            FluxSink<WebSocketMessage> sink2 = (FluxSink<WebSocketMessage>) session2.getAttributes().get("outgoingSink");

            if (sink1 != null) {
                sink1.next(session1.textMessage(jsonMessage));
            }
            if (sink2 != null) {
                sink2.next(session2.textMessage(jsonMessage));
            }

            // Clean up session maps
            matchmakingService.removePlayer(player1Id);
            matchmakingService.removePlayer(player2Id);
        }
    }
}
