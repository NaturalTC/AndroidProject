package com.gamecodeschool.minesweeper;

import java.util.Random;

public class MineSweeperGame {
    private int rows, cols, totalMines, flagsPlaced;
    private MineCell[][] grid;
    private boolean gameOver;

    public MineSweeperGame(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = mines;
        this.flagsPlaced = 0;
        this.grid = new MineCell[rows][cols];

        initializeGrid();
        placeMines();
        calculateNums();
    }

    private void initializeGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new MineCell();
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < totalMines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);

            if (!grid[r][c].hasMine) {
                grid[r][c].hasMine = true;
                placedMines++;
            }
        }
    }

    private void calculateNums() {
        int[] directions = {-1, 0, 1};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].hasMine) continue;
                int count = 0;
                for (int dx : directions) {
                    for (int dy : directions) {
                        int newX = i + dx;
                        int newY = j + dy;

                        if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                            if (grid[newX][newY].hasMine) {
                                count++;
                            }
                        }
                    }
                }
                grid[i][j].surroundingMines = count;
            }
        }
    }

    public void revealCell(int x, int y) {
        if (gameOver || grid[x][y].isRevealed || grid[x][y].isFlagged) return;
        grid[x][y].isRevealed = true;
        if (grid[x][y].hasMine) {
            gameOver = true;
            return;
        }
        if (grid[x][y].surroundingMines == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int newX = x + dx;
                    int newY = y + dy;
                    if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                        if (!grid[newX][newY].isRevealed && !grid[newX][newY].hasMine && !grid[newX][newY].isFlagged) {
                            revealCell(newX, newY);
                        }
                    }
                }
            }
        }
    }

    // Toggle flag on a cell
    public void toggleFlag(int x, int y) {
        if (!grid[x][y].isRevealed) {
            if (grid[x][y].isFlagged) {
                grid[x][y].isFlagged = false;
                flagsPlaced--;
            } else {
                grid[x][y].isFlagged = true;
                flagsPlaced++;
            }
        }
    }
    public int getRemainingFlags() {
        return totalMines - flagsPlaced;
    }
    public MineCell getCell(int x, int y) {
        return grid[x][y];
    }
    public boolean isGameOver() {
        return gameOver;
    }
}