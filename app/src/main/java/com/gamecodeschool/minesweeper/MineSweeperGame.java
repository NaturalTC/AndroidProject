package com.gamecodeschool.minesweeper;

import java.util.Random;

public class MineSweeperGame {
    private int totalRows, totalCols, totalMines, flagsPlaced;
    private MineCell[][] grid;
    private boolean gameOver;

    private int clickCount = 0;        // Track clicks
    private long startTime = System.currentTimeMillis();  // Track game start time

    public MineSweeperGame(int rows, int cols, int mines) {
        this.totalRows = rows;
        this.totalCols = cols;
        this.totalMines = mines;
        this.flagsPlaced = 0;
        this.grid = new MineCell[rows][cols];

        initializeGrid();
        placeMines();
        calculateNums();
    }

    private void initializeGrid() {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                grid[i][j] = new MineCell(i, j, this);
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < totalMines) {
            int r = random.nextInt(totalRows);
            int c = random.nextInt(totalCols);

            if (!grid[r][c].hasMine) {
                grid[r][c].hasMine = true;
                placedMines++;
            }
        }
    }

    private void calculateNums() {
        int[] directions = {-1, 0, 1};

        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                if (grid[i][j].hasMine) continue;
                int count = 0;
                for (int dx : directions) {
                    for (int dy : directions) {
                        int newX = i + dx;
                        int newY = j + dy;

                        if (newX >= 0 && newX < totalRows && newY >= 0 && newY < totalCols) {
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

    public boolean isWin() {
        for (int rowI = 0; rowI < totalRows; rowI++){
            for (int colI = 0; colI < totalCols; colI++) {
                MineCell cell = this.getCell(rowI, colI);
                if (!cell.isFinished()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void revealCell(int x, int y) {
        if (gameOver || grid[x][y].isRevealed || grid[x][y].isFlagged) return;

        grid[x][y].isRevealed = true;
        clickCount++;  // Increment click count

        if (grid[x][y].hasMine) {
            gameOver = true;
            return;
        }

        if (grid[x][y].surroundingMines == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int newX = x + dx;
                    int newY = y + dy;
                    if (newX >= 0 && newX < totalRows && newY >= 0 && newY < totalCols) {
                        if (!grid[newX][newY].isRevealed && !grid[newX][newY].hasMine && !grid[newX][newY].isFlagged) {
                            revealCell(newX, newY);
                        }
                    }
                }
            }
        }
    }

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

    public MineCell getCell(int row, int col) {
        return grid[row][col];
    }


    public int getElapsedTime() {
        long now = System.currentTimeMillis();
        return (int) ((now - startTime) / 1000); // return in seconds
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getMinesCount() {
        return totalMines;
    }

    public String getFieldRepresentation() {
        return totalRows + "x" + totalCols;
    }
}
