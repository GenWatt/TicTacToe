package com.adrian.ddd.integrationTests.api.controllers;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.MakeMoveCommand;
import com.adrian.ddd.domain.Result;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTests extends PostgresTestContainerConfig {

    @Autowired
    private WebTestClient webTestClient;

    private static final ParameterizedTypeReference<Result<GameDto>> GAME_DTO_RESPONSE =
            new ParameterizedTypeReference<>() {};

    private UUID gameId;

    @BeforeEach
    void setup() {
        gameId = createGame();
    }

    private UUID createGame() {
        Result<GameDto> result = webTestClient.post()
                .uri("/api/v1/games")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GAME_DTO_RESPONSE)
                .returnResult()
                .getResponseBody();

        return result.getData()
                .orElseThrow(() -> new IllegalStateException("Game data not found"))
                .id();
    }

    private Result<GameDto> startGame(UUID gameId) {
        return webTestClient.put()
                .uri("/api/v1/games/" + gameId + "/start")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GAME_DTO_RESPONSE)
                .returnResult()
                .getResponseBody();
    }

    private Result<GameDto> makeMove(UUID gameId, int x, int y) {
        MakeMoveCommand moveCommand = new MakeMoveCommand(x, y);
        moveCommand.setGameId(new GameId(gameId));

        return webTestClient.put()
                .uri("/api/v1/games/" + gameId + "/moves")
                .bodyValue(moveCommand)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GAME_DTO_RESPONSE)
                .returnResult()
                .getResponseBody();
    }

    @Nested
    class GameCreationTests {
        @Test
        void shouldCreateNewGameSuccessfully() {
            UUID newGameId = createGame();
            assertThat(newGameId).as("Created game ID should not be null").isNotNull();
        }
    }

    @Nested
    class GameRetrievalTests {
        @Test
        void shouldReturnGameDetailsWhenGameIdIsValid() {
            webTestClient.get()
                    .uri("/api/v1/games/" + gameId)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GAME_DTO_RESPONSE)
                    .consumeWith(response -> {
                        Result<GameDto> body = response.getResponseBody();

                        assertThat(body)
                                .as("Get game response should not be null")
                                .isNotNull();
                        assertThat(body.isSuccess())
                                .as("Get game should succeed")
                                .isTrue();
                        assertThat(body.getData().orElseThrow().id())
                                .as("Returned game ID should match")
                                .isEqualTo(gameId);
                    });
        }

        @Test
        void shouldReturnNotFoundWhenGameIdDoesNotExist() {
            webTestClient.get()
                    .uri("/api/v1/games/" + UUID.randomUUID())
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    class GamePlayTests {
        @Test
        void shouldStartGameSuccessfully() {
           Result<GameDto> response = startGame(gameId);

            assertThat(response)
                    .as("Start game response should not be null")
                    .isNotNull();
            assertThat(response.isSuccess())
                    .as("Start game should succeed")
                    .isTrue();
            assertThat(response.getData().orElseThrow().gameStatus())
                    .as("Game status should be started")
                    .isEqualTo(GameStatus.IN_PROGRESS);
        }

        @Test
        void shouldMakeMoveSuccessfullyWhenGameIsStarted() {
            startGame(gameId);
            Result<GameDto> response = makeMove(gameId, 0, 0);

            assertThat(response)
                    .as("Make move response should not be null")
                    .isNotNull();
            assertThat(response.isSuccess())
                    .as("Make move should succeed")
                    .isTrue();
            assertThat(response.getData().orElseThrow().board().getCell(0, 0))
                    .as("Move should be recorded on the board")
                    .isNotNull();
        }
    }
}
