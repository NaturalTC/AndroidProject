package com.gamecodeschool.minesweeper;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ScoreDialog extends DialogFragment  {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =
                getActivity().getLayoutInflater();

        View scoreView =
                inflater.inflate(R.layout.score_display, null);

        TextView rank =
                scoreView.findViewById(R.id.rank);

        TextView name =
                scoreView.findViewById(R.id.name);

        TextView score =
                scoreView.findViewById(R.id.score);

        Button enterBTN =
                scoreView.findViewById(R.id.enter);

        builder.setView(scoreView).setMessage("High Scores");


        enterBTN.setOnClickListener(v -> {
            // Get a reference to MainActivity
            MainActivity callingActivity = (MainActivity) getActivity();
            callingActivity.restartGame();
            dismiss();
        });

        return builder.create();

    }
}
