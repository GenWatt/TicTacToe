package com.adrian.ddd.application.handler.game;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.game.CreateGameCommand;
import com.adrian.ddd.domain.models.aggregate.game.Game;
import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.infrastructure.presistence.entities.gamePlayerEntity.GamePlayerEntity;
import com.adrian.ddd.infrastructure.presistence.mappers.game.GameMapperImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.game.GameRepositoryImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.gamePlayer.GamePlayerRepositoryImpl;
import com.adrian.ddd.infrastructure.presistence.repositories.player.PlayerRepositoryImpl;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class CreateGameHandler {
    private final GameRepositoryImpl gameRepository;
    private final PlayerRepositoryImpl playerRepository;
    private final GamePlayerRepositoryImpl gamePlayerRepository;
    private final GameMapperImpl mapper;

    public CreateGameHandler(
            GameRepositoryImpl gameRepository,
            PlayerRepositoryImpl playerRepository,
            GamePlayerRepositoryImpl gamePlayerRepository,
            GameMapperImpl mapper) {

        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
        this.gamePlayerRepository = gamePlayerRepository;
    }

    public Mono<Either<String, GameDto>> handle(CreateGameCommand command) {
        // Validate command inputs
        if (command.getPlayer1Id().equals(command.getPlayer2Id())) {
            return Mono.just(Either.left("Players are the same"));
        }

        if (command.getPlayer1Id() == null || command.getPlayer2Id() == null) {
            return Mono.just(Either.left("Player ID is null"));
        }

        System.out.println("Creating game: " + command.getPlayer1Id() + " vs " + command.getPlayer2Id());

        // Get both players reactively
        Mono<Optional<Player>> player1Mono = playerRepository.findPlayerById(new PlayerId(command.getPlayer1Id()));
        Mono<Optional<Player>> player2Mono = playerRepository.findPlayerById(new PlayerId(command.getPlayer2Id()));

        return Mono.zip(player1Mono, player2Mono)
                .flatMap(tuple -> {
                    Optional<Player> player1 = tuple.getT1();
                    Optional<Player> player2 = tuple.getT2();

                    if (player1.isEmpty() || player2.isEmpty()) {
                        return Mono.just(Either.left("Player not found"));
                    }

                    // Create game
                    Either<String, Game> gameEither = Game.createGame(player1.get(), player2.get());

                    if (gameEither.isLeft()) {
                        return Mono.just(Either.left(gameEither.getLeft()));
                    }

                    Game game = gameEither.get();
                    System.out.println("Game created: " + game);

                    // Save game
                    return gameRepository.create(game)
                            .flatMap(savedGame -> {
                                // Create and save GamePlayer relationship
                                GamePlayerEntity gamePlayer = new GamePlayerEntity(
                                        new PlayerId(command.getPlayer1Id()),
                                        new PlayerId(command.getPlayer2Id()),
                                        savedGame.getId()
                                );

                                return gamePlayerRepository.createGamePlayer(gamePlayer)
                                        .thenReturn(savedGame);
                            })
                            .map(mapper::toDto)
                            .map(Either::<String, GameDto>right);
                });
    }
}

