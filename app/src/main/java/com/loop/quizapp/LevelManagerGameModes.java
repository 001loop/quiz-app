package com.loop.quizapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class LevelManagerGameModes {

    private LevelOtherGameModes[] levels;
    private Context context;

    public LevelManagerGameModes(Context _context) {
        context = _context;
        loadLevelFromJSON();
    }

    public LevelManagerGameModes(Context _context, int numOfLevels) {
        context = _context;
        loadTutorialLevelFromJSON(numOfLevels);
    }

    @SuppressWarnings("all")
    private void loadTutorialLevelFromJSON(int numOfLevels) {
        try {
            InputStream is = context.getAssets().open("quiz_other_game_modes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String stringJSON = new String(buffer, "UTF-8");
            String image;
            String name;
            try {
                JSONArray json = new JSONArray(stringJSON);
                int numOfQuiz = json.length();
                if (numOfLevels < numOfQuiz) {
                    numOfQuiz = numOfLevels;
                }
                levels = new LevelOtherGameModes[numOfQuiz];
                for (int number = 0; number < numOfQuiz; number++) {
                    JSONObject object = json.getJSONObject(number);
                    image = object.getString("image");
                    name = object.getString("name");
                    levels[number] = new LevelOtherGameModes(image, name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //

    @SuppressWarnings("all")
    private void loadLevelFromJSON() {
        try {
            InputStream is = context.getAssets().open("quiz_other_game_modes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String stringJSON = new String(buffer, "UTF-8");
            String image;
            String name;
            try {
                JSONArray json = new JSONArray(stringJSON);
                int numOfQuiz = json.length();
                levels = new LevelOtherGameModes[numOfQuiz];
                for (int number = 0; number < numOfQuiz; number++) {
                    JSONObject object = json.getJSONObject(number);
                    image = object.getString("image");
                    name = object.getString("name");
                    levels[number] = new LevelOtherGameModes(image, name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public LevelOtherGameModes getLevel(int number) {
        if (levels == null) {
            return null;
        }
        if (levels.length > number) {
            return levels[number];
        }
        return null;
    }

    public int getNumOfLevels() {
        if (levels != null) {
            return levels.length;
        } else {
            return 0;
        }
    }

}