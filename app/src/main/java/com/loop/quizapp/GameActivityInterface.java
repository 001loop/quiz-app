package com.loop.quizapp;

public interface GameActivityInterface {
    void sendDataFromCharsFragment(Symbol symbol);
    void sendDataFromAnswerFragment(Symbol symbol);
    Symbol getSymbolFromCharsFragment(String character);
    void swapTwoSymbols(Symbol fromWordsToAnswer, Symbol fromAnswerToWords, int indexForLock);
    void sendCorrectAnswerMessage();
    void sendWrongAnswerMessage();
    void closeLevelInfoFragment();
}
