package com.loop.quizapp;

public class DataGameModeYesOrNo {
    String imageName;
    String answer;
    boolean isItTrue;

    DataGameModeYesOrNo(String _imageName, String _answer, boolean _isItTrue) {
        imageName = _imageName;
        answer = _answer;
        isItTrue = _isItTrue;
    }

    public String getImageName() {
        return imageName;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean getIsItTrue() {
        return isItTrue;
    }

}
