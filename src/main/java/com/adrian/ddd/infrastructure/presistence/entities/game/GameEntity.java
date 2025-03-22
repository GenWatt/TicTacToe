package com.adrian.ddd.infrastructure.presistence.entities.game;

import com.adrian.ddd.domain.models.aggregate.game.Game;
import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.domain.Persistable;

@Table(name = "game")
@Data
@NoArgsConstructor
public class GameEntity implements Persistable<GameId>  {
    @Id
    private GameId id;

    @Column("current_player")
    private PlayerType currentPlayerType;

    @Column("game_status")
    private GameStatus gameStatus;

    private PlayerType winner;
    private Board board;

    @Transient
    private boolean isNew = true;

    public GameEntity(Game game) {
        this.id = game.getId();
        this.currentPlayerType = game.getCurrentPlayerType();
        this.gameStatus = game.getGameStatus();
        this.winner = game.getWinner();
        this.board = game.getBoard();
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markAsNotNew() {
        this.isNew = false;
    }
}