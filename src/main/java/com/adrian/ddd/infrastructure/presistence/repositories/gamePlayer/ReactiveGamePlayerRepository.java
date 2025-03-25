package com.adrian.ddd.infrastructure.presistence.repositories.gamePlayer;

import com.adrian.ddd.api.dto.PlayerGameDto;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.infrastructure.presistence.entities.gamePlayerEntity.GamePlayerEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveGamePlayerRepository extends ReactiveCrudRepository<GamePlayerEntity, UUID> {

}
