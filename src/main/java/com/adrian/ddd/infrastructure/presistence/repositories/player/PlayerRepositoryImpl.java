package com.adrian.ddd.infrastructure.presistence.repositories.player;

import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.domain.models.valueObject.player.Score;
import com.adrian.ddd.domain.repository.IPlayerRepository;
import com.adrian.ddd.infrastructure.presistence.entities.player.PlayerEntity;
import com.adrian.ddd.infrastructure.presistence.mappers.player.PlayerMapperEntityDomainImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@Transactional
public class PlayerRepositoryImpl implements IPlayerRepository {
    private final ReactivePlayerRepository reactivePlayerRepository;
    private final PlayerMapperEntityDomainImpl playerMapper;

    public PlayerRepositoryImpl(ReactivePlayerRepository reactivePlayerRepository, PlayerMapperEntityDomainImpl playerMapper) {
        this.reactivePlayerRepository = reactivePlayerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public Mono<Player> createPlayer(Player player) {
        return reactivePlayerRepository.save(playerMapper.toEntity(player))
                .map(playerMapper::toDomain);
    }

    @Override
    public Mono<Player> updatePlayerType(PlayerId playerId, PlayerType playerType) {
        return reactivePlayerRepository.findById(playerId)
                .map(playerMapper::toDomain)
                .map(player -> {
                    player.updatePlayerType(playerType);
                    return player;
                })
                .flatMap(player -> {
                    PlayerEntity playerEntity = playerMapper.toEntity(player);
                    playerEntity.setNew(false);
                   return reactivePlayerRepository.save(playerEntity);
                })
                .map(playerMapper::toDomain);
    }

    @Override
    public Mono<Player> updateScore(PlayerId playerId, int score) {
        return reactivePlayerRepository.findById(playerId)
                .map(playerMapper::toDomain)
                .map(player -> {
                    player.updateScore(new Score(score));
                    return player;
                })
                .flatMap(player -> {
                    PlayerEntity playerEntity = playerMapper.toEntity(player);
                    playerEntity.setNew(false);
                    return reactivePlayerRepository.save(playerEntity);
                })
                .map(playerMapper::toDomain);
    }

    @Override
    public Mono<Optional<Player>> findPlayerById(PlayerId playerId) {
        return reactivePlayerRepository.findById(playerId)
                .map(playerMapper::toDomain)
                .map(Optional::ofNullable);
    }

    @Override
    public Mono<Optional<Player>> findByUsername(String username) {
        return reactivePlayerRepository.findByUsername(username)
                .map(playerMapper::toDomain)
                .map(Optional::ofNullable);
    }

    @Override
    public Flux<Player> findAll() {
        return reactivePlayerRepository.findAll()
                .map(playerMapper::toDomain);
    }
}
