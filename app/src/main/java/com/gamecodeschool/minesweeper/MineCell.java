package com.gamecodeschool.minesweeper;

import android.graphics.Color;
import android.util.Log;


enum CellStatus {
    RevealedMine, RevealedNotMine, Flagged, Neutral
}

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

    public boolean isFinished() {
        return !(this.hasMine && !this.isFlagged) && !(!this.hasMine && !this.isRevealed);
    }
    public CellStatus status() {
        if (this.isRevealed && this.hasMine) {
            return CellStatus.RevealedMine;
        } else if (this.isRevealed) {
            return CellStatus.RevealedNotMine;
        } else if (this.isFlagged) {
            return CellStatus.Flagged;
        }
        return CellStatus.Neutral;
    }
}