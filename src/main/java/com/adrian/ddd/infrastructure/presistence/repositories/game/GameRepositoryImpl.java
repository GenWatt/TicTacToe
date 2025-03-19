package com.adrian.ddd.infrastructure.presistence.repositories.game;

import com.adrian.ddd.domain.models.entities.Game;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.domain.repository.IGameRepository;
import com.adrian.ddd.infrastructure.presistence.entities.game.GameEntity;
import com.adrian.ddd.infrastructure.presistence.mappers.game.GameMapperEntityDomainImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional
public class GameRepositoryImpl implements IGameRepository {
    private final ReactiveGameRepository reactiveGameRepository;
    private final GameMapperEntityDomainImpl gameMapper;

    @Autowired
    public GameRepositoryImpl(ReactiveGameRepository reactiveGameRepository, GameMapperEntityDomainImpl gameMapper) {
        this.reactiveGameRepository = reactiveGameRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public Mono<Void> create(Game game) {
        GameEntity gameEntity = gameMapper.toEntity(game);
        return reactiveGameRepository.save(gameEntity).then();
    }

    @Override
    public Mono<Game> findById(GameId id) {
        return reactiveGameRepository.findById(id)
                .map(gameMapper::toDomain);
    }

    @Override
    public Flux<Game> findAll() {
        return reactiveGameRepository.findAll()
                .map(gameMapper::toDomain);
    }

    @Override
    public Mono<Void> delete(GameId id) {
        return reactiveGameRepository.deleteById(id).then();
    }

    @Override
    public Mono<Void> update(Game game) {
        GameEntity gameEntity = gameMapper.toEntity(game);
        gameEntity.markAsNotNew();
        return reactiveGameRepository.save(gameEntity).then();
    }
}
