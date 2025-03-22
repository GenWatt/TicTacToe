CREATE TABLE game (
    id uuid PRIMARY KEY,
    current_player SMALLINT NOT NULL,
    game_status SMALLINT NOT NULL,
    winner SMALLINT NULL,
    board VARCHAR(255) NOT NULL
)
