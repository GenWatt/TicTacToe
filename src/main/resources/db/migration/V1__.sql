CREATE TABLE game
(
    current_player smallint,
    finished       smallint,
    winner         smallint,
    board          varchar(255),
    id             varchar(36) PRIMARY KEY
)
    GO
