package com.gamecodeschool.minesweeper;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.widget.AppCompatButton;

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

        public void render() {
            this.setClickable(cell.clickable());
            this.setBackgroundColor(cell.color());
            this.setText(cell.text());
            this.setTextColor(cell.textColor());
        }
}
