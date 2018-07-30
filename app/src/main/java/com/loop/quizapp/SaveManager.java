package com.loop.quizapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class SaveManager {

    private static final String APP_PREFERENCES = "preferences";
    private static final String LAST_VERSION = "last_version";
    private static final String LANGUAGE = "language";
    private static final String PRIVACY_POLICY_ACCEPTED = "pp_accepted";

    private static final String SYMBOLS = "symbols";
    private static final String VISIBILITY = "visibility";
    private static final String REMOVE_WORDS_AVAILABILITY = "remove_words";
    private static final String COINS = "coins";
    private static final String GAME_MODES_UNLOCKED = "game_modes_unlocked";
    private static final String ADVERTISEMENT_ENABLED = "advertisement_enabled";
    private static final String PERSONALIZED_ADVERTISEMENT_ENABLED = "personalized_advertisement_enabled";
    private static final String IS_PREMIUM_ACTIVE = "is_premium_active";
    private static final String SPECIAL_OFFER_ENABLED = "special_offer_enabled";
    private static final String SHARE_APP_REWARD_ENABLED = "share_app_reward_enabled";
    private static final String SHARE_APP_REWARD_ANIMATION_ENABLED = "share_app_reward_animation_enabled";
    private static final String CURRENT_LEVEL = "current_level";
    private static final String MAXIMUM_ACHIEVED_LEVEL = "maximum_achieved_level";
    private static final String TIME_CHALLENGE_HIGH_SCORE = "time_challenge_high_score";
    private static final String TIME_CHALLENGE_AVERAGE_SCORE = "time_challenge_average_score";
    private static final String TIME_CHALLENGE_GAMES_COUNT = "time_challenge_games_count";
    private static final String YES_OR_NO_HIGH_SCORE = "yes_or_no_high_score";
    private static final String YES_OR_NO_AVERAGE_SCORE = "yes_or_no_average_score";
    private static final String YES_OR_NO_GAMES_COUNT = "yes_or_no_games_count";
    private static final String FIND_THE_PAIRS_HIGH_SCORE = "find_the_pairs_high_score";
    private static final String FIND_THE_PAIRS_AVERAGE_SCORE = "find_the_pairs_average_score";
    private static final String FIND_THE_PAIRS_GAMES_COUNT = "find_the_pairs_games_count";
    private static final String STAT_CURRENT_LEVEL_WITHOUT_MISTAKES = "stat_current_level_without_mistakes";
    private static final String STAT_CURRENT_LEVEL_REMOVE_LETTERS = "stat_current_level_remove_letters";
    private static final String STAT_CURRENT_LEVEL_SHOW_LETTER = "stat_current_level_show_letter";
    private static final String STAT_REMOVE_LETTERS_COUNT = "stat_remove_letters_count";
    private static final String STAT_SHOW_LETTER_COUNT = "stat_show_letter_count";
    private static final String STAT_ANSWERS_WITHOUT_MISTAKES = "stat_answers_without_mistakes";
    private static final String STAT_ANSWERS_WITHOUT_HINTS = "stat_answers_without_hints";
    private static final String STAT_ANSWERS_NEED_TO_SEND = "stat_answers_need_to_send";
    private static final String VOLUME_ON = "volume_on";
    private static final String PLAY_GAMES_ENABLED = "play_games_enabled";

    private static final boolean FIRST_LAUNCH_VOLUME_ON = true;
    private static final boolean FIRST_LAUNCH_PLAY_GAMES_ENABLED = false;

    public static void resetGameData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(CURRENT_LEVEL, 0);
        editor.putString(VISIBILITY, null);
        editor.putString(SYMBOLS, null);
        editor.putBoolean(REMOVE_WORDS_AVAILABILITY, true);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static void setDefaultData(Context context, boolean saveCoins) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int numOfCoins;
        int maxAchievedLevel;
        int timeChallengeHighScore;
        int timeChallengeAverageScore;
        int timeChallengeGamesCount;
        int yesOrNoHighScore;
        int yesOrNoAverageScore;
        int yesOrNoGamesCount;
        int findThePairsHighScore;
        int findThePairsAverageScore;
        int findThePairsGamesCount;
        if (saveCoins) {
            numOfCoins = sp.getInt(COINS, ApplicationData.getFirstLaunchCoins());
            maxAchievedLevel = 0;
            timeChallengeHighScore = sp.getInt(TIME_CHALLENGE_HIGH_SCORE, 0);
            timeChallengeAverageScore = sp.getInt(TIME_CHALLENGE_AVERAGE_SCORE, 0);
            timeChallengeGamesCount = sp.getInt(TIME_CHALLENGE_GAMES_COUNT, 0);
            yesOrNoHighScore = sp.getInt(YES_OR_NO_HIGH_SCORE, 0);
            yesOrNoAverageScore = sp.getInt(YES_OR_NO_AVERAGE_SCORE, 0);
            yesOrNoGamesCount = sp.getInt(YES_OR_NO_GAMES_COUNT, 0);
            findThePairsHighScore = sp.getInt(FIND_THE_PAIRS_HIGH_SCORE, 0);
            findThePairsAverageScore = sp.getInt(FIND_THE_PAIRS_AVERAGE_SCORE, 0);
            findThePairsGamesCount = sp.getInt(FIND_THE_PAIRS_GAMES_COUNT, 0);
        } else {
            numOfCoins = ApplicationData.getFirstLaunchCoins();
            maxAchievedLevel = sp.getInt(MAXIMUM_ACHIEVED_LEVEL, 0);
            timeChallengeHighScore = 0;
            timeChallengeAverageScore = 0;
            timeChallengeGamesCount = 0;
            yesOrNoHighScore = 0;
            yesOrNoAverageScore = 0;
            yesOrNoGamesCount = 0;
            findThePairsHighScore = 0;
            findThePairsAverageScore = 0;
            findThePairsGamesCount = 0;
        }
        editor.putInt(COINS, numOfCoins);
        editor.putInt(MAXIMUM_ACHIEVED_LEVEL, maxAchievedLevel);
        editor.putInt(TIME_CHALLENGE_HIGH_SCORE, timeChallengeHighScore);
        editor.putInt(TIME_CHALLENGE_AVERAGE_SCORE, timeChallengeAverageScore);
        editor.putInt(TIME_CHALLENGE_GAMES_COUNT, timeChallengeGamesCount);
        editor.putInt(YES_OR_NO_HIGH_SCORE, yesOrNoHighScore);
        editor.putInt(YES_OR_NO_AVERAGE_SCORE, yesOrNoAverageScore);
        editor.putInt(YES_OR_NO_GAMES_COUNT, yesOrNoGamesCount);
        editor.putInt(FIND_THE_PAIRS_HIGH_SCORE, findThePairsHighScore);
        editor.putInt(FIND_THE_PAIRS_AVERAGE_SCORE, findThePairsAverageScore);
        editor.putInt(FIND_THE_PAIRS_GAMES_COUNT, findThePairsGamesCount);
        editor.putBoolean(STAT_ANSWERS_NEED_TO_SEND, false);
        editor.putInt(CURRENT_LEVEL, 0);
        editor.putString(VISIBILITY, null);
        editor.putString(SYMBOLS, null);
        editor.putBoolean(REMOVE_WORDS_AVAILABILITY, true);
        editor.putBoolean(GAME_MODES_UNLOCKED, false);
        editor.putBoolean(ADVERTISEMENT_ENABLED, true);
        editor.putBoolean(IS_PREMIUM_ACTIVE, false);
        editor.putBoolean(SPECIAL_OFFER_ENABLED, true);
        editor.putBoolean(SHARE_APP_REWARD_ENABLED, true);
        editor.putBoolean(SHARE_APP_REWARD_ANIMATION_ENABLED, false);
        editor.putBoolean(PLAY_GAMES_ENABLED, FIRST_LAUNCH_PLAY_GAMES_ENABLED);
        editor.putBoolean(VOLUME_ON, FIRST_LAUNCH_VOLUME_ON);
        updateLastVersion(context);
        if (editor.commit()) {
            savePreferences(context);
        }
        AchievementManager.setDefaultAchievementPreferencesData(context);
    }

    public static void savePreferences(Context context) {
        MD5Manager.savePreferencesMD5(context);
    }

    public static void setPrivacyPolicyAccepted(Context context, boolean isAccepted) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PRIVACY_POLICY_ACCEPTED, isAccepted);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static boolean getIsPrivacyPolicyAccepted(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(PRIVACY_POLICY_ACCEPTED, false);
    }

    public static DataForGameActivity getDataForGameActivity(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return new DataForGameActivity(
                sp.getBoolean(ADVERTISEMENT_ENABLED, true),
                sp.getBoolean(IS_PREMIUM_ACTIVE, false),
                sp.getInt(CURRENT_LEVEL, 0),
                sp.getInt(MAXIMUM_ACHIEVED_LEVEL, 0),
                sp.getBoolean(REMOVE_WORDS_AVAILABILITY, true),
                sp.getBoolean(SHARE_APP_REWARD_ENABLED, true),
                sp.getBoolean(SHARE_APP_REWARD_ANIMATION_ENABLED, false),
                sp.getBoolean(VOLUME_ON, false));
    }

    public static void saveDataFromGameActivity(Context context, int level, int maxAchievedLevel,
                                                Symbol[] symbols, boolean[] isVisible,
                                                boolean isRemoveWordsAvailable) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(CURRENT_LEVEL, level);
        editor.putInt(MAXIMUM_ACHIEVED_LEVEL, maxAchievedLevel);
        editor.putString(VISIBILITY, createVisibilityString(isVisible));
        editor.putString(SYMBOLS, createSymbolsString(symbols));
        editor.putBoolean(REMOVE_WORDS_AVAILABILITY, isRemoveWordsAvailable);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static DataForOtherGameModesActivity getDataForGameTimeChallengeActivity(
            Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return new DataForOtherGameModesActivity(
                sp.getBoolean(ADVERTISEMENT_ENABLED, true),
                sp.getBoolean(IS_PREMIUM_ACTIVE, false),
                sp.getInt(TIME_CHALLENGE_HIGH_SCORE, 0),
                sp.getBoolean(VOLUME_ON, false));
    }

    public static void saveGameTimeChallengeHighScore(Context context, int score) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(TIME_CHALLENGE_HIGH_SCORE, score);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static DataForOtherGameModesActivity getDataForGameYesOrNoActivity(
            Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return new DataForOtherGameModesActivity(
                sp.getBoolean(ADVERTISEMENT_ENABLED, true),
                sp.getBoolean(IS_PREMIUM_ACTIVE, false),
                sp.getInt(YES_OR_NO_HIGH_SCORE, 0),
                sp.getBoolean(VOLUME_ON, false));
    }

    public static DataForOtherGameModesActivity getDataForGameFindThePairsActivity(
            Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return new DataForOtherGameModesActivity(
                sp.getBoolean(ADVERTISEMENT_ENABLED, true),
                sp.getBoolean(IS_PREMIUM_ACTIVE, false),
                sp.getInt(FIND_THE_PAIRS_HIGH_SCORE, 0),
                sp.getBoolean(VOLUME_ON, false));
    }

    public static void saveGameYesOrNoHighScore(Context context, int score) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(YES_OR_NO_HIGH_SCORE, score);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void saveGameFindThePairsHighScore(Context context, int score) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(FIND_THE_PAIRS_HIGH_SCORE, score);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void saveGameTimeChallengeAverageScore(Context context, int score) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int averageScore = sp.getInt(TIME_CHALLENGE_AVERAGE_SCORE, 0);
        int gamesCount = sp.getInt(TIME_CHALLENGE_GAMES_COUNT, 0);
        int newAverageScore = (averageScore*gamesCount + score)/(gamesCount + 1);
        gamesCount++;
        editor.putInt(TIME_CHALLENGE_AVERAGE_SCORE, newAverageScore);
        editor.putInt(TIME_CHALLENGE_GAMES_COUNT, gamesCount);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void saveGameYesOrNoAverageScore(Context context, int score) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int averageScore = sp.getInt(YES_OR_NO_AVERAGE_SCORE, 0);
        int gamesCount = sp.getInt(YES_OR_NO_GAMES_COUNT, 0);
        int newAverageScore = (averageScore*gamesCount + score)/(gamesCount + 1);
        gamesCount++;
        editor.putInt(YES_OR_NO_AVERAGE_SCORE, newAverageScore);
        editor.putInt(YES_OR_NO_GAMES_COUNT, gamesCount);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void saveGameFindThePairsAverageScore(Context context, int score) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int averageScore = sp.getInt(FIND_THE_PAIRS_AVERAGE_SCORE, 0);
        int gamesCount = sp.getInt(FIND_THE_PAIRS_GAMES_COUNT, 0);
        int newAverageScore = (averageScore*gamesCount + score)/(gamesCount + 1);
        gamesCount++;
        editor.putInt(FIND_THE_PAIRS_AVERAGE_SCORE, newAverageScore);
        editor.putInt(FIND_THE_PAIRS_GAMES_COUNT, gamesCount);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static DataForStoreActivity getDataForStoreActivity(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return new DataForStoreActivity(
                sp.getBoolean(ADVERTISEMENT_ENABLED, true),
                sp.getBoolean(SPECIAL_OFFER_ENABLED, true),
                sp.getBoolean(IS_PREMIUM_ACTIVE, false),
                sp.getBoolean(VOLUME_ON, false));
    }

    public static DataForStatisticActivity getDataForStatisticActivity(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return new DataForStatisticActivity(
                sp.getInt(TIME_CHALLENGE_HIGH_SCORE, 0),
                sp.getInt(TIME_CHALLENGE_AVERAGE_SCORE, 0),
                sp.getInt(TIME_CHALLENGE_GAMES_COUNT, 0),
                sp.getInt(YES_OR_NO_HIGH_SCORE, 0),
                sp.getInt(YES_OR_NO_AVERAGE_SCORE, 0),
                sp.getInt(YES_OR_NO_GAMES_COUNT, 0),
                sp.getInt(FIND_THE_PAIRS_HIGH_SCORE, 0),
                sp.getInt(FIND_THE_PAIRS_AVERAGE_SCORE, 0),
                sp.getInt(FIND_THE_PAIRS_GAMES_COUNT, 0),
                sp.getInt(CURRENT_LEVEL, 0),
                sp.getInt(STAT_ANSWERS_WITHOUT_MISTAKES, 0),
                sp.getInt(STAT_ANSWERS_WITHOUT_HINTS, 0),
                sp.getInt(STAT_REMOVE_LETTERS_COUNT, 0),
                sp.getInt(STAT_SHOW_LETTER_COUNT, 0),
                sp.getBoolean(STAT_ANSWERS_NEED_TO_SEND, false),
                sp.getBoolean(VOLUME_ON, false));
    }

    public static void setStatAnswersNeedToSend(Context context, boolean isNeed) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(STAT_ANSWERS_NEED_TO_SEND, isNeed);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void changeCoinsNumber(Context context, int coins) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(COINS, sp.getInt(COINS, ApplicationData.getFirstLaunchCoins()) + coins);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static int getCoins(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(COINS, ApplicationData.getFirstLaunchCoins());
    }

    @SuppressWarnings("SameParameterValue")
    public static void setShareAppRewardEnabled(Context context, boolean shareAppRewardEnabled) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SHARE_APP_REWARD_ENABLED, shareAppRewardEnabled);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void setShareAppRewardAnimationEnabled(
            Context context ,boolean shareAppRewardAnimationEnabled) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SHARE_APP_REWARD_ANIMATION_ENABLED, shareAppRewardAnimationEnabled);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void setAdvertisementEnabled(Context context, boolean advertisementEnabled) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ADVERTISEMENT_ENABLED, advertisementEnabled);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void setPersonalizedAdvertisementEnabled(Context context, boolean personalizedAdvertisementEnabled) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PERSONALIZED_ADVERTISEMENT_ENABLED, personalizedAdvertisementEnabled);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static boolean getIsPersonalizedAdvertisementEnabled(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(PERSONALIZED_ADVERTISEMENT_ENABLED, true);
    }

    public static void setIsPremiumActive(Context context, boolean isPremiumActive) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_PREMIUM_ACTIVE, isPremiumActive);
        if (isPremiumActive) {
            editor.putBoolean(ADVERTISEMENT_ENABLED, false);
            editor.putBoolean(GAME_MODES_UNLOCKED, true);
        }
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static void setGameModesUnlocked(Context context, boolean isGameModesUnlocked) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(GAME_MODES_UNLOCKED, isGameModesUnlocked);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static boolean getIsGameModesUnlocked(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(GAME_MODES_UNLOCKED, false);
    }

    public static void setSpecialOfferEnabled(Context context, boolean specialOfferEnabled) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SPECIAL_OFFER_ENABLED, specialOfferEnabled);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static boolean getIsFirstLaunch(Context context) {
        return !isPreferencesFileExist(context);
    }

    @SuppressWarnings("unused")
    public static int getPreviousVersionCode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(LAST_VERSION, 0);
    }

    public static boolean getIsFirstLaunchAfterUpdate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        int currentVersionCode = BuildConfig.VERSION_CODE;
        return sp.getInt(LAST_VERSION, 0) != currentVersionCode;
    }

    public static void updateLastVersion(Context context) {
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(LAST_VERSION, currentVersionCode);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static String getLanguage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(LANGUAGE, null);
    }

    public static void saveLanguage(Context context, String languageCode) {
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LANGUAGE, languageCode);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static DataForOptionsActivity getDataForOptionsActivity(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return new DataForOptionsActivity(
                sp.getBoolean(VOLUME_ON, FIRST_LAUNCH_VOLUME_ON),
                sp.getBoolean(PLAY_GAMES_ENABLED, FIRST_LAUNCH_PLAY_GAMES_ENABLED));
    }

    public static int getCurrentLevel(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(CURRENT_LEVEL, 0);
    }

    public static int getMaximumAchievedLevel(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(MAXIMUM_ACHIEVED_LEVEL, 0);
    }

    public static void setPlayGamesEnabled(Context context, boolean playGamesEnabled) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PLAY_GAMES_ENABLED, playGamesEnabled);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static boolean getPlayGamesEnabled(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(PLAY_GAMES_ENABLED, FIRST_LAUNCH_PLAY_GAMES_ENABLED);
    }

    public static void setVolumeOn(Context context, boolean volumeOn) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(VOLUME_ON, volumeOn);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static boolean getVolumeOn(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(VOLUME_ON, false);
    }

    public static Symbol[] getSymbols(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (sp.contains(SYMBOLS)) {
            return getSymbolsArray(sp.getString(SYMBOLS, null));
        }
        return null;
    }

    public static boolean[] getVisibility(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (sp.contains(VISIBILITY)) {
            return getVisibilityArray(sp.getString(VISIBILITY, null));
        }
        return null;
    }

    public static boolean isPreferencesFileExist(Context context) {
        String preferencesFileDirectory = "shared_prefs/" + APP_PREFERENCES + ".xml";
        File preferencesFile = new File(context.getApplicationInfo().dataDir, preferencesFileDirectory);
        return preferencesFile.exists();
    }

    public static boolean getStatCurrentLevelWithoutMistakes(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(STAT_CURRENT_LEVEL_WITHOUT_MISTAKES, true);
    }

    public static void setStatCurrentLevelWithoutMistakes(Context context, boolean isWithoutMistakes) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(STAT_CURRENT_LEVEL_WITHOUT_MISTAKES, isWithoutMistakes);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static int getStatCurrentLevelRemoveLetters(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(STAT_CURRENT_LEVEL_REMOVE_LETTERS, 0);
    }

    public static int getStatCurrentLevelShowLetter(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(STAT_CURRENT_LEVEL_SHOW_LETTER, 0);
    }

    public static void setStatShowLetter(Context context, int count) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(STAT_CURRENT_LEVEL_SHOW_LETTER, count);
        editor.putInt(STAT_SHOW_LETTER_COUNT, getStatShowLetterCount(context) + 1);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void setStatRemoveLetters(Context context, int count) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(STAT_CURRENT_LEVEL_REMOVE_LETTERS, count);
        editor.putInt(STAT_REMOVE_LETTERS_COUNT, getStatRemoveLettersCount(context) + 1);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void setStatAnswersWithoutMistakes(Context context, int count) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(STAT_ANSWERS_WITHOUT_MISTAKES, count);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void setStatLevelCompleted(Context context, boolean isAnsweredWithoutHints) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(STAT_CURRENT_LEVEL_REMOVE_LETTERS, 0);
        editor.putInt(STAT_CURRENT_LEVEL_SHOW_LETTER, 0);
        editor.putBoolean(STAT_CURRENT_LEVEL_WITHOUT_MISTAKES, true);
        if (isAnsweredWithoutHints) {
            editor.putInt(STAT_ANSWERS_WITHOUT_HINTS, getStatAnswerWithoutHints(context) + 1);
        }
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static int getStatRemoveLettersCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(STAT_REMOVE_LETTERS_COUNT, 0);
    }

    public static int getStatShowLetterCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(STAT_SHOW_LETTER_COUNT, 0);
    }

    public static int getStatAnswersWithoutMistakes(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(STAT_ANSWERS_WITHOUT_MISTAKES, 0);
    }

    public static int getStatAnswerWithoutHints(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(STAT_ANSWERS_WITHOUT_HINTS, 0);
    }

    public static void resetStatistic(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(STAT_ANSWERS_WITHOUT_MISTAKES, 0);
        editor.putInt(STAT_ANSWERS_WITHOUT_HINTS, 0);
        editor.putInt(STAT_CURRENT_LEVEL_SHOW_LETTER, 0);
        editor.putInt(STAT_CURRENT_LEVEL_REMOVE_LETTERS, 0);
        editor.putBoolean(STAT_CURRENT_LEVEL_WITHOUT_MISTAKES, true);
        editor.putInt(STAT_REMOVE_LETTERS_COUNT, 0);
        editor.putInt(STAT_SHOW_LETTER_COUNT, 0);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    public static void resetLevelData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(VISIBILITY, null);
        editor.putString(SYMBOLS, null);
        editor.putBoolean(REMOVE_WORDS_AVAILABILITY, true);
        editor.putBoolean(STAT_CURRENT_LEVEL_WITHOUT_MISTAKES, true);
        editor.putInt(STAT_CURRENT_LEVEL_REMOVE_LETTERS, 0);
        editor.putInt(STAT_CURRENT_LEVEL_SHOW_LETTER, 0);
        if (editor.commit()) {
            savePreferences(context);
        }
    }

    private static String createVisibilityString(boolean[] isVisible) {
        if (isVisible == null) {
            return null;
        }
        StringBuilder visibilityString = new StringBuilder();
        int arraySize = isVisible.length;
        for (int x = 0; x < arraySize; x ++) {
            if (isVisible[x]) {
                visibilityString.append("1");
            } else {
                visibilityString.append("0");
            }
            if (x != arraySize - 1) {
                visibilityString.append(";");
            }
        }
        return visibilityString.toString();
    }

    private static boolean[] getVisibilityArray(String visibilityString) {
        if (visibilityString == null) {
            return null;
        }
        String[] visibilityElements = visibilityString.split(";");
        int numOfElements = visibilityElements.length;
        if (numOfElements == 0) {
            return null;
        }
        boolean[] visibilityArray = new boolean[numOfElements];
        for (int x = 0; x < numOfElements; x++) {
            visibilityArray[x] = visibilityElements[x].equals("1");
        }
        return visibilityArray;
    }

    private static String createSymbolsString(Symbol[] symbols) {
        if (symbols == null) {
            return null;
        }
        StringBuilder symbolsString = new StringBuilder();
        int arraySize = symbols.length;
        for (int x = 0; x < arraySize; x ++) {
            Symbol currentSymbol = symbols[x];
            if (currentSymbol.getIsLocked()) {
                symbolsString.append("1");
            } else {
                symbolsString.append("0");
            }
            symbolsString.append(",");
            symbolsString.append(currentSymbol.getCharacter());
            symbolsString.append(",");
            symbolsString.append(currentSymbol.getPosition());
            if (x != arraySize - 1) {
                symbolsString.append(";");
            }
        }
        return symbolsString.toString();
    }

    private static Symbol[] getSymbolsArray(String symbolsString) {
        if (symbolsString == null) {
            return null;
        }
        String[] symbolElements = symbolsString.split(";");
        int numOfElements = symbolElements.length;
        if (numOfElements == 0) {
            return null;
        }
        Symbol[] symbolsArray = new Symbol[numOfElements];
        for (int x = 0; x < numOfElements; x++) {
            String[] currentSymbolData = symbolElements[x].split(",");
            boolean locked = false;
            char character = " ".charAt(0);
            int position = -1;
            if (currentSymbolData.length == 3) {
                locked = currentSymbolData[0].equals("1");
                character = currentSymbolData[1].charAt(0);
                try {
                    position = Integer.parseInt(currentSymbolData[2]);
                } catch (NumberFormatException ignored) {

                }
            }
            symbolsArray[x] = new Symbol(locked, character, position);
        }
        return symbolsArray;
    }

}
