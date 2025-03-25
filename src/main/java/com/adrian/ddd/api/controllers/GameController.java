package com.adrian.ddd.api.controllers;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.application.command.game.CreateGameCommand;
import com.adrian.ddd.application.command.game.MakeMoveCommand;
import com.adrian.ddd.application.command.game.StartGameCommand;
import com.adrian.ddd.application.handler.game.CreateGameHandler;
import com.adrian.ddd.application.handler.game.GetGameByIdHandler;
import com.adrian.ddd.application.handler.game.MakeMoveHandler;
import com.adrian.ddd.application.handler.game.StartGameHandler;
import com.adrian.ddd.application.query.game.GetGameByIdQuery;
import com.adrian.ddd.domain.Result;
import com.adrian.ddd.domain.models.valueObject.game.GameId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {
    private final MakeMoveHandler makeMoveHandler;
    private final GetGameByIdHandler getGameByIdHandler;
    private final CreateGameHandler createGameHandler;
    private final StartGameHandler startGameHandler;

    @Autowired
    public GameController(
            MakeMoveHandler makeMoveHandler,
            GetGameByIdHandler getGameByIdHandler,
            CreateGameHandler createGameHandler,
            StartGameHandler startGameHandler) {

        this.getGameByIdHandler = getGameByIdHandler;
        this.makeMoveHandler = makeMoveHandler;
        this.createGameHandler = createGameHandler;
        this.startGameHandler = startGameHandler;
    }

    @PostMapping
    public Mono<ResponseEntity<Result<GameDto>>> createGame() {
        return createGameHandler.handle(new CreateGameCommand())
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
                ));
    }

    @GetMapping("/{gameId}")
    public Mono<ResponseEntity<Result<GameDto>>> getGame(@PathVariable String gameId) {
        return getGameByIdHandler.handle(new GetGameByIdQuery(new GameId(UUID.fromString(gameId))))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{gameId}/moves")
    public Mono<ResponseEntity<Result<GameDto>>> makeMove(@PathVariable String gameId, @RequestBody MakeMoveCommand makeMoveCommand) {
        makeMoveCommand.setGameId(new GameId(UUID.fromString(gameId)));

        return makeMoveHandler.handle(makeMoveCommand)
                .map(either -> either.fold(
                        error -> ResponseEntity.badRequest().body(Result.failure(error)),
                        success -> ResponseEntity.ok(Result.success(success))
                ));
    }

    @PutMapping("/{gameId}/start")
    public Mono<ResponseEntity<Result<GameDto>>> startGame(@PathVariable String gameId) {
        return startGameHandler.handle(new StartGameCommand(new GameId(UUID.fromString(gameId))))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
