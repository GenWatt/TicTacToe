package com.adrian.ddd.application.messages;

import com.adrian.ddd.domain.SocektTypes;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerMessage {
    private SocektTypes socektType;
    private UUID playerId;
    private String type;
}
