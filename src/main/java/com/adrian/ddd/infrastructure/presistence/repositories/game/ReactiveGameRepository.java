package com.adrian.ddd.infrastructure.presistence.repositories.game;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.infrastructure.presistence.entities.game.GameEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveGameRepository extends ReactiveCrudRepository<GameEntity, GameId> {
}
