package com.loop.quizapp;

public class LevelBase {
    int id;
    int levelNumber;
    String image;

    LevelBase(int _id,int _levelNumber, String _image) {
        id = _id;
        levelNumber = _levelNumber;
        image = _image;
    }

    public int getId() {
        return id;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getImage() {
        return image;
    }

}