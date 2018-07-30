package com.loop.quizapp;

public class DataForStoreActivity {
    private boolean _advertisementEnabled;
    private boolean _specialOfferEnabled;
    private boolean _isPremiumActive;
    private boolean _isVolumeOn;

    DataForStoreActivity(boolean advertisementEnabled, boolean specialOfferEnabled,
                         boolean isPremiumActive, boolean isVolumeOn) {
        _advertisementEnabled = advertisementEnabled;
        _specialOfferEnabled = specialOfferEnabled;
        _isPremiumActive = isPremiumActive;
        _isVolumeOn = isVolumeOn;
    }

    public boolean getAdvertisementEnabled() {
        return _advertisementEnabled;
    }

    public boolean getSpecialOfferEnabled() {
        return _specialOfferEnabled;
    }

    public boolean getIsPremiumActive() {
        return _isPremiumActive;
    }

    public boolean getIsVolumeOn() {
        return _isVolumeOn;
    }

}


