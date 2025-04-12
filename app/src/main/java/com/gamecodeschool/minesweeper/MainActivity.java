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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    // Game stats
    private int gridSize = 10;
    private static final int MAX_GRID_SIZE = 15;
    private static final int MIN_GRID_SIZE = 5;
    private int mines = 20;
    private static final float MAX_MINES_RATIO = 0.3f;
    private static final float MIN_MINES_RATIO = 0.1f;

    // UI Elements
    private TextView gridDisplay;
    private TextView mineDisplay;
    private TextView scoreDisplay;
    private MineCellButton[][] buttons;
    private TextView timerDisplay;
    private Button restartButton;
    private long startTime;
    private final Handler handler = new Handler();
    private MineSweeperGame game;
    private boolean gameOver = false;
    private ScoreBoard scoreBoard;


    // --- START OF APP ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize UI
        gridDisplay = findViewById(R.id.grid_display);
        mineDisplay = findViewById(R.id.mine_display);

        // Initialize ScoreBoard
        scoreBoard = new ScoreBoard();

        // Test Code
        scoreBoard.addScore(20, "Jos");
        scoreBoard.addScore(20, "CAR");
        scoreBoard.addScore(30, "zZz");
        scoreBoard.addScore(30, "Dil");
        scoreBoard.addScore(40, "XPZ");
        scoreBoard.addScore(50, "GOD");
        scoreBoard.addScore(60, "Bit");
        scoreBoard.addScore(60, "Byt");
        scoreBoard.addScore(70, "And");



        // -- RecyclerView setup --

        // Find the RecyclerView by its ID
        RecyclerView recyclerView = findViewById(R.id.score_recycler);

        // Initialize the adapter with the score data
        ScoreAdapter adapter = new ScoreAdapter(scoreBoard.getMainScores(), score -> Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show());

        // Set the LayoutManager
        recyclerView.
                setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Add a neat dividing line between items in the list
        recyclerView.
                addItemDecoration(new DividerItemDecoration(
                        this,
                        LinearLayoutManager.
                        VERTICAL));

        // *Set the Adapter
        recyclerView.setAdapter(adapter);


    }

    // --- UI (Up and Down Button) Link ID to Method ---
    public void onPress(View view) {
        if (view.getId() == R.id.up_arrow_button_grid) addGrid();
        if (view.getId() == R.id.down_arrow_button_grid) subtractGrid();
        if (view.getId() == R.id.up_arrow_button_mines) addMine();
        if (view.getId() == R.id.down_arrow_button_mines) subtractMine();
    }

    // -- UI (Up and Down) Methods --
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

    // ---- After UI Settings Decided ----

    // --- Check the Settings
    public void checkSettings(View v) {
        if (!isMineRatioValid()) {
            Toast.makeText(this, "Invalid Mine Count", Toast.LENGTH_SHORT).show();
            return;
        }
        startNewGame();
    }

    private boolean isMineRatioValid() {
        int minMines = (int) (MIN_MINES_RATIO * gridSize * gridSize);
        int maxMines = (int) (MAX_MINES_RATIO * gridSize * gridSize);
        return mines >= minMines && mines <= maxMines;
    }

    // --- Start Game If Proper Settings ---
    private void startNewGame() {
        game = new MineSweeperGame(gridSize, gridSize, mines);
        displayPlayBoard(gridSize);
        startTimer();
    }


    // --- Create and Display the PlayBoard
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
        buttons = new MineCellButton[grid][grid];

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
                MineCellButton btn = new MineCellButton(this, game.getCell(row, col));
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
        scoreDisplay.setText("");
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

            // Dialog Popup - Restart Game
            NewGameDialog dialog = new NewGameDialog();

            // Create the dialog
            dialog.show(getSupportFragmentManager(), "Confirm_popup");

        });

        // Add views to bottom layout
        bottomLayout.addView(mineDisplay);
        bottomLayout.addView(restartButton);
        bottomLayout.addView(timerDisplay);
        bottomLayout.addView(scoreDisplay);
        rootLayout.addView(bottomLayout);
        setContentView(rootLayout);
    }


    private void updateBoard() {
        for (MineCellButton[] buttonRow : buttons) {
            for (MineCellButton btn : buttonRow) {
                btn.render();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (gameOver) return;
        if (!(v instanceof MineCellButton)) {
            return;
        }
        MineCellButton btn = (MineCellButton) v;
        MineCell clickedCell = btn.cell;
        clickedCell.setClicked();

        if (clickedCell.hasMine) {
            btn.setBackgroundColor(Color.RED);
            revealAllMinesExceptClicked(clickedCell.row, clickedCell.col);
            stopTimer();
            restartButton.setBackgroundColor(Color.rgb(0,100,220));
            endButton();
            endButton();
            return;
        }
        updateBoard();
        checkForWin();
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
        if (game.isWin()) {
            restartButton.setBackgroundColor(Color.GREEN);
            Toast.makeText(this, "Game Win", Toast.LENGTH_SHORT).show();
            gameOver = true;
            stopTimer();
            int currentScore = getScore();

            if (scoreBoard != null) {
                scoreBoard.addScore(currentScore, "???");
            }
            endButton();
        }
    }

    public void endButton() {
        int currentScore = getScore();
        restartButton.setOnClickListener(v -> {
            ScoreDialog dialog = new ScoreDialog(currentScore, scoreBoard);
            dialog.show(getSupportFragmentManager(), "ScoreDialog");
        });
    }
    @Override
    public boolean onLongClick(View v) {
        if (gameOver) {
            return false;
        }
        if (!(v instanceof MineCellButton)) {
            return false;
        }
        MineCellButton btn = (MineCellButton) v;
        game.toggleFlag(btn.cell.row, btn.cell.col);
        updateBoard();
        mineDisplay.setText(String.valueOf(game.getRemainingFlags()));
        checkForWin();
        return true;
    }
    private final Runnable renderApp = new Runnable() {
        @Override
        public void run() {
            timerDisplay.setText(getDisplayTime());
            scoreDisplay.setText(String.valueOf(getScore()));
            handler.postDelayed(this, 1000);
        }
    };

    private int getScore() {
        return Math.max(((int) (Math.pow(gridSize, 2)) + mines - getElapsedTimeSeconds() / 6), 0);
    }

    private int getElapsedTimeSeconds() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return (int) ((elapsedTime / 1000) % 60);
    }

    @SuppressLint("DefaultLocale")
    private String getDisplayTime() {
        int totalSeconds = getElapsedTimeSeconds();
        int remainderSeconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, remainderSeconds);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void displayInitialConfigScreen() {
        setContentView(R.layout.activity_main);
        gridDisplay = findViewById(R.id.grid_display);
        mineDisplay = findViewById(R.id.mine_display);
        gridDisplay.setText(gridSize + "x" + gridSize);
        mineDisplay.setText(String.valueOf(mines));

        RecyclerView recyclerView = findViewById(R.id.score_recycler);
        if (recyclerView != null) {
            // Create or update the adapter with the score data
            ScoreAdapter adapter = new ScoreAdapter(scoreBoard.getMainScores(), scores -> Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show());
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set the LayoutManager
            recyclerView.setAdapter(adapter);  // Set the Adapter to the RecyclerView
            adapter.notifyDataSetChanged(); // Refresh the RecyclerView

            recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, LinearLayoutManager.
                            VERTICAL));
        }
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        handler.post(renderApp);
        // handler.post();
    }
    private void stopTimer() {
        handler.removeCallbacks(renderApp);
    }

    public void restartGame() {
            stopTimer();
            gameOver = false;
            displayInitialConfigScreen();
    }
}