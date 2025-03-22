package com.gamecodeschool.minesweeper;

import android.graphics.Color;
import android.util.Log;

public class MineCell {
    public boolean hasMine;
    public boolean isRevealed;
    public boolean isFlagged;
    public int surroundingMines;
    public boolean isClicked;
    public int row;
    public int col;

    public MineSweeperGame game;

    public MineCell(int row, int col, MineSweeperGame game) {
        this.row = row;
        this.col = col;

        this.hasMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.isClicked = false;

        this.surroundingMines = 0;
        this.game = game;
    }

    public void setClicked() {
        this.isClicked = true;
        if (!this.hasMine && !this.isRevealed) {
            game.revealCell(this.row, this.col);
        }
    }

    public boolean clickable() {
        return !this.isRevealed;
    }

    public int color() {
        if (this.isRevealed && this.hasMine) {
            return Color.RED;
        } else if (this.isRevealed) {
            return Color.LTGRAY;
        } else if (this.isFlagged) {
            return Color.YELLOW;
        }
        return Color.DKGRAY;
    }

    public String text() {
        if (this.isRevealed && this.surroundingMines > 0) {
            return (String.valueOf(this.surroundingMines));
        }
        return "";
    }

    public int textColor() {
        switch (this.surroundingMines) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.RED;
            case 4:
                return Color.rgb(0,0,139);
            case 5:
                return Color.rgb(139, 69, 19);
            case 6:
                return Color.CYAN;
            case 7:
                return Color.BLACK;
            case 8:
                return Color.GRAY;
            default:
                return Color.BLACK;
        }
    }
}