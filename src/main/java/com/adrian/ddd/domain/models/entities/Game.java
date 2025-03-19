package com.adrian.ddd.domain.models.entities;

import com.adrian.ddd.domain.event.DomainEvent;
import com.adrian.ddd.domain.event.game.GameCreatedEvent;
import com.adrian.ddd.domain.event.game.GameFinishedEvent;
import com.adrian.ddd.domain.event.game.GameStartedEvent;
import com.adrian.ddd.domain.event.game.MakeMoveEvent;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import com.adrian.ddd.domain.models.valueObject.Player;
import com.adrian.ddd.domain.models.valueObject.Board;

import io.vavr.control.Either;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Game {
    private GameId id;
    private Board board;
    private Player currentPlayer;
    private Player winner;
    private GameStatus finished;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Game() {
        this.id = new GameId(UUID.randomUUID());
        this.board = new Board();
        this.currentPlayer = Player.X;
        this.winner = null;
        this.finished = GameStatus.CREATED;

        domainEvents.add(new GameCreatedEvent(this.id));
    }

    public Either<String, Board> makeMove(int x, int y) {
        if (finished == GameStatus.FINISHED) {
            return Either.left("Game is finished");
        }

        if (finished == GameStatus.CREATED) {
            return Either.left("Game is not started");
        }

        if (board.isOutOfBounds(x, y)) {
            return Either.left("Coordinates are out of bounds");
        }

        if (!board.isCellEmpty(x, y)) {
            return Either.left("Cell is not empty");
        }

        this.board = board.setCell(x, y, currentPlayer); // Update the board
        domainEvents.add(new MakeMoveEvent(this.id, this.currentPlayer, x, y));

        checkWinner();
        togglePlayer();

        return Either.right(this.board);
    }

    public void startGame() {
        this.finished = GameStatus.IN_PROGRESS;
        domainEvents.add(new GameStartedEvent(this.id));
    }

    public void togglePlayer() {
        currentPlayer = currentPlayer == Player.X ? Player.O : Player.X;
    }

    public void checkWinner() {
        Player[][] cells = board.getCells();

        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (cells[i][0] != null && cells[i][0] == cells[i][1] && cells[i][1] == cells[i][2]) {
                finishGame(cells[i][0]);
                return;
            }

            // Check columns
            if (cells[0][i] != null && cells[0][i] == cells[1][i] && cells[1][i] == cells[2][i]) {
                finishGame(cells[0][i]);
                return;
            }
        }

        // Check diagonals
        if (cells[0][0] != null && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2]) {
            finishGame(cells[0][0]);
            return;
        }
        if (cells[0][2] != null && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0]) {
            finishGame(cells[0][2]);
            return;
        }

        // Check draw
        if (board.isFull()) {
            finishGame(null); // Null winner for draw
        }
    }

    private void finishGame(Player winner) {
        this.winner = winner;
        this.finished = GameStatus.FINISHED;
        this.domainEvents.add(new GameFinishedEvent(this.id, winner));
    }
}
