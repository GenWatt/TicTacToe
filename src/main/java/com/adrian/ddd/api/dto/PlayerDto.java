package com.adrian.ddd.api.dto;

import com.adrian.ddd.domain.models.valueObject.PlayerType;

import java.util.UUID;

public record PlayerDto(
        UUID id,
        String username,
        PlayerType playerType,
        int score
) {
}
