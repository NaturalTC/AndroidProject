package com.gamecodeschool.minesweeper;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoard {

    public static class ScoreEntry {
        int score;
        String name;

        public ScoreEntry(int score, String name) {
            this.score = score;
            this.name = name;
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
        mainScores = new ArrayList<>();  // Ensure mainScores is initialized
    }

    // Method to add a score
    public void addScore(int score, String name) {


        mainScores.add(new ScoreEntry(score, name));
        highScores.add(new ScoreEntry(score, name));

        // Sort in descending order for both lists
        mainScores.sort(Comparator.comparingInt(entry -> -entry.score)); // Sort mainScores in descending order
        highScores.sort(Comparator.comparingInt(entry -> -entry.score)); // Sort highScores in descending order
        checkHighScore();
        checkMainScore();


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
