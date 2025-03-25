package com.adrian.ddd.api.controllers;

import com.adrian.ddd.api.dto.PlayerDto;
import com.adrian.ddd.application.command.player.CreatePlayerCommand;
import com.adrian.ddd.application.command.player.UpdatePlayerTypeCommand;
import com.adrian.ddd.application.command.player.UpdateScoreCommand;
import com.adrian.ddd.application.handler.player.*;
import com.adrian.ddd.application.query.player.GetPlayerByIdQuery;
import com.adrian.ddd.application.query.player.GetPlayerByUsernameQuery;
import com.adrian.ddd.domain.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {
    private final CreatePlayerHandler createPlayerHandler;
    private final UpdatePlayerTypeHandler updatePlayerTypeHandler;
    private final GetAllPlayersHandler getAllPlayersHandler;
    private final UpdateScoreHandler updateScoreHandler;
    private final GetPlayerByUsernameHandler getPlayerByUsernameHandler;
    private final GetPlayerByIdHandler getPlayerByIdHandler;

    public PlayerController(
            CreatePlayerHandler createPlayerHandler,
            UpdatePlayerTypeHandler updatePlayerTypeHandler,
            GetAllPlayersHandler getAllPlayersHandler,
            UpdateScoreHandler updateScoreHandler,
            GetPlayerByUsernameHandler getPlayerByUsernameHandler,
            GetPlayerByIdHandler getPlayerByIdHandler) {

        this.createPlayerHandler = createPlayerHandler;
        this.updatePlayerTypeHandler = updatePlayerTypeHandler;
        this.getAllPlayersHandler = getAllPlayersHandler;
        this.updateScoreHandler = updateScoreHandler;
        this.getPlayerByUsernameHandler = getPlayerByUsernameHandler;
        this.getPlayerByIdHandler = getPlayerByIdHandler;
    }

    @GetMapping
    public Mono<ResponseEntity<Result<Iterable<PlayerDto>>>> getAllPlayers() {
        return getAllPlayersHandler.handle()
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
                ));
    }

    @GetMapping("/{playerId}")
    public Mono<ResponseEntity<Result<PlayerDto>>> getPlayer(@PathVariable UUID playerId) {
        return getPlayerByIdHandler.handle(new GetPlayerByIdQuery(playerId))
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
                ));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Result<PlayerDto>>> login(@RequestBody GetPlayerByUsernameQuery query) {
        return getPlayerByUsernameHandler.handle(new GetPlayerByUsernameQuery(query.username()))
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
                ));
    }

    @PostMapping
    public Mono<ResponseEntity<Result<PlayerDto>>> createPlayer(@RequestBody CreatePlayerCommand command) {
        return createPlayerHandler.handle(command)
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
                ));
    }

    @PutMapping("/{playerId}/type")
    public Mono<ResponseEntity<Result<PlayerDto>>> updatePlayerType(
            @PathVariable UUID playerId,
            @RequestBody UpdatePlayerTypeCommand command) {

        command.setPlayerId(playerId);

        return updatePlayerTypeHandler.handle(command)
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
        ));
    }

    @PutMapping("/{playerId}/score")
    public Mono<ResponseEntity<Result<PlayerDto>>> updateScore(
            @PathVariable UUID playerId,
            @RequestParam UpdateScoreCommand command) {

        command.setPlayerId(playerId);

        return updateScoreHandler.handle(command)
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
                ));
    }
}
