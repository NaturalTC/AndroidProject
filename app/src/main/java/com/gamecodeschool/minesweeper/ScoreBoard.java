package com.gamecodeschool.minesweeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreBoard {
    private static final int MAX_SCORES = 5;
    private ArrayList<ScoreEntry> highScores;

    public ScoreBoard() {
        highScores = new ArrayList<>();
    }

    public void addScore(int score, String name) {
        highScores.add(new ScoreEntry(score, name));
        Collections.sort(highScores, Comparator.comparingInt(entry -> -entry.score)); // Sort in descending order

        // Keep only top 5 scores
        if (highScores.size() > MAX_SCORES) {
            highScores.remove(highScores.size() - 1);
        }
    }

    // **Added this method**
    public ArrayList<ScoreEntry> getHighScores() {
        return highScores;
    }

    public static class ScoreEntry {
        int score;
        String name;

        public ScoreEntry(int score, String name) {
            this.score = score;
            this.name = name;
        }
    }
}
