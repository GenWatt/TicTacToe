package com.adrian.ddd.domain.models.aggregate.game;

import com.adrian.ddd.domain.event.game.GameCreatedEvent;
import com.adrian.ddd.domain.event.game.GameFinishedEvent;
import com.adrian.ddd.domain.event.game.GameStartedEvent;
import com.adrian.ddd.domain.event.game.MakeMoveEvent;
import com.adrian.ddd.domain.models.aggregate.AggregateRoot;
import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import io.vavr.control.Either;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Game extends AggregateRoot {
    private GameId id;
    private Board board;
    private PlayerType currentPlayerType;
    private PlayerType winner;
    private GameStatus gameStatus;

    private Player player1;
    private Player player2;

    private Game() { }

    protected Game(GameId id, Board board, PlayerType currentPlayerType, PlayerType winner, GameStatus gameStatus) {
        this.id = id;
        this.board = board;
        this.currentPlayerType = currentPlayerType;
        this.winner = winner;
        this.gameStatus = gameStatus;
    }

    public static Either<String, Game> createGame(Player player1, Player player2) {
        Game game = new Game();

        // Initialize the game state
        game.player1 = player1;
        game.player2 = player2;
        game.id = new GameId(UUID.randomUUID());
        game.board = new Board();
        game.currentPlayerType = PlayerType.X;
        game.winner = null;
        game.gameStatus = GameStatus.CREATED;

        game.addDomainEvent(new GameCreatedEvent(game.id));
        return Either.right(game);
    }

    public static Game reconstruct(GameId id, Board board, PlayerType currentPlayerType, PlayerType winner, GameStatus gameStatus) {
        return new Game(id, board, currentPlayerType, winner, gameStatus);
    }

    public Either<String, Board> makeMove(int x, int y) {
        if (gameStatus == GameStatus.FINISHED) {
            return Either.left("Game is finished");
        }

        if (gameStatus == GameStatus.CREATED) {
            return Either.left("Game is not started");
        }

        if (board.isOutOfBounds(x, y)) {
            return Either.left("Coordinates are out of bounds");
        }

        if (!board.isCellEmpty(x, y)) {
            return Either.left("Cell is not empty");
        }

        // Update the board and record the move event
        this.board = board.setCell(x, y, currentPlayerType);
        addDomainEvent(new MakeMoveEvent(this.id, this.currentPlayerType, x, y));

        checkWinner();

        if (gameStatus != GameStatus.FINISHED) {
            togglePlayer();
        }

        return Either.right(this.board);
    }

    public Either<String, GameStatus> startGame() {
        if (gameStatus != GameStatus.CREATED) {
            return Either.left("Game already started or it's finished");
        }

        this.gameStatus = GameStatus.IN_PROGRESS;
        addDomainEvent(new GameStartedEvent(this.id));
        return Either.right(this.gameStatus);
    }

    private void togglePlayer() {
        currentPlayerType = currentPlayerType == PlayerType.X ? PlayerType.O : PlayerType.X;
    }

    private void checkWinner() {
        PlayerType winner = board.isWinner();

        if (winner != null) {
            finishGame(winner);
        } else if (board.isFull()) {
            finishGame(null);
        }
    }

    private void finishGame(PlayerType winner) {
        this.winner = winner;
        this.gameStatus = GameStatus.FINISHED;
        addDomainEvent(new GameFinishedEvent(this.id, winner));
    }
}
