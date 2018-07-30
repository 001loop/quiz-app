package com.loop.quizapp;
import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LevelManager{

    private JSONArray json;
    private Context context;

    LevelManager(Context _context) {
        context = _context;
        json = getJson();
    }

    private JSONArray getJson() {
        if (json != null) {
            return json;
        }
        try {
            return new JSONArray(getStringJson());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    int getNumOfLevels() {
        if (json != null) {
            return json.length();
        } else {
            try {
                json = new JSONArray(getStringJson());
                return json.length();
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public DataFilters getFiltersData(Context _context, int currentLevel) {
        context = _context;
        try {
            JSONArray json = new JSONArray(getStringJson());
            return collectFiltersData(json, currentLevel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("all")
    private String getStringJson() {
        try {
            InputStream is = context.getAssets().open("quiz.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Level getFullLevelInfo(int id) {
        if (id >= json.length()) {
            return null;
        }
        json = getJson();
        String image;
        String answerText;
        String linesIndexString;
        String wordsChars;
        try {
            JSONObject object = json.getJSONObject(id);
            id = object.getInt("id");
            image = object.getString("image");
            answerText = object.getString("answer");
            linesIndexString = object.getString("lines_index");
            wordsChars = object.getString("chars");
            return new Level(id, image, answerText, getIntArrayFromString(linesIndexString), wordsChars);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<LevelBase> getLevelsForEncyclopediaActivity(int currentLevel) {
        json = getJson();
        ArrayList<LevelBase> levels = new ArrayList<>();
        try {
            for (int x = 0; x < currentLevel; x++) {
                if (x > json.length()) {
                    break;
                }
                JSONObject object = json.getJSONObject(x);
                levels.add(new LevelBase(
                        x, object.getInt("id"), object.getString("image")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return levels;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private DataFilters collectFiltersData(JSONArray json, int currentLevel) {
        ArrayList<String> countriesArrayList = new ArrayList<>();
        ArrayList<String> teamsArrayList = new ArrayList<>();
        try {
            for (int x = 0; x < currentLevel; x++) {
                if (x > json.length()) {
                    break;
                }
                JSONObject object = json.getJSONObject(x);
                if (object.has("team")) {
                    if (!teamsArrayList.contains(object.getString("team"))) {
                        teamsArrayList.add(object.getString("team"));
                    }
                }
                if (object.has("countries")) {
                    String countries[] = getStringArrayFromString(object.getString("countries"));
                    for (int y = 0; y < countries.length; y++) {
                        if (!countriesArrayList.contains(countries[y])) {
                            countriesArrayList.add(countries[y]);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new DataFilters(getStringArrayFromArrayList(countriesArrayList),
                getStringArrayFromArrayList(teamsArrayList));
    }

    private String[] getStringArrayFromArrayList(ArrayList<String> arrayList) {
        int size = arrayList.size();
        String[] array = new String[size];
        for (int x = 0; x < size; x++) {
            array[x] = arrayList.get(x);
        }
        return array;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private boolean getIsChampion(String[] achievements) {
        if (achievements == null) {
            return false;
        }
        for (int x = 0; x < achievements.length; x++) {
            if (Integer.valueOf(achievements[x].substring(0, 1)).equals(1)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private boolean isCountryExist(String country, String[] countriesArray) {
        if (countriesArray == null) {
            return false;
        }
        for (int x = 0; x < countriesArray.length; x++) {
            if (countriesArray[x].equals(country)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    private Date getDateOfBirth(String dateOfBirth) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = sdf.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    private String getFormattedDateString(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    private int getAge(Date dayOfBirth){
        if (dayOfBirth == null) {
            return -1;
        }
        int age;
        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        dob.setTime(dayOfBirth);
        if (dob.after(now)) {
            return -1;
        }
        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);
        age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }
        return age;
    }

    private String[] getStringArrayFromString(String string) {
        if (string == null) {
            return null;
        }
        int numOfElements = 1;
        for (int x = 0; x < string.length(); x++) {
            if (string.charAt(x) == ';') {
                numOfElements++;
            }
        }
        int startIndex = 0;
        String[] stringArray = new String[numOfElements];
        for (int x = 0; x < numOfElements; x++) {
            int endIndex = string.indexOf(";", startIndex);
            if (endIndex == -1) {
                endIndex = string.length();
            }
            String element = string.substring(startIndex, endIndex);
            if (element.length() > 0) {
                stringArray[x] = element;
            } else {
                return null;
            }
            startIndex = endIndex + 1;
        }
        return stringArray;
    }

    private int[] getIntArrayFromString(String string) {
        if (string == null) {
            return null;
        }
        int numOfLines = 1;
        for (int x = 0; x < string.length(); x++) {
            if (string.charAt(x) == ';') {
                numOfLines++;
            }
        }
        int startIndex = 0;
        int[] lines = new int[numOfLines];
        for (int x = 0; x < numOfLines; x++) {
            int endIndex = string.indexOf(";", startIndex);
            if (endIndex == -1) {
                endIndex = string.length();
            }
            lines[x] = Integer.valueOf(string.substring(startIndex, endIndex));
            startIndex = endIndex + 1;
        }
        return lines;
    }

}
