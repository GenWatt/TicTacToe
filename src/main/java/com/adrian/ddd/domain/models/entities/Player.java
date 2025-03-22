package com.adrian.ddd.domain.models.entities;

import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.player.Score;
import lombok.Getter;

@Getter
public class Player {
    private String username;
    private PlayerType playerType;
    private Score score;

    protected Player() { }

    static Player create(String username) {
        Player player = new Player();
        player.username = username;
        player.playerType = null;
        player.score = new Score(0);

        return player;
    }

    public void updatePlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void updateScore(Score score) {
        this.score = score;
    }
}
