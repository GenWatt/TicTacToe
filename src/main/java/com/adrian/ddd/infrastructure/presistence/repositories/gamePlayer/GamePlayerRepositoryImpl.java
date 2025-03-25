package com.adrian.ddd.infrastructure.presistence.repositories.gamePlayer;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.api.dto.PlayerDto;
import com.adrian.ddd.api.dto.PlayerGameDto;
import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.infrastructure.presistence.entities.gamePlayerEntity.GamePlayerEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class GamePlayerRepositoryImpl {
    private final ReactiveGamePlayerRepository gamePlayerRepository;
    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GamePlayerRepositoryImpl(ReactiveGamePlayerRepository gamePlayerRepository, DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
        this.gamePlayerRepository = gamePlayerRepository;
    }

    public Mono<GamePlayerEntity> createGamePlayer(GamePlayerEntity gamePlayerEntity) {
        return gamePlayerRepository.save(gamePlayerEntity);
    }

    public Mono<PlayerGameDto> findInProgressGameByPlayerId(PlayerId playerId) {
        String sql = """
        SELECT 
          g.id as game_id, 
          g.current_player as game_current_player, 
          g.game_status as game_status, 
          g.winner as game_winner, 
          g.board as game_board,
          p1.id as player1_id, 
          p1.username as player1_username, 
          p1.player_type as player1_type,
          p1.score as player1_score,
          p2.id as player2_id, 
          p2.username as player2_username, 
          p2.player_type as player2_type,
          p2.score as player2_score      
        FROM game_player gp
        JOIN game g ON gp.game_id = g.id
        JOIN player p1 ON gp.player1_id = p1.id
        JOIN player p2 ON gp.player2_id = p2.id
        WHERE (gp.player1_id = :playerId OR gp.player2_id = :playerId) AND g.game_status = 0
        LIMIT 1
    """;

        return databaseClient.sql(sql)
                .bind("playerId", playerId.id())
                .map(row -> {

                    PlayerType[][] cells = null;
                    try {
                        cells = objectMapper.readValue(row.get("game_board", String.class), PlayerType[][].class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    Board board = new Board(cells);

                    GameDto gameDto = new GameDto(
                            row.get("game_id", UUID.class),
                            PlayerType.fromDatabase(row.get("game_current_player", Integer.class)),
                            GameStatus.values()[row.get("game_status", Integer.class)],
                            PlayerType.fromDatabase(row.get("game_winner", Integer.class)),
                            board
                    );

                    PlayerDto player1Dto = new PlayerDto(
                            row.get("player1_id", UUID.class),
                            row.get("player1_username", String.class),
                            PlayerType.fromDatabase(row.get("player1_type", Integer.class)),
                            row.get("player1_score", Integer.class)
                    );

                    PlayerDto player2Dto = new PlayerDto(
                            row.get("player2_id", UUID.class),
                            row.get("player2_username", String.class),
                            PlayerType.fromDatabase(row.get("player2_type", Integer.class)),
                            row.get("player2_score", Integer.class)
                    );

                    return new PlayerGameDto(gameDto, player1Dto, player2Dto);
                })
                .first();
    }
}
