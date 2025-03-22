package com.adrian.ddd.domain.repository;

import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import reactor.core.publisher.Mono;

public interface IPlayerRepository {
    Mono<Player> createPlayer(String username);
    Mono<Player> updatePlayerType(String playerId, PlayerType playerType);
    Mono<Player> updateScore(String playerId, int score);
    Mono<Player> getPlayerById(String playerId);
    Mono<Player> getPlayerByUsername(String username);
}
