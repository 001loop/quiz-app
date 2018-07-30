package com.loop.quizapp;

public class DataForOptionsActivity {
    private boolean _isVolumeOn;
    private boolean _playGamesEnabled;

    DataForOptionsActivity(boolean isVolumeOn, boolean playGamesEnabled) {
        _isVolumeOn = isVolumeOn;
        _playGamesEnabled = playGamesEnabled;
    }

    public boolean getIsVolumeOn() {
        return _isVolumeOn;
    }

    public boolean getPlayGamesEnabled() {
        return _playGamesEnabled;
    }

}