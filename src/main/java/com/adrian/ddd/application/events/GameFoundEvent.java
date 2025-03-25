package com.adrian.ddd.application.events;

import com.adrian.ddd.api.dto.GameDto;
import com.adrian.ddd.api.dto.PlayerGameDto;
import com.adrian.ddd.domain.MatchmakingTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GameFoundEvent {
    private final PlayerGameDto playerGameDto;
    private final UUID player1Id;
    private final UUID player2Id;
}