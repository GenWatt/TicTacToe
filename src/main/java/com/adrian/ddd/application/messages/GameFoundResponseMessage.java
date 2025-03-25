package com.adrian.ddd.application.messages;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.api.dto.PlayerGameDto;
import com.adrian.ddd.domain.MatchmakingTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GameFoundResponseMessage {
    private MatchmakingTypes type = MatchmakingTypes.MATCH_FOUND;
    private PlayerGameDto gameDto;
    private UUID player1Id;
    private UUID player2Id;

    public GameFoundResponseMessage(PlayerGameDto gameDto, UUID player1Id, UUID player2Id) {
        this.gameDto = gameDto;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }
}