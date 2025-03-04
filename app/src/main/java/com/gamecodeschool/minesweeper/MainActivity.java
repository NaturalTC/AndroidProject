package com.gamecodeschool.minesweeper;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    // initialized game stats
    private int gridSize = 10;
    private static final int MAX_GRID_SIZE = 15;
    private static final int MIN_GRID_SIZE = 5;
    private int mines = 20;
    private static final float MAX_MINES_RATIO = .3f;
    private static final float MIN_MINES_RATIO = .1f;
    private ImageButton upArrowGrid = null;
    TextView gridDisplay = null;
    TextView mineDisplay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // run display
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Adjust for window
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        gridDisplay = findViewById(R.id.grid_display);
        mineDisplay = findViewById(R.id.mine_display);

    }

    ;

    public void onPress(View view) {
        if (view.getId() == R.id.up_arrow_button_grid) {
            addGrid();
        }
        if (view.getId() == R.id.down_arrow_button_grid) {
            subtractGrid();
        }
        if (view.getId() == R.id.up_arrow_button_mines) {
            addMine();
        }
        if (view.getId() == R.id.down_arrow_button_mines) {
            subtractMine();
        }
    }

    private void subtractMine() {
        if (mines > 0) {
            mines--;
            mineDisplay.setText(String.valueOf(mines));
        }
    }

    private void addMine() {
        mines++;
        mineDisplay.setText(String.valueOf(mines));
    }


    @SuppressLint("SetTextI18n")
    private void addGrid() {
        if (gridSize < MAX_GRID_SIZE) {
            gridSize++;
            gridDisplay.setText(gridSize + "x" + gridSize);
        }
    }

    @SuppressLint("SetTextI18n")
    private void subtractGrid() {
        if (gridSize > MIN_GRID_SIZE) {
            gridSize--;
            gridDisplay.setText(gridSize + "x" + gridSize);
        }
    }

    public void runTest(View v) {
        displayPlayBoard(gridSize);
    }

    private void displayPlayBoard(int grid) {
        // Create the main layout
        LinearLayout myLayout = new LinearLayout(this);
        myLayout.setOrientation(LinearLayout.VERTICAL);
        myLayout.setGravity(Gravity.CENTER);
        myLayout.setPadding(0, 205, 0, 0);

        LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        LinearLayout.LayoutParams colLabelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

        myLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create a layout for the column labels (top row)
        LinearLayout topLabelLayout = new LinearLayout(this);
        topLabelLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLabelLayout.setGravity(Gravity.CENTER);
        topLabelLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add 0 placeholder
        TextView emptyLabel = new TextView(this);
        emptyLabel.setLayoutParams(myLayoutParams);
        emptyLabel.setText("0");
        emptyLabel.setGravity(Gravity.CENTER);
        topLabelLayout.addView(emptyLabel);

        // Add the numbers (column labels)
        for (int col = 1; col <= grid; col++) {
            TextView colLabel = new TextView(this);
            colLabel.setText(String.valueOf(col));
            colLabel.setLayoutParams(colLabelParams);
            colLabel.setGravity(Gravity.CENTER);

            topLabelLayout.addView(colLabel);
        }

        myLayout.addView(topLabelLayout);

        // Create the main grid with row labels and buttons
        for (int row = 0; row < grid; row++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Add the row label (character)
            TextView rowLabel = new TextView(this);
            rowLabel.setText(String.valueOf((char) ('A' + row))); // Row label (A, B, C, ...)
            rowLabel.setLayoutParams(myLayoutParams);
            rowLabel.setGravity(Gravity.CENTER);
            rowLayout.addView(rowLabel);

            // Add the buttons for the grid
            for (int col = 0; col < grid; col++) {
                Button btn = new Button(this);
                btn.setLayoutParams(myLayoutParams);
                btn.setOnClickListener(this);
                btn.setOnLongClickListener(this);
                rowLayout.addView(btn);
            }

            myLayout.addView(rowLayout);
        }

        setContentView(myLayout);
    }






    @Override
    public void onClick(View v) {
        v.setBackgroundColor(Color.argb(255, 0, 255, 0));
    }

    @Override
    public boolean onLongClick(View v) {
        v.setBackgroundColor(Color.argb(255, 255, 0, 0));
        return true;
    }
}