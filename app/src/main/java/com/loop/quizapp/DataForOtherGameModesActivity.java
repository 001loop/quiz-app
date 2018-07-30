package com.loop.quizapp;

public class DataForOtherGameModesActivity {
    private boolean _advertisementEnabled;
    private boolean _isPremiumActive;
    private int _highScore;
    private boolean _isVolumeOn;

    DataForOtherGameModesActivity(boolean advertisementEnabled, boolean isPremiumActive,
                                  int highScore, boolean isVolumeOn) {
        _advertisementEnabled = advertisementEnabled;
        _isPremiumActive = isPremiumActive;
        _highScore = highScore;
        _isVolumeOn = isVolumeOn;
    }

    public boolean getAdvertisementEnabled() {
        return _advertisementEnabled;
    }

    public boolean getIsPremiumActive() {
        return _isPremiumActive;
    }

    public int getHighScore() {
        return _highScore;
    }

    public boolean getIsVolumeOn() {
        return _isVolumeOn;
    }

}
