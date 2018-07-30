package com.loop.quizapp;

public class DataForEncyclopediaActivity {
    private boolean _filterIsChampions;
    private String _filterCountry;
    private String _filterTeam;
    private boolean _isVolumeOn;

    DataForEncyclopediaActivity(boolean filterIsChampions, String filterCountry,
                                String filterTeam, boolean isVolumeOn) {
        _filterIsChampions = filterIsChampions;
        _filterCountry = filterCountry;
        _filterTeam = filterTeam;
        _isVolumeOn = isVolumeOn;
    }

    public boolean getFilterIsChampions() {
        return _filterIsChampions;
    }

    public String getFilterCountry() {
        return _filterCountry;
    }

    public String getFilterTeam() {
        return _filterTeam;
    }

    public boolean getIsVolumeOn() {
        return _isVolumeOn;
    }

}
