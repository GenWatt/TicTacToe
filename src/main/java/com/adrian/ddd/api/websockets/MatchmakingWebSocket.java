package com.adrian.ddd.api.websockets;

import com.adrian.ddd.application.handler.game.MakeMoveHandler;
import com.adrian.ddd.application.services.MatchmakingService;
import com.adrian.ddd.infrastructure.websocket.BaseSocketMessage;
import com.adrian.ddd.infrastructure.websocket.JoinMatchmakingMessage;
import com.adrian.ddd.infrastructure.websocket.MakeMoveMessage;
import com.adrian.ddd.infrastructure.websocket.WebSocketSessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.util.UUID;

@Component
public class MatchmakingWebSocket implements WebSocketHandler {

    private final MatchmakingService matchmakingService;
    private final ObjectMapper objectMapper;
    private final MakeMoveHandler makeMoveHandler;
    private final WebSocketSessionManager webSocketSessionManager;

    public MatchmakingWebSocket(MatchmakingService matchmakingService, MakeMoveHandler makeMoveHandler, WebSocketSessionManager webSocketSessionManager, ObjectMapper objectMapper) {
        this.matchmakingService = matchmakingService;
        this.makeMoveHandler = makeMoveHandler;
        this.webSocketSessionManager = webSocketSessionManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // Initialize session state
        session.getAttributes().put("state", "CONNECTED");

        // Create a UnicastProcessor for outgoing messages
        UnicastProcessor<WebSocketMessage> processor = UnicastProcessor.create();
        Flux<WebSocketMessage> outgoing = processor.doOnCancel(processor::onComplete);

        // Store the sink in session attributes for later use
        session.getAttributes().put("outgoingSink", processor.sink());

        // Send all messages through this Flux
        Mono<Void> sendMono = session.send(outgoing);

//        webSocketSessionManager.addSession(session.getId(), session);

        // Handle incoming messages (adjust as per your needs)
        Mono<Void> receiveMono = session.receive()
                .map(WebSocketMessage::getPayloadAsText) // Convert message to String
                .doOnNext(payload -> {
                    BaseSocketMessage baseMessage = null;
                    System.out.println("Received message: " + payload);
                    try {
                        baseMessage = objectMapper.readValue(payload, BaseSocketMessage.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    switch (baseMessage.getType()) {
                        case JOIN_MATCHMAKING:
                            JoinMatchmakingMessage joinMessage = (JoinMatchmakingMessage) baseMessage;
                            UUID playerId = joinMessage.getPlayerId();
                            matchmakingService.addToQueue(playerId, session);
                            break;
                        case MAKE_MOVE:
                            MakeMoveMessage moveMessage = (MakeMoveMessage) baseMessage;

                            makeMoveHandler.handle(moveMessage.getMakeMoveCommand());
                            break;
                        default:
                            System.out.println("Unknown message type: " + baseMessage.getType());
                    }
                })
                .doOnError(Throwable::printStackTrace)
                .then();

        // Clean up on session close
        return Mono.zip(sendMono, receiveMono)
                .doFinally(signal -> {
                    matchmakingService.removeFromQueue(session);
                    processor.onComplete();
                })
                .then();
    }
}
