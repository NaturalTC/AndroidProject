package com.gamecodeschool.minesweeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreDialog extends DialogFragment {
    private ScoreBoard scoreBoard;
    private int playerScore;
    private TextView[] scoreViews = new TextView[5];
    private EditText nameInput;
    private int playerRank = -1;
    private Button okButton;

    public ScoreDialog(int score, ScoreBoard scoreBoard) {
        this.playerScore = score;
        this.scoreBoard = scoreBoard;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.score_display, null);
        builder.setView(view);

        // Get UI elements
        scoreViews[0] = view.findViewById(R.id.rank1);
        scoreViews[1] = view.findViewById(R.id.rank2);
        scoreViews[2] = view.findViewById(R.id.rank3);
        scoreViews[3] = view.findViewById(R.id.rank4);
        scoreViews[4] = view.findViewById(R.id.rank5);
        nameInput = view.findViewById(R.id.nameInput);
        okButton = view.findViewById(R.id.okButton);

        loadHighScores();

        if (playerRank != -1) {
            nameInput.setVisibility(View.VISIBLE);
            nameInput.requestFocus();

            // Show keyboard safely
            nameInput.post(() -> {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(nameInput, InputMethodManager.SHOW_IMPLICIT);
                }
            });

            nameInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (playerRank != -1 && playerRank < scoreBoard.getHighScores().size()) {
                        scoreBoard.getHighScores().get(playerRank).name = s.toString();
                        updateHighScoreUI();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() >= 3) {
                        nameInput.setVisibility(View.GONE);
                        updateHighScoreUI();
                        view.requestLayout();
                    }
                }
            });
        } else {
            nameInput.setVisibility(View.GONE);
        }

        okButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).restartGame();
            }
            dismiss();
        });

        return builder.create();
    }

    private void loadHighScores() {
        ArrayList<ScoreBoard.ScoreEntry> scores = new ArrayList<>(scoreBoard.getHighScores());
        scores.sort(Comparator.comparingInt(a -> -a.score)); // Sort in descending order

        int lastScore = -1;
        int rank = 1;

        for (int i = 0; i < 5; i++) {
            if (i < scores.size()) {
                ScoreBoard.ScoreEntry entry = scores.get(i);
                if (entry.score != lastScore) {
                    rank = i + 1;
                }
                scoreViews[i].setText(rank + "         -         " + entry.name + "         -         " + entry.score);
                lastScore = entry.score;

                // Determine player rank
                if (entry.score == playerScore && playerRank == -1) {
                    playerRank = i;
                }
            } else {
                scoreViews[i].setText((i + 1) + "         -         ???         -         ");
            }
        }
    }

    private void updateHighScoreUI() {
        ArrayList<ScoreBoard.ScoreEntry> scores = scoreBoard.getHighScores();
        for (int i = 0; i < 5; i++) {
            if (i < scores.size()) {
                ScoreBoard.ScoreEntry entry = scores.get(i);
                scoreViews[i].setText((i + 1) + "         -         " + entry.name + "         -         " + entry.score);
            }
        }
    }
}
