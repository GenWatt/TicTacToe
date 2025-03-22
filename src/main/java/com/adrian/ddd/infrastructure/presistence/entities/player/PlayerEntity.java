package com.adrian.ddd.infrastructure.presistence.entities.player;

import com.adrian.ddd.domain.models.valueObject.PlayerType;
import com.adrian.ddd.domain.models.valueObject.player.PlayerId;
import com.adrian.ddd.domain.models.valueObject.player.Score;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("player")
@NoArgsConstructor
@Data
public class PlayerEntity {
    @Id
    private PlayerId id;
    private String username;

    @Column("player_type")
    private PlayerType playerType;
    private Score score;
}
