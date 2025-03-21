package com.adrian.ddd.integrationTests.api.controllers;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.MakeMoveCommand;
import com.adrian.ddd.domain.Result;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class GameControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    private UUID gameId;

    @BeforeEach
    void setUp() {
        ParameterizedTypeReference<Result<GameDto>> typeRef = new ParameterizedTypeReference<>() {};

        Result<GameDto> result = webTestClient.post()
                .uri("/api/v1/games")
                .exchange()
                .expectStatus().isOk()
                .expectBody(typeRef)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();

        gameId = result.getData().orElseThrow(() -> new IllegalStateException("Game data not found")).id();
    }

    @Test
    void testCreateGame() {
        ParameterizedTypeReference<Result<GameDto>> typeRef = new ParameterizedTypeReference<>() {};

        webTestClient.post()
                .uri("/api/v1/games")
                .exchange()
                .expectStatus().isOk()
                .expectBody(typeRef)
                .consumeWith(response -> {
                    Result<GameDto> body = response.getResponseBody();
                    assertThat(body).isNotNull();
                    assertThat(body.isSuccess()).isTrue();
                    assertThat(body.getData().orElseThrow().id()).isNotNull();
                });
    }

    @Test
    void testGetGame() {
        webTestClient.get()
                .uri("/api/v1/games/" + gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDto.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).isNotNull();
                    assertThat(response.getResponseBody().id()).isEqualTo(gameId);
                });
    }

    @Test
    void testMakeMove() {
        webTestClient.put()
                .uri("/api/v1/games/" + gameId + "/start")
                .exchange()
                .expectStatus().isOk();

        MakeMoveCommand moveCommand = new MakeMoveCommand(0, 0);
        moveCommand.setGameId(new GameId(gameId));

        ParameterizedTypeReference<Result<GameDto>> typeRef = new ParameterizedTypeReference<>() {};

        webTestClient.put()
                .uri("/api/v1/games/" + gameId + "/moves")
                .bodyValue(moveCommand)
                .exchange()
                .expectStatus().isOk()
                .expectBody(typeRef)
                .consumeWith(response -> {
                    Result<GameDto> body = response.getResponseBody();
                    assertThat(body).isNotNull();
                    assertThat(body.isSuccess()).isTrue();
                });
    }

    @Test
    void testStartGame() {
        webTestClient.put()
                .uri("/api/v1/games/" + gameId + "/start")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDto.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).isNotNull();
                    assertThat(response.getResponseBody().id()).isEqualTo(gameId);
                });
    }

    @Test
    void testGetNonExistingGame() {
        webTestClient.get()
                .uri("/api/v1/games/" + UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }
}