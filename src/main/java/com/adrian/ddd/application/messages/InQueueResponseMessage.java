package com.adrian.ddd.application.messages;

import com.adrian.ddd.domain.MatchmakingTypes;
import lombok.Getter;

@Getter
public class InQueueResponseMessage {
    private final MatchmakingTypes type = MatchmakingTypes.IN_QUEUE;
}
