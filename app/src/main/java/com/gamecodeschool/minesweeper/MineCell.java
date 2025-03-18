package com.gamecodeschool.minesweeper;

public class MineCell {
    public boolean hasMine;
    public boolean isRevealed;
    public boolean isFlagged;
    public int surroundingMines;

    public MineCell() {
        this.hasMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.surroundingMines = 0;
    }
}
