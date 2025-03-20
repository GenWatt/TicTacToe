package com.adrian.ddd.domain.models.entities;

import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.Player;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    void createGame() {
        // Arrange
        Game game = new Game();

        // Act

        // Assert
        assert game.getFinished() == GameStatus.CREATED;
        assert game.getBoard().getSize() == 3;

        for (int i = 0; i < game.getBoard().getSize(); i++) {
            for (int j = 0; j < game.getBoard().getSize(); j++) {
                assert game.getBoard().getCell(i, j) == null;
            }
        }
    }

    @Test
    void startGame() {
        // Arrange
        Game game = new Game();

        // Act
        Either<String, GameStatus> result = game.startGame();

        // Assert
        assert game.getFinished() == GameStatus.IN_PROGRESS;
        assert result.isRight();
    }

    @Test
    void makeMove() {
        // Arrange
        Game game = new Game();
        Player player = game.getCurrentPlayer();
        int moveX = 0, moveY = 0;

        // Act
        game.startGame();
        Either<String, Board> result = game.makeMove(moveX, moveY);

        // Assert
        assert game.getBoard().getCell(moveX, moveY) == player;
        assert result.isRight();
    }

    @Test
    void makeIllegalMove() {
        // Arrange
        Game game = new Game();
        int size = game.getBoard().getSize();

        // Act
        game.startGame();
        Either<String, Board> result = game.makeMove(size + 1, size + 1);

        // Assert
        assert result.isLeft();
    }
}
