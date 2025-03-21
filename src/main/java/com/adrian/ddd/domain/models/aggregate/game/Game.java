package com.adrian.ddd.domain.models.aggregate.game;

import com.adrian.ddd.domain.event.game.GameCreatedEvent;
import com.adrian.ddd.domain.event.game.GameFinishedEvent;
import com.adrian.ddd.domain.event.game.GameStartedEvent;
import com.adrian.ddd.domain.event.game.MakeMoveEvent;
import com.adrian.ddd.domain.models.aggregate.AggregateRoot;
import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.Player;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import io.vavr.control.Either;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Game extends AggregateRoot {
    private GameId id;
    private Board board;
    private Player currentPlayer;
    private Player winner;
    private GameStatus finished;

    private Game() { }

    protected Game(GameId id, Board board, Player currentPlayer, Player winner, GameStatus finished) {
        this.id = id;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.finished = finished;
    }

    public static Either<String, Game> createGame() {
        Game game = new Game();
        // Initialize the game state
        game.id = new GameId(UUID.randomUUID());
        game.board = new Board();
        game.currentPlayer = Player.X;
        game.winner = null;
        game.finished = GameStatus.CREATED;

        game.addDomainEvent(new GameCreatedEvent(game.id));
        return Either.right(game);
    }

    public static Game reconstruct(GameId id, Board board, Player currentPlayer, Player winner, GameStatus finished) {
        return new Game(id, board, currentPlayer, winner, finished);
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

        // Update the board and record the move event
        this.board = board.setCell(x, y, currentPlayer);
        addDomainEvent(new MakeMoveEvent(this.id, this.currentPlayer, x, y));

        checkWinner();

        if (finished != GameStatus.FINISHED) {
            togglePlayer();
        }

        return Either.right(this.board);
    }

    public Either<String, GameStatus> startGame() {
        if (finished != GameStatus.CREATED) {
            return Either.left("Game already started or it's finished");
        }

        this.finished = GameStatus.IN_PROGRESS;
        addDomainEvent(new GameStartedEvent(this.id));
        return Either.right(this.finished);
    }

    private void togglePlayer() {
        currentPlayer = currentPlayer == Player.X ? Player.O : Player.X;
    }

    private void checkWinner() {
        Player winner = board.isWinner();

        if (winner != null) {
            finishGame(winner);
        } else if (board.isFull()) {
            finishGame(null);
        }
    }

    private void finishGame(Player winner) {
        this.winner = winner;
        this.finished = GameStatus.FINISHED;
        addDomainEvent(new GameFinishedEvent(this.id, winner));
    }
}
