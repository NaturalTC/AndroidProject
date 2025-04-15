package com.gamecodeschool.minesweeper;

import android.content.Context;
import android.util.Log;

import com.google.android.material.color.utilities.Score;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONSerializer {
    private final String fileName;
    private final Context context;

    public JSONSerializer(String fileName, Context context) {
        this.fileName = fileName;
        this.context = context;
    }

    // Save high scores to a JSON file
    public void save(List<ScoreBoard.ScoreEntry> scores) throws IOException, JSONException {
        // Create a JSONArray to hold all the ScoreEntry objects
        JSONArray scoresArray = new JSONArray();

        // Convert each ScoreEntry object to JSONObject and add to the array
        for (ScoreBoard.ScoreEntry score : scores) {
            scoresArray.put(score.convertToJSON());
        }
        Log.d("JSONSerializer", "Saved JSON: " + scoresArray.toString());
        // Save the JSON array to a file
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write(scoresArray.toString().getBytes());
        fos.close();
    }


    // Load high scores from a JSON file
    public ArrayList<ScoreBoard.ScoreEntry> load() throws IOException, JSONException{
        ArrayList<ScoreBoard.ScoreEntry> noteList = new ArrayList<ScoreBoard.ScoreEntry>();
        BufferedReader reader = null;
        try {

            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {

                jsonString.append(line);
            }


            JSONArray jArray = (JSONArray) new
                    JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jArray.length(); i++) {
                noteList.add(new ScoreBoard.ScoreEntry(jArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // we will ignore this one, since it happens
            // when we start fresh. You could add a log here.
        } finally {// This will always run
            if (reader != null)
                reader.close();
        }

        return noteList;
    }


}