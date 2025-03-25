package com.adrian.ddd.domain.repository;

import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IPlayerRepository {
    Mono<Player> createPlayer(Player player);
    Mono<Player> updatePlayerType(PlayerId playerId, PlayerType playerType);
    Mono<Player> updateScore(PlayerId playerId, int score);
    Mono<Optional<Player>> findPlayerById(PlayerId playerId);
    Mono<Optional<Player>> findByUsername(String username);
    Flux<Player> findAll();
}
