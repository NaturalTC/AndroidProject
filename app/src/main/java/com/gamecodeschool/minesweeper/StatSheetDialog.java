package com.gamecodeschool.minesweeper;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class StatSheetDialog extends DialogFragment {

    private TextView Rank, Name, Score, Field, Mines, Time, Clicks, sweepDate;
    private ScoreBoard.ScoreEntry entry;
    private int rank;  // Store the rank here

    // Constructor now accepts rank along with ScoreEntry
    public StatSheetDialog(ScoreBoard.ScoreEntry entry, int rank) {
        this.entry = entry;
        this.rank = rank;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.stat_sheet_dialog, null);

        // Textview Methods
        Rank = dialogView.findViewById(R.id.rank_stat);
        Name = dialogView.findViewById(R.id.name_stat);
        Score = dialogView.findViewById(R.id.score_stat);
        Field = dialogView.findViewById(R.id.field_stat);
        Mines = dialogView.findViewById(R.id.mines_stat);
        Time = dialogView.findViewById(R.id.time_stat);
        Clicks = dialogView.findViewById(R.id.clicks_stat);
        sweepDate = dialogView.findViewById(R.id.sweep_date_stat);

        // Set the values in the dialog
        Rank.setText(String.valueOf(rank));
        Name.setText(entry.name);
        Score.setText(String.valueOf(entry.score));
        Field.setText(entry.field);
        Mines.setText(String.valueOf(entry.mines));
        Time.setText(entry.time + "s");
        Clicks.setText(String.valueOf(entry.clicks));
        sweepDate.setText(entry.date);

        // Button Methods
        Button deleteBTN = dialogView.findViewById(R.id.delete_btn);
        Button okBTN = dialogView.findViewById(R.id.ok_btn);

        builder.setView(dialogView).setMessage("High Score Details");

        okBTN.setOnClickListener(v -> dismiss());

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.restartGame();
                dismiss();
            }
        });

        return builder.create();
    }
}
