package com.loop.quizapp;

public class DataForGameActivity {
    private boolean _advertisementEnabled;
    private boolean _isPremiumActive;
    private int _currentLevel;
    private int _maxAchievedLevel;
    private boolean _isRemoveWordsAvailable;
    private boolean _isShareAppRewardEnabled;
    private boolean _isShareAppRewardAnimationEnabled;
    private boolean _isVolumeOn;

    DataForGameActivity(boolean advertisementEnabled, boolean isPremiumActive, int currentLevel,
                        int maxAchievedLevel, boolean isRemoveWordsAvailable,
                        boolean isShareAppRewardEnabled, boolean isShareAppRewardAnimationEnabled,
                        boolean isVolumeOn) {
        _advertisementEnabled = advertisementEnabled;
        _isPremiumActive = isPremiumActive;
        _currentLevel = currentLevel;
        _maxAchievedLevel = maxAchievedLevel;
        _isRemoveWordsAvailable = isRemoveWordsAvailable;
        _isShareAppRewardEnabled = isShareAppRewardEnabled;
        _isShareAppRewardAnimationEnabled = isShareAppRewardAnimationEnabled;
        _isVolumeOn = isVolumeOn;
    }

    public boolean getAdvertisementEnabled() {
        return _advertisementEnabled;
    }

    public boolean getIsPremiumActive() {
        return _isPremiumActive;
    }

    public int getCurrentLevel() {
        return _currentLevel;
    }

    public int getMaxAchievedLevel() {
        return _maxAchievedLevel;
    }

    public boolean getIsRemoveCharsAvailable() {
        return _isRemoveWordsAvailable;
    }

    public boolean getIsShareAppRewardEnabled() {
        return _isShareAppRewardEnabled;
    }

    public boolean getIsShareAppRewardAnimationEnabled() {
        return _isShareAppRewardAnimationEnabled;
    }

    public boolean getIsVolumeOn() {
        return _isVolumeOn;
    }

}