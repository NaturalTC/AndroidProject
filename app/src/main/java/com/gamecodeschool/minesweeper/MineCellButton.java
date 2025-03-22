package com.gamecodeschool.minesweeper;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.widget.AppCompatButton;
import java.util.Map;

public class MineCellButton extends AppCompatButton {

    public MineCell cell;
    public MineCellButton(Context context, MineCell inboundCell) {
            super(context);
            init();
            this.cell = inboundCell;
        }
        private void init() {
            setBackgroundColor(Color.LTGRAY);
            setTextAppearance(android.R.style.TextAppearance_Material);
        }

        private static final Map<CellStatus, Integer> statusToColor = Map.of(
            CellStatus.RevealedMine, Color.RED,
            CellStatus.RevealedNotMine, Color.LTGRAY,
            CellStatus.Flagged, Color.YELLOW,
            CellStatus.Neutral, Color.DKGRAY
        );
        public int color() {
            return statusToColor.get(this.cell.status());
        }

    public String text() {
        if (this.cell.isRevealed && this.cell.surroundingMines > 0) {
            return (String.valueOf(this.cell.surroundingMines));
        }
        return "";
    }

    public int textColor() {
        switch (this.cell.surroundingMines) {
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
        public void render() {
            this.setClickable(cell.clickable());
            this.setBackgroundColor(this.color());
            this.setText(this.text());
            this.setTextColor(this.textColor());
        }
}
