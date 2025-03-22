package com.gamecodeschool.minesweeper;

public class MineCell {
    public boolean hasMine;
    public boolean isRevealed;
    public boolean isFlagged;
    public int surroundingMines;
    public int row;
    public int col;

    public MineCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.hasMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.surroundingMines = 0;
    }
}