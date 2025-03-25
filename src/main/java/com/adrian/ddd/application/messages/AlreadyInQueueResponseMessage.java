package com.adrian.ddd.application.messages;

import com.adrian.ddd.domain.MatchmakingTypes;
import lombok.Getter;

@Getter
public class AlreadyInQueueResponseMessage {
    private final MatchmakingTypes type = MatchmakingTypes.ALREADY_IN_QUEUE;
}
