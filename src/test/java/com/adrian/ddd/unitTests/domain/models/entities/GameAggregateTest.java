package com.adrian.ddd.unitTests.domain.models.entities;

import com.adrian.ddd.domain.models.aggregate.game.Game;
import com.adrian.ddd.domain.models.entities.Player;
import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameAggregateTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        // Create players before each test
        player1 = Player.create("Player1").get();
        player2 = Player.create("Player2").get();
    }

    @Test
    void givenNewGame_whenCreated_thenStatusIsCreatedAndBoardIsEmpty() {
        // Given & When
        Game game = Game.createGame(player1, player2).get();

        // Then
        assertEquals(GameStatus.CREATED, game.getGameStatus(), "New game should be in CREATED state");
        Board board = game.getBoard();
        assertNotNull(board, "Board should not be null");
        assertEquals(3, board.getSize(), "Board size should be 3");

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                assertNull(board.getCell(i, j), "Cell [" + i + "][" + j + "] should be empty");
            }
        }
    }

    @Test
    void givenCreatedGame_whenStartGame_thenStatusIsInProgress() {
        // Given
        Game game = Game.createGame(player1, player2).get();

        // When
        Either<String, GameStatus> result = game.startGame();

        // Then
        assertTrue(result.isRight(), "startGame should succeed");
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus(), "Game should be in IN_PROGRESS state after starting");
    }

    @Test
    void givenStartedGame_whenMakeValidMove_thenBoardIsUpdatedAndPlayerIsToggled() {
        // Given
        Game game = Game.createGame(player1, player2).get();
        game.startGame();
        PlayerType initialPlayerType = game.getCurrentPlayerType();

        // When
        Either<String, Board> result = game.makeMove(0, 0);

        // Then
        assertTrue(result.isRight(), "Move should succeed");
        Board board = result.get();
        assertEquals(initialPlayerType, board.getCell(0, 0), "The cell (0,0) should be occupied by the initial player");
        assertNotEquals(initialPlayerType, game.getCurrentPlayerType(), "Current player should be toggled after a valid move");
    }

    @Test
    void givenStartedGame_whenMoveOutOfBounds_thenReturnError() {
        // Given
        Game game = Game.createGame(player1, player2).get();
        game.startGame();
        int size = game.getBoard().getSize();

        // When
        Either<String, Board> result = game.makeMove(size + 1, size + 1);

        // Then
        assertTrue(result.isLeft(), "Move out of bounds should fail");
        assertEquals("Coordinates are out of bounds", result.getLeft(), "Error message should indicate out-of-bounds coordinates");
    }

    @Test
    void givenStartedGame_whenMoveOnNonEmptyCell_thenReturnError() {
        // Given
        Game game = Game.createGame(player1, player2).get();
        game.startGame();
        // First move at (0,0)
        Either<String, Board> firstMove = game.makeMove(0, 0);
        assertTrue(firstMove.isRight(), "First move should succeed");

        // When: second move on the same cell
        Either<String, Board> secondMove = game.makeMove(0, 0);

        // Then
        assertTrue(secondMove.isLeft(), "Moving on a non-empty cell should fail");
        assertEquals("Cell is not empty", secondMove.getLeft(), "Error message should indicate cell is not empty");
    }

    @Test
    void givenWinningMove_whenPlayed_thenGameIsFinishedAndWinnerIsSet() {
        // Given: Simulate a win on the top row for Player.X
        Game game = Game.createGame(player1, player2).get();
        game.startGame();

        // In the default turn order, Player.X moves first
        // Moves sequence: X: (0,0), O: (1,0), X: (0,1), O: (1,1), X: (0,2) wins.
        Either<String, Board> move1 = game.makeMove(0, 0); // X
        assertTrue(move1.isRight(), "Move 1 should succeed");

        Either<String, Board> move2 = game.makeMove(1, 0); // O
        assertTrue(move2.isRight(), "Move 2 should succeed");

        Either<String, Board> move3 = game.makeMove(0, 1); // X
        assertTrue(move3.isRight(), "Move 3 should succeed");

        Either<String, Board> move4 = game.makeMove(1, 1); // O
        assertTrue(move4.isRight(), "Move 4 should succeed");

        // When: winning move by Player.X
        Either<String, Board> move5 = game.makeMove(0, 2); // X wins
        assertTrue(move5.isRight(), "Winning move should succeed");

        // Then
        assertEquals(GameStatus.FINISHED, game.getGameStatus(), "Game should be finished after a winning move");
        assertEquals(PlayerType.X, game.getWinner(), "Player.X should be declared the winner");
    }
}