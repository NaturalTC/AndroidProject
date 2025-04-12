package com.gamecodeschool.minesweeper;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.utilities.Score;

import java.util.Collections;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    // Declarations
    private final List<ScoreBoard.ScoreEntry> scoreList;
    private OnScoreClickListener listener;

    // Holds references to the views in my Recycler View
    public static class ScoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Rank, Name, Score;

        public ScoreViewHolder(@NonNull View view) {
            super(view);
            Rank = itemView.findViewById(R.id.score_rank);
            Name = itemView.findViewById(R.id.score_name);
            Score = itemView.findViewById(R.id.score_score);
        }

        @Override
        public void onClick(View v) {
            // --
        }
    }

    // Inflates and Wraps XML file into a list item
    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(view);
    }
    // Connects Data to UI -- Handles Logic for UI

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        // Sort the scores in descending order before displaying (highest score first)
        scoreList.sort((entry1, entry2) -> Integer.compare(entry2.score, entry1.score));

        // Store each score of the list
        ScoreBoard.ScoreEntry entry = scoreList.get(position);
        // Handle ties by checking if the current score is equal to the previous score
        int rank = position + 1;
        if (position > 0 && scoreList.get(position).score == scoreList.get(position - 1).score) {
            rank = position; // Same rank as the previous one
        }

        // Links Data to the UI
        holder.Rank.setText(String.valueOf(rank));
        holder.Name.setText(entry.name);
        holder.Score.setText(String.valueOf(entry.score));
        holder.itemView.setOnClickListener(v -> listener.onScoreClick(entry));
    }
    // Provides total count of items inside the list

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

// Methods
public ScoreAdapter(List<ScoreBoard.ScoreEntry> scoreList, OnScoreClickListener listener) {
    this.scoreList = scoreList;
    this.listener = listener;
}

public interface OnScoreClickListener {
    void onScoreClick(ScoreBoard.ScoreEntry entry);

    }
}
