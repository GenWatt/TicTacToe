CREATE TABLE player (
    id uuid PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    player_type SMALLINT NULL,
    score INT NOT NULL DEFAULT 0
)
