CREATE TABLE game_player(
    id uuid PRIMARY KEY,
    game_id uuid NOT NULL,
    player1_id uuid NOT NULL,
    player2_id uuid NOT NULL,

    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (player1_id) REFERENCES player(id),
    FOREIGN KEY (player2_id) REFERENCES player(id)
)