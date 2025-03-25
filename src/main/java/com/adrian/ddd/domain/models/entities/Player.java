package com.adrian.ddd.domain.models.entities;

import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.domain.models.valueObject.player.Score;
import io.vavr.control.Either;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Player {
    private PlayerId playerId;
    private String username;
    private PlayerType playerType;
    private Score score;

    protected Player() { }

    public static Either<String, Player> create(String username) {
        if (username == null || username.isEmpty()) return Either.left("Username cannot be empty");

        if (username.length() < 3) return Either.left("Username must be at least 3 characters long");

        if (username.length() > 20) return Either.left("Username must be at most 20 characters long");

        Player player = new Player();
        player.playerId = new PlayerId(UUID.randomUUID());
        player.username = username;
        player.playerType = null;
        player.score = new Score(0);

        return Either.right(player);
    }

    public static Player reconstruct(PlayerId playerId, String username, PlayerType playerType, Score score) {
        Player player = new Player();
        player.playerId = playerId;
        player.username = username;
        player.playerType = playerType;
        player.score = score;

        return player;
    }

    public void updatePlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void updateScore(Score score) {
        this.score = score;
    }
}
