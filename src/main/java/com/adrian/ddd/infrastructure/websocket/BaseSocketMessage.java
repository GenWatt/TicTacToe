package com.adrian.ddd.infrastructure.websocket;

import com.adrian.ddd.domain.SocektTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JoinMatchmakingMessage.class, name = "JOIN_MATCHMAKING"),
        @JsonSubTypes.Type(value = MakeMoveMessage.class, name = "MAKE_MOVE")
})
@Getter
@Setter
public abstract class BaseSocketMessage {
    private SocektTypes type;
}