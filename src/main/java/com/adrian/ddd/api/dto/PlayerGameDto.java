package com.adrian.ddd.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerGameDto {
    private GameDto gameDto;
    private PlayerDto player1Dto;
    private PlayerDto player2Dto;
}
