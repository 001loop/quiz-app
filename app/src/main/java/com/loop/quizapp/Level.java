package com.loop.quizapp;

import java.io.Serializable;

public class Level implements Serializable {
    int id;
    String image;
    Symbol[] answer;
    int[] linesIndex;
    Symbol[] keyboardChars;

    Level(int _id, String _image, String _answer, int[] _linesIndex, String _keyboardChars) {
        id = _id;
        image = _image;
        answer = Symbol.arrayFromString(_answer);
        linesIndex = _linesIndex;
        keyboardChars = Symbol.arrayFromString(_keyboardChars);
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }


    public Symbol[] getAnswer() {
        return answer;
    }

    public int[] getLinesIndex() {
        return linesIndex;
    }

    public Symbol[] getKeyboardChars() {
        return keyboardChars;
    }

}
