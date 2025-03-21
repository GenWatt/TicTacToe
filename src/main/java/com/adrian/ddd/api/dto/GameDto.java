package com.adrian.ddd.api.dto;

import com.adrian.ddd.domain.models.valueObject.Board;
import com.adrian.ddd.domain.models.valueObject.Player;
import com.adrian.ddd.domain.models.valueObject.game.GameStatus;

import java.util.UUID;


public record GameDto(UUID id,
      Player currentPlayer,
      GameStatus finished,
      Player winner,
      Board board) {
}
