package com.adrian.ddd.domain.models.valueObject;

import lombok.Value;
import java.util.Arrays;

@Value
public class Board {
    Player[][] cells;
    int size = 3;

    public Board() {
        this.cells = new Player[size][size];
    }

    public Board(Player[][] cells) {
        this.cells = deepCopy(cells);
    }

    public Player getCell(int x, int y) {
        return cells[x][y];
    }

    public Board setCell(int x, int y, Player player) {
        Player[][] newCells = deepCopy(cells);
        newCells[x][y] = player;

        return new Board(newCells);
    }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= size || y < 0 || y >= size;
    }

    public boolean isCellEmpty(int x, int y) {
        return cells[x][y] == null;
    }

    public boolean isFull() {
        for (Player[] row : cells) {
            for (Player cell : row) {
                if (cell == null) {
                    return false;
                }
            }
        }

        return true;
    }

    private Player[][] deepCopy(Player[][] original) {
        Player[][] copy = new Player[original.length][];

        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }

        return copy;
    }

    public Player isWinner() {
        // Check rows and columns
        for (int i = 0; i < size; i++) {
            if (cells[i][0] != null && cells[i][0] == cells[i][1] && cells[i][1] == cells[i][2]) {
                return cells[i][0]; // Row winner
            }
            if (cells[0][i] != null && cells[0][i] == cells[1][i] && cells[1][i] == cells[2][i]) {
                return cells[0][i]; // Column winner
            }
        }

        // Check diagonals
        if (cells[0][0] != null && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2]) {
            return cells[0][0];
        }
        if (cells[0][2] != null && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0]) {
            return cells[0][2];
        }

        return null; // No winner
    }
}