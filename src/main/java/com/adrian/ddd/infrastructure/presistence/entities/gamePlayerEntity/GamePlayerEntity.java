package com.adrian.ddd.infrastructure.presistence.entities.gamePlayerEntity;

import com.adrian.ddd.domain.models.valueObject.game.GameId;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("game_player")
@Data
@NoArgsConstructor
public class GamePlayerEntity implements Persistable<UUID> {
    @Id
    private UUID id;

    @Column("player1_id")
    private PlayerId player1Id;

    @Column("player2_id")
    private PlayerId player2Id;

    @Column("game_id")
    private GameId gameId;

    public GamePlayerEntity(PlayerId player1Id, PlayerId player2Id, GameId gameId) {
        id = UUID.randomUUID();
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.gameId = gameId;
    }

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
