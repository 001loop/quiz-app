package com.loop.quizapp;

import android.content.Context;
import android.content.SharedPreferences;

public class AchievementManager {

    final static String APP_PREFERENCES = "preferences";
    final static String EXPERT = "achievement_expert";
    final static String NEED_HELP = "achievement_need_help";
    final static String RICH_PLAYER = "achievement_rich_player";
    final static String GAME_PROGRESS = "achievement_complete_game";
    final static String GAME_PROGRESS_SYNC = "achievement_complete_game_synchronized";
    final static int LOCKED = -1;
    final static int UNLOCKED = 0;
    final static int SYNCHRONIZED = 1;

    public static void setDefaultAchievementPreferencesData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(EXPERT, LOCKED);
        editor.putInt(NEED_HELP, LOCKED);
        editor.putInt(RICH_PLAYER, LOCKED);
        editor.putInt(GAME_PROGRESS, 0);
        editor.putInt(GAME_PROGRESS_SYNC, UNLOCKED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static void unlockExpert(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(EXPERT, UNLOCKED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static void synchronizeExpert(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(EXPERT, SYNCHRONIZED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static int getExpert(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(EXPERT, LOCKED);
    }

    public static void unlockNeedHelp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(NEED_HELP, UNLOCKED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static void synchronizeNeedHelp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(NEED_HELP, SYNCHRONIZED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static int getNeedHelp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(NEED_HELP, LOCKED);
    }

    public static void unlockRichPlayer(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(RICH_PLAYER, UNLOCKED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static void synchronizeRichPlayer(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(RICH_PLAYER, SYNCHRONIZED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static int getRichPlayer(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(RICH_PLAYER, LOCKED);
    }

    public static void setGameProgress(Context context, int step) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(GAME_PROGRESS, step);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static int getGameProgress(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(GAME_PROGRESS, LOCKED);
    }

    public static void setGameProgressNotSynchronized(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(GAME_PROGRESS_SYNC, UNLOCKED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static void setGameProgressSynchronized(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(GAME_PROGRESS_SYNC, SYNCHRONIZED);
        if (editor.commit()) {
            saveMD5(context);
        }
    }

    public static int getGameProgressSynchronized(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(GAME_PROGRESS_SYNC, LOCKED);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean saveMD5(Context context) {
        return MD5Manager.savePreferencesMD5(context);
    }

}
