package com.loop.quizapp;

import java.util.ArrayList;

public class DataOtherGameModes {
    String imageName;
    ArrayList<String> answers;

    DataOtherGameModes(String _imageName, ArrayList<String> _answers) {
        imageName = _imageName;
        answers = _answers;
    }

    public String getImageName() {
        return imageName;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

}
