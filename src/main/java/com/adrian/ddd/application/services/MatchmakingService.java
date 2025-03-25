package com.adrian.ddd.application.services;

import com.adrian.ddd.application.command.game.CreateGameCommand;
import com.adrian.ddd.application.events.GameFoundEvent;
import com.adrian.ddd.application.events.PlayerAddedToQueueEvent;
import com.adrian.ddd.application.events.PlayerAlreadyInQueueEvent;
import com.adrian.ddd.application.handler.game.CreateGameHandler;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.infrastructure.presistence.repositories.gamePlayer.GamePlayerRepositoryImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MatchmakingService {

    private final Queue<UUID> matchmakingQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<UUID, WebSocketSession> playerSessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketSession, UUID> sessionToPlayer = new ConcurrentHashMap<>();
    private final CreateGameHandler createGameHandler;
    private final ApplicationEventPublisher publisher;
    private final GamePlayerRepositoryImpl gamePlayerRepository;

    public MatchmakingService(CreateGameHandler gameCommandHandler, ApplicationEventPublisher publisher, GamePlayerRepositoryImpl gamePlayerRepository) {
        this.createGameHandler = gameCommandHandler;
        this.gamePlayerRepository = gamePlayerRepository;
        this.publisher = publisher;
    }

    public void addToQueue(UUID playerId, WebSocketSession session) {
        if (playerSessions.containsKey(playerId)) {
            publisher.publishEvent(new PlayerAlreadyInQueueEvent(playerId));
            return;
        }

        matchmakingQueue.add(playerId);
        playerSessions.put(playerId, session);
        sessionToPlayer.put(session, playerId);

        // Publish an event for a player joining the queue
        publisher.publishEvent(new PlayerAddedToQueueEvent(playerId, session));
    }

    public void removeFromQueue(WebSocketSession session) {
        UUID playerId = sessionToPlayer.remove(session);
        if (playerId != null) {
            matchmakingQueue.remove(playerId);
            playerSessions.remove(playerId);
        }
    }

    public Boolean tryMatchPlayers() {
        while (matchmakingQueue.size() >= 2) {
            UUID player1 = matchmakingQueue.poll();
            UUID player2 = matchmakingQueue.poll();
            if (player1 != null && player2 != null) {
                createGame(player1, player2);

                return true;
            }
        }

        return false;
    }

    private void createGame(UUID player1Id, UUID player2Id) {
        CreateGameCommand command = new CreateGameCommand(player1Id, player2Id);
        createGameHandler.handle(command)
                .doOnSuccess(result -> {
                    if (result.isRight()) {
                        gamePlayerRepository.findInProgressGameByPlayerId(new PlayerId(player1Id))
                                .doOnSuccess(game -> {
                                    System.out.println("Game found: " + game);
                                    publisher.publishEvent(new GameFoundEvent(game, player1Id, player2Id));
                                }).subscribe();
                    } else {
                        System.out.println("Error creating game: " + result.getLeft());
                        throw new RuntimeException("Error creating game: " + result.getLeft());
                    }
                })
                .subscribe();
    }

    // Getters for session maps, if needed by event listeners
    public WebSocketSession getSession(UUID playerId) {
        return playerSessions.get(playerId);
    }

    public void removePlayer(UUID playerId) {
        WebSocketSession session = playerSessions.remove(playerId);
        if (session != null) {
            sessionToPlayer.remove(session);
        }
    }
}