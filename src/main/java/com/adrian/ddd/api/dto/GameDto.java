package com.adrian.ddd.api.dto;

import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;

import java.util.UUID;


public record GameDto(UUID id,
      PlayerType currentPlayerType,
      GameStatus gameStatus,
      PlayerType winner,
      Board board) {
}
