//package com.adrian.ddd.integrationTests.api.controllers;
//
//import com.adrian.ddd.api.dto.GameDto;
//import com.adrian.ddd.api.dto.PlayerDto;
//import com.adrian.ddd.application.command.game.MakeMoveCommand;
//import com.adrian.ddd.domain.Result;
//import com.adrian.ddd.domain.models.valueObject.game.GameId;
//import com.adrian.ddd.domain.models.valueObject.game.GameStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class GameControllerTests extends PostgresTestContainerConfig {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    private static final ParameterizedTypeReference<Result<GameDto>> GAME_DTO_RESPONSE =
//            new ParameterizedTypeReference<>() {};
//
//    private static final ParameterizedTypeReference<Result<PlayerDto>> PLAYER_DTO_RESPONSE =
//            new ParameterizedTypeReference<>() {};
//
//    private UUID player1Id;
//    private UUID player2Id;
//    private UUID gameId;
//
//    @BeforeEach
//    void setup() {
//        player1Id = createPlayer("Player1");
//        player2Id = createPlayer("Player2");
//        gameId = createGame(player1Id, player2Id);
//    }
//
//    private UUID createPlayer(String username) {
//        Result<PlayerDto> result = webTestClient.post()
//                .uri("/api/v1/players")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue("{\"username\": \"" + username + "\"}")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(PLAYER_DTO_RESPONSE)
//                .returnResult()
//                .getResponseBody();
//
//        return result.getData()
//                .orElseThrow(() -> new IllegalStateException("Player data not found"))
//                .id();
//    }
//
//    private UUID createGame(UUID player1Id, UUID player2Id) {
//        Result<GameDto> result = webTestClient.post()
//                .uri("/api/v1/games")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue("{\"player1Id\": \"" + player1Id + "\", \"player2Id\": \"" + player2Id + "\"}")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GAME_DTO_RESPONSE)
//                .returnResult()
//                .getResponseBody();
//
//        return result.getData()
//                .orElseThrow(() -> new IllegalStateException("Game data not found"))
//                .id();
//    }
//
//    private Result<GameDto> startGame(UUID gameId) {
//        return webTestClient.put()
//                .uri("/api/v1/games/" + gameId + "/start")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GAME_DTO_RESPONSE)
//                .returnResult()
//                .getResponseBody();
//    }
//
//    private Result<GameDto> makeMove(UUID gameId, int x, int y) {
//        MakeMoveCommand moveCommand = new MakeMoveCommand(x, y);
//        moveCommand.setGameId(new GameId(gameId));
//
//        return webTestClient.put()
//                .uri("/api/v1/games/" + gameId + "/moves")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(moveCommand)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GAME_DTO_RESPONSE)
//                .returnResult()
//                .getResponseBody();
//    }
//
//    @Nested
//    class GameCreationTests {
//        @Test
//        void shouldCreateNewGameSuccessfully() {
//            assertThat(gameId).as("Created game ID should not be null").isNotNull();
//        }
//    }
//
//    @Nested
//    class GameRetrievalTests {
//        @Test
//        void shouldReturnGameDetailsWhenGameIdIsValid() {
//            webTestClient.get()
//                    .uri("/api/v1/games/" + gameId)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectBody(GAME_DTO_RESPONSE)
//                    .consumeWith(response -> {
//                        Result<GameDto> body = response.getResponseBody();
//
//                        assertThat(body)
//                                .as("Get game response should not be null")
//                                .isNotNull();
//                        assertThat(body.isSuccess())
//                                .as("Get game should succeed")
//                                .isTrue();
//                        assertThat(body.getData().orElseThrow().id())
//                                .as("Returned game ID should match")
//                                .isEqualTo(gameId);
//                    });
//        }
//
//        @Test
//        void shouldReturnNotFoundWhenGameIdDoesNotExist() {
//            webTestClient.get()
//                    .uri("/api/v1/games/" + UUID.randomUUID())
//                    .exchange()
//                    .expectStatus().isNotFound();
//        }
//    }
//
//    @Nested
//    class GamePlayTests {
//        @Test
//        void shouldStartGameSuccessfully() {
//            Result<GameDto> response = startGame(gameId);
//
//            assertThat(response)
//                    .as("Start game response should not be null")
//                    .isNotNull();
//            assertThat(response.isSuccess())
//                    .as("Start game should succeed")
//                    .isTrue();
//            assertThat(response.getData().orElseThrow().gameStatus())
//                    .as("Game status should be started")
//                    .isEqualTo(GameStatus.IN_PROGRESS);
//        }
//
//        @Test
//        void shouldMakeMoveSuccessfullyWhenGameIsStarted() {
//            startGame(gameId);
//            Result<GameDto> response = makeMove(gameId, 0, 0);
//
//            assertThat(response)
//                    .as("Make move response should not be null")
//                    .isNotNull();
//            assertThat(response.isSuccess())
//                    .as("Make move should succeed")
//                    .isTrue();
//            assertThat(response.getData().orElseThrow().board().getCell(0, 0))
//                    .as("Move should be recorded on the board")
//                    .isNotNull();
//        }
//    }
//}