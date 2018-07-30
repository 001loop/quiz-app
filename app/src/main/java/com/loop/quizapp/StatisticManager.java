package com.loop.quizapp;

import android.content.Context;

public class StatisticManager {

    static int _currentLevelRemoveLetters = 0;
    static int _currentLevelShowLetter = 0;
    Context _context;

    public StatisticManager(Context context) {
        _context = context;
        _currentLevelRemoveLetters = SaveManager.getStatCurrentLevelRemoveLetters(context);
        _currentLevelShowLetter = SaveManager.getStatCurrentLevelShowLetter(context);
    }

    public void removeLetters() {
        _currentLevelRemoveLetters++;
        SaveManager.setStatRemoveLetters(_context, _currentLevelRemoveLetters);
    }

    public void showLetter() {
        _currentLevelShowLetter++;
        SaveManager.setStatShowLetter(_context, _currentLevelShowLetter);
    }

    public void correctAnswer() {
        boolean isAnsweredWithoutHints = false;
        if (_currentLevelShowLetter == 0 && _currentLevelRemoveLetters == 0) {
            isAnsweredWithoutHints = true;
        }
        SaveManager.setStatLevelCompleted(_context, isAnsweredWithoutHints);
        _currentLevelRemoveLetters = 0;
        _currentLevelShowLetter = 0;
    }

    public int getRemoveLettersCount() {
        return SaveManager.getStatRemoveLettersCount(_context);
    }

    public int getShowLettersCount() {
        return SaveManager.getStatShowLetterCount(_context);
    }

    public int getNumOfAnswersWithoutHints() {
        return SaveManager.getStatAnswerWithoutHints(_context);
    }

    public boolean isCurrentLevelCompletedWithoutHints() {
        return (_currentLevelRemoveLetters == 0 && _currentLevelShowLetter == 0);
    }

    public void resetStatistic() {
        SaveManager.resetStatistic(_context);
    }

}
