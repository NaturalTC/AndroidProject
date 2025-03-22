package com.gamecodeschool.minesweeper;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    // Game stats
    private int gridSize = 10;
    private static final int MAX_GRID_SIZE = 15;
    private static final int MIN_GRID_SIZE = 5;
    private int mines = 20;
    private int score = gridSize*gridSize+mines;
    private static final float MAX_MINES_RATIO = 0.3f;
    private static final float MIN_MINES_RATIO = 0.1f;

    // UI Elements
    private TextView gridDisplay;
    private TextView mineDisplay;
    private TextView scoreDisplay;
    private Button[][] buttons;
    private TextView timerDisplay;
    private Button restartButton;
    private long startTime;
    private Handler handler = new Handler();
    private MineSweeperGame game;
    private boolean gameOver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Adjust for system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI
        gridDisplay = findViewById(R.id.grid_display);
        mineDisplay = findViewById(R.id.mine_display);

    }

    // Start Game

    public void onPress(View view) {
        if (view.getId() == R.id.up_arrow_button_grid) addGrid();
        if (view.getId() == R.id.down_arrow_button_grid) subtractGrid();
        if (view.getId() == R.id.up_arrow_button_mines) addMine();
        if (view.getId() == R.id.down_arrow_button_mines) subtractMine();
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
    private boolean isMineRatioValid() {
        int minMines = (int) (MIN_MINES_RATIO * gridSize * gridSize);
        int maxMines = (int) (MAX_MINES_RATIO * gridSize * gridSize);
        return mines >= minMines && mines <= maxMines;
    }
    public void runTest(View v) {
        if (isMineRatioValid()) {
            startNewGame();
        } else {
            Toast.makeText(this, "Invalid Mine Count", Toast.LENGTH_SHORT).show();
        }
    }

    private void startNewGame() {
        game = new MineSweeperGame(gridSize, gridSize, mines);
        displayPlayBoard(gridSize);
        startTimer();
        //startScore();
    }

    @SuppressLint("SetTextI18n")
    private void displayPlayBoard(int grid) {
        // Root Layout
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(0, 100, 0, 0);

        // Board Layout
        LinearLayout boardLayout = new LinearLayout(this);
        boardLayout.setOrientation(LinearLayout.VERTICAL);
        boardLayout.setGravity(Gravity.CENTER);
        boardLayout.setPadding(5,5,5,5);

        LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );

        LinearLayout.LayoutParams colLabelParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );

        // Top row with column labels
        LinearLayout topLabelLayout = new LinearLayout(this);
        topLabelLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLabelLayout.setGravity(Gravity.CENTER);
        topLabelLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Empty corner label
        TextView emptyLabel = new TextView(this);
        emptyLabel.setLayoutParams(myLayoutParams);
        emptyLabel.setText("0");
        emptyLabel.setGravity(Gravity.CENTER);
        topLabelLayout.addView(emptyLabel);

        // Column labels
        for (int col = 1; col <= grid; col++) {
            TextView colLabel = new TextView(this);
            colLabel.setText(String.valueOf(col));
            colLabel.setLayoutParams(colLabelParams);
            colLabel.setGravity(Gravity.CENTER);
            topLabelLayout.addView(colLabel);
        }
        boardLayout.addView(topLabelLayout);

        // Initialize button grid
        buttons = new Button[grid][grid];

        // Create the grid
        for (int row = 0; row < grid; row++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Row label
            TextView rowLabel = new TextView(this);
            rowLabel.setText(String.valueOf((char) ('A' + row)));
            rowLabel.setLayoutParams(myLayoutParams);
            rowLabel.setGravity(Gravity.CENTER);
            rowLayout.addView(rowLabel);

            // Buttons for grid
            for (int col = 0; col < grid; col++) {
                Button btn = new Button(this);
                btn.setLayoutParams(myLayoutParams);
                btn.setOnClickListener(this);
                btn.setOnLongClickListener(this);
                rowLayout.addView(btn);
                buttons[row][col] = btn;
            }
            boardLayout.addView(rowLayout);
        }

        // Spacing Between Buttons
        myLayoutParams.setMargins(5,5,5,5);

        // Add all Layouts to my root
        rootLayout.addView(boardLayout);

        // Create Bottom layout
        LinearLayout bottomLayout = new LinearLayout(this);
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.setGravity(Gravity.CENTER);
        bottomLayout.setPadding(0, 50, 0, 50);
        bottomLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Score Counter Display (Bottom Right)
        scoreDisplay = new TextView(this);
        scoreDisplay.setText(String.valueOf(""));
        scoreDisplay.setTextSize(18);
        scoreDisplay.setGravity(Gravity.START);
        scoreDisplay.setPadding(100, 20, 60, 20);

        // Mine Counter Display (Bottom Left)
        mineDisplay = new TextView(this);
        mineDisplay.setText(String.valueOf(mines));
        mineDisplay.setTextSize(18);
        mineDisplay.setGravity(Gravity.START);
        mineDisplay.setPadding(20, 20, 60, 20);

        // Timer Display (Bottom Right)
        timerDisplay = new TextView(this);
        timerDisplay.setText("");
        timerDisplay.setTextSize(18);
        timerDisplay.setGravity(Gravity.END | Gravity.BOTTOM);
        timerDisplay.setPadding(60, 20, 20, 20);

        // Restart Button (Bottom Center)
        restartButton = new Button(this);
        restartButton.setBackgroundColor(Color.LTGRAY);
        restartButton.setText("NEW GAME");
        restartButton.setGravity(Gravity.CENTER);
        restartButton.setPadding(0, 20, 0, 20);
        restartButton.setOnClickListener(v -> {

            // Create a new DialogShowNote called dialog
            NewGameDialog dialog = new NewGameDialog();
            // Create the dialog
            dialog.show(getSupportFragmentManager(), "Confirm_popup");

          /*  ScoreDialog scoreDialog = new ScoreDialog();
            scoreDialog.show(getSupportFragmentManager(), "scoreDialog");*/

        });

        // Add views to bottom layout

        bottomLayout.addView(mineDisplay);
        bottomLayout.addView(restartButton);
        bottomLayout.addView(timerDisplay);
        bottomLayout.addView(scoreDisplay);
        rootLayout.addView(bottomLayout);
        setContentView(rootLayout);
    }
    private int getColorForNumber(int number) {
        switch (number) {
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
    private void updateBoard() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                MineCell cell = game.getCell(row, col);
                Button btn = buttons[row][col];
                if (cell.isRevealed) {
                    btn.setClickable(false);
                    if (cell.hasMine) {
                        btn.setBackgroundColor(Color.RED);
                    } else {
                        btn.setBackgroundColor(Color.LTGRAY);
                        if (cell.surroundingMines > 0) {
                            btn.setPadding(5,5,5,5);
                            btn.setText(String.valueOf(cell.surroundingMines));
                            btn.setTextColor(getColorForNumber(cell.surroundingMines));
                        } else {
                            btn.setText("");
                        }
                    }
                } else if (cell.isFlagged) {
                    btn.setBackgroundColor(Color.YELLOW);
                } else {
                    btn.setText("");
                    btn.setBackgroundColor(Color.DKGRAY);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (gameOver) return;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (buttons[row][col] == v) {
                    if (game.getCell(row, col).isRevealed) return;
                    if (game.getCell(row, col).hasMine) {
                        Button clickedBtn = buttons[row][col];
                        clickedBtn.setBackgroundColor(Color.RED);
                        revealAllMinesExceptClicked(row, col);
                        stopTimer();
                        restartButton.setBackgroundColor(Color.rgb(0,100,220));
                        return;
                    } else {
                        game.revealCell(row, col);
                    }
                    updateBoard();
                    checkForWin();
                    return;
                }
            }
        }
    }
    private void revealAllMinesExceptClicked(int clickedRow, int clickedCol) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                MineCell cell = game.getCell(row, col);
                Button btn = buttons[row][col];

                if (cell.hasMine && !cell.isFlagged && !(row == clickedRow && col == clickedCol)) {
                    btn.setBackgroundColor(Color.BLACK);
                }

                if (cell.isFlagged && !cell.hasMine) {
                    btn.setText("X");
                }
                btn.setClickable(false);
            }
        }
        Toast.makeText(this,"Game Lost",Toast.LENGTH_SHORT).show();
        gameOver = true;
    }
    private void checkForWin() {
        boolean allMinesFlagged = true;
        boolean allNonMinesRevealed = true;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                MineCell cell = game.getCell(row, col);

                if (cell.hasMine && !cell.isFlagged) {
                    allMinesFlagged = false;
                }

                if (!cell.hasMine && !cell.isRevealed) {
                    allNonMinesRevealed = false;
                }
            }
        }
        if (allMinesFlagged && allNonMinesRevealed) {
            restartButton.setBackgroundColor(Color.GREEN);
            Toast.makeText(this,"Game Win",Toast.LENGTH_SHORT).show();
            gameOver = true;
            scoreDialogDisplay();
        };
    }

    private void scoreDialogDisplay() {
        // Create a new DialogShowNote called dialog
        ScoreDialog myscoredialog = new ScoreDialog();

        // Create the dialog
        myscoredialog.show(getSupportFragmentManager(), "ScoreDialog");
    }


    @Override
    public boolean onLongClick(View v) {
        if (gameOver) {
            return false;
        }
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (buttons[row][col] == v) {
                    game.toggleFlag(row, col);
                    updateBoard();
                    mineDisplay.setText(String.valueOf(game.getRemainingFlags()));

                    return true;
                }
            }
        }
        return false;
    }
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - startTime;
            int hours = (int) (elapsedTime / (1000 * 60 * 60));
            int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
            int seconds = (int) ((elapsedTime / 1000) % 60);
            timerDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            handler.postDelayed(this, 1000);
        }
    };

    private Runnable updateScore = new Runnable() {
        @Override
        public void run() {
            long elapsedScore = score - 1;
            scoreDisplay.setText(score);
            handler.postDelayed(this, 6000);
        }
    };

    private void displayInitialConfigScreen() {
        setContentView(R.layout.activity_main);
        gridDisplay = findViewById(R.id.grid_display);
        mineDisplay = findViewById(R.id.mine_display);
        gridDisplay.setText(gridSize + "x" + gridSize);
        mineDisplay.setText(String.valueOf(mines));
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        handler.post(updateTimer);
        // handler.post();
    }
    private void stopTimer() {
        handler.removeCallbacks(updateTimer);
    }

    public void restartGame() {
            stopTimer();
            gameOver = false;
            displayInitialConfigScreen();
    }
}