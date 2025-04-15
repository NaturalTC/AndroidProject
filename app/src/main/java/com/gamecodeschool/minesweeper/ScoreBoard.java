package com.gamecodeschool.minesweeper;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

        public ScoreEntry(JSONObject jo) throws JSONException {
            this.score = jo.getInt("score");
            this.name = jo.getString("name");
            this.time = jo.getInt("time");
            this.clicks = jo.getInt("clicks");
            this.date = jo.getString("date");
            this.mines = jo.getInt("mines");
            this.field = jo.getString("field");
        }

        // Convert ScoreEntry to JSONObject
        public JSONObject convertToJSON() throws JSONException {
            JSONObject jo = new JSONObject();
            jo.put("score", this.score);
            jo.put("name", this.name);
            jo.put("time", this.time);
            jo.put("clicks", this.clicks);
            jo.put("date", this.date);
            jo.put("mines", this.mines);
            jo.put("field", this.field);
            return jo;
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

        Log.d("JSONSerializer", "Saved JSON: " + mainScores.toString());
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
        mainScores.remove(entry);
    }
}
