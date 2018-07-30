package com.loop.quizapp;

import android.content.Context;

import java.text.NumberFormat;

public class LocaleTextHelper {

    public static String getLevelNumberText(Context context, int levelNumber) {
        return String.format(
                context.getResources().getString(R.string.text_level),
                getLocaleNumber(levelNumber));
    }

    public static String getLocaleNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String getLocaleNumber(long number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String getLocaleTimer(long seconds, long milliseconds) {
        return getLocaleNumber(seconds) + ":" + getLocaleNumber(milliseconds);
    }

}