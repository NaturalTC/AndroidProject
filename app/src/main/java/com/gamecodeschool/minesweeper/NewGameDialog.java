package com.gamecodeschool.minesweeper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class NewGameDialog extends DialogFragment  {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
            new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =
            getActivity().getLayoutInflater();

        View dialogView =
            inflater.inflate(R.layout.popup, null);

        Button cancelBTN =
            dialogView.findViewById(R.id.back);

        Button restartBTN =
            dialogView.findViewById(R.id.next);

        builder.setView(dialogView).setMessage("New Game?");

        cancelBTN.setOnClickListener(v -> dismiss());

        restartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a reference to MainActivity
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.restartGame();
                dismiss();
            }
        });

        return builder.create();

    }
}


