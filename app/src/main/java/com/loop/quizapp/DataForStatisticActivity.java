package com.loop.quizapp;

public class DataForStatisticActivity {
    private int _timeChallengeHighScore;
    private int _timeChallengeAverageScore;
    private int _timeChallengeGamesCount;
    private int _yesOrNoHighScore;
    private int _yesOrNoAverageScore;
    private int _yesOrNoGamesCount;
    private int _findThePairsHighScore;
    private int _findThePairsAverageScore;
    private int _findThePairsGamesCount;
    private int _numOfCompletedLevels;
    private int _numOfAnswersWithoutMistakes;
    private int _numOfAnswersWithoutHints;
    private int _numOfRemoveLettersHints;
    private int _numOfShowLetterHints;
    private boolean _isNeedSendDataToAnswers;
    private boolean _isVolumeOn;

    DataForStatisticActivity(int timeChallengeHighScore, int timeChallengeAverageScore,
                             int timeChallengeGamesCount, int yesOrNoHighScore,
                             int yesOrNoAverageScore, int yesOrNoGamesCount,
                             int findThePairsHighScore, int findThePairsAverageScore,
                             int findThePairsGamesCount,
                             int numOfCompletedLevels, int numOfAnswersWithoutMistakes,
                             int numOfAnswersWithoutHints, int numOfRemoveLettersHints,
                             int numOfShowLetterHints, boolean isNeedSendDataToAnswers,
                             boolean isVolumeOn) {
        _timeChallengeHighScore = timeChallengeHighScore;
        _timeChallengeAverageScore = timeChallengeAverageScore;
        _timeChallengeGamesCount = timeChallengeGamesCount;
        _yesOrNoHighScore = yesOrNoHighScore;
        _yesOrNoAverageScore = yesOrNoAverageScore;
        _yesOrNoGamesCount = yesOrNoGamesCount;
        _findThePairsHighScore = findThePairsHighScore;
        _findThePairsAverageScore = findThePairsAverageScore;
        _findThePairsGamesCount = findThePairsGamesCount;
        _numOfCompletedLevels = numOfCompletedLevels;
        _numOfAnswersWithoutMistakes = numOfAnswersWithoutMistakes;
        _numOfAnswersWithoutHints = numOfAnswersWithoutHints;
        _numOfRemoveLettersHints = numOfRemoveLettersHints;
        _numOfShowLetterHints = numOfShowLetterHints;
        _isNeedSendDataToAnswers = isNeedSendDataToAnswers;
        _isVolumeOn = isVolumeOn;
    }

    public int getTimeChallengeHighScore() {
        return _timeChallengeHighScore;
    }

    public int getTimeChallengeAverageScore() {
        return _timeChallengeAverageScore;
    }

    public int getTimeChallengeGamesCount() {
        return _timeChallengeGamesCount;
    }

    public int getYesOrNoHighScore() {
        return _yesOrNoHighScore;
    }

    public int getYesOrNoAverageScore() {
        return _yesOrNoAverageScore;
    }

    public int getYesOrNoGamesCount() {
        return _yesOrNoGamesCount;
    }

    public int getFindThePairsHighScore() {
        return _findThePairsHighScore;
    }

    public int getFindThePairsAverageScore() {
        return _findThePairsAverageScore;
    }

    public int getFindThePairsGamesCount() {
        return _findThePairsGamesCount;
    }

    public int getNumOfCompletedLevels() {
        return _numOfCompletedLevels;
    }

    public int getNumOfAnswersWithoutHints() {
        return _numOfAnswersWithoutHints;
    }

    public int getNumOfAnswersWithoutMistakes() {
        return _numOfAnswersWithoutMistakes;
    }

    public int getNumOfRemoveLettersHints() {
        return _numOfRemoveLettersHints;
    }

    public int getNumOfShowLetterHints() {
        return _numOfShowLetterHints;
    }

    public boolean getIsNeedSendDataToAnswers() {
        return _isNeedSendDataToAnswers;
    }

    public boolean getIsVolumeOn() {
        return _isVolumeOn;
    }

}