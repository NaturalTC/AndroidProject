package com.gamecodeschool.minesweeper;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoard {

    public static class ScoreEntry {
        int score;
        String name;
        int time;
        int clicks;
        String date;
        int mines;
        String field;

        public ScoreEntry(int score, String name, int time, int clicks, String date, int mines, String field) {
            this.score = score;
            this.name = name;
            this.time = time;
            this.clicks = clicks;
            this.date = date;
            this.mines = mines;
            this.field = field;
        }
    }




    // Declarations
    private static final int MAX_HIGH_SCORES = 5;
    private static final int MAX_MAIN_SCORES = 10;
    private final ArrayList<ScoreEntry> highScores;
    private final ArrayList<ScoreEntry> mainScores;

    // Constructor
    public ScoreBoard() {
        highScores = new ArrayList<>();
        mainScores = new ArrayList<>();
    }

    // Method to add a score
    public void addScore(ScoreEntry entry) {
        mainScores.add(entry);
        highScores.add(entry);

        mainScores.sort(Comparator.comparingInt(e -> -e.score));
        highScores.sort(Comparator.comparingInt(e -> -e.score));

        checkHighScore();
        checkMainScore();
    }

    public int getRank(ScoreEntry entry) {
        // Sort scores in descending order
        highScores.sort(Comparator.comparingInt(e -> -e.score));

        // Find the index of the entry and calculate rank (index + 1)
        return highScores.indexOf(entry) + 1;
    }

    public void checkHighScore(){
        // Keep only top 5 scores for highScores
        if (highScores.size() > MAX_HIGH_SCORES) {
            highScores.remove(highScores.size() - 1);
        }
    }
    public void checkMainScore(){
        // Keep only top 10 scores for mainScores
        if (highScores.size() > MAX_MAIN_SCORES) {
            highScores.remove(highScores.size() - 1);
        }
    }
    // Method to get high scores
    public ArrayList<ScoreEntry> getHighScores() {
        return highScores;
    }

    // Method to get main scores
    public ArrayList<ScoreEntry> getMainScores() {
        return mainScores;
    }

    // Method to remove a score from highScores
    public void removeScore(ScoreEntry entry) {
        highScores.remove(entry);
    }
}
