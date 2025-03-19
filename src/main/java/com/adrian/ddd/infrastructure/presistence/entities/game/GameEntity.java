package com.adrian.ddd.infrastructure.presistence.entities.game;

import com.adrian.ddd.domain.models.entities.Game;
import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.Player;
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
    private Player currentPlayer;

    private GameStatus finished;
    private Player winner;
    private Board board;

    @Transient
    private boolean isNew = true;

    public GameEntity(Game game) {
        this.id = game.getId();
        this.currentPlayer = game.getCurrentPlayer();
        this.finished = game.getFinished();
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