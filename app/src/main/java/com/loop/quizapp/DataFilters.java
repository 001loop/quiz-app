package com.loop.quizapp;

public class DataFilters {

    String[] _filterCountries;
    String[] _filterTeams;

    DataFilters(String[] filterCountries, String[] filterTeams) {
        _filterCountries = filterCountries;
        _filterTeams = filterTeams;
    }

    public String[] getFilterCountries() {
        return _filterCountries;
    }

    public String[] getFilterTeams() {
        return _filterTeams;
    }

}
