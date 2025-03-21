package com.adrian.ddd.domain.repository;

import com.adrian.ddd.domain.models.aggregate.game.Game;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGameRepository {
    Mono<Game> create(Game game);

    Mono<Game> findById(GameId id);

    Flux<Game> findAll();

    Mono<Void> delete(GameId id);

    Mono<Void> update(Game game);
}
