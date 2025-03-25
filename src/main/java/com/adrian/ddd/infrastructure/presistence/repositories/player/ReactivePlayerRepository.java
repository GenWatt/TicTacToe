package com.adrian.ddd.infrastructure.presistence.repositories.player;

import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.infrastructure.presistence.entities.player.PlayerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactivePlayerRepository extends ReactiveCrudRepository<PlayerEntity, PlayerId> {
    Mono<PlayerEntity> findByUsername(String username);
}
