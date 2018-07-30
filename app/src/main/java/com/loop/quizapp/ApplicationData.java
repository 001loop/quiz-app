package com.loop.quizapp;

public class ApplicationData {
    private static final int firstLaunchCoins = 1500;
    private static final int watchVideoReward = 35;             // shown to the user in text view
    private static final int watchInterstitialAdvertReward = 20;
    private static final int shareAppReward = 50;
    private static final int correctAnswerReward = 2;
    private static final int minimumRequiredLevelForTimeChallengeCorrectAnswerMark = 1;
    // (234) num of levels (from 1 to last) last id in assets
    // game mods
    private static final int minimumRequiredLevelForOtherGameMods = 1; // shown to the user
    private static final int unlockOtherGameModsLevelCost = 10;         // shown to the user
    private static final int gameModsBoostCoins = 8;       // shown to the user in text view
    // time challenge
    private static final int timeChallengeMaxNumOfLevels = 20;
    private static final int timeChallengeCorrectAnswerPoints = 100;
    private static final int timeChallengeTime = 35000;
    private static final int timeChallengeBoostTime = 40000;
    private static final int timeChallengeMinTimeForAnswerWithSpeedBonus = 1000;
    private static final int timeChallengeMaxTimeForAnswerWithSpeedBonus = 2500;
    private static final float timeChallengeMaxSpeedBonusCoefficient = 2.0f;
    private static final int timeChallengeMaxRemainingTimeBonus = 10000;
    private static final float timeChallengeMaxRemainingTimeCoefficient = 1.25f;
    // yes or no
    private static final int yesOrNoMaxNumOfLevels = 25;
    private static final int yesOrNoCorrectAnswerPoints = 80;
    private static final int yesOrNoTime = 27000;
    private static final int yesOrNoBoostTime = 30000;
    private static final int yesOrNoMinTimeForAnswerWithSpeedBonus = 750;
    private static final int yesOrNoMaxTimeForAnswerWithSpeedBonus = 2000;
    private static final float yesOrNoMaxSpeedBonusCoefficient = 2.5f;
    // find the pairs
    private static final int findThePairsNumOfLevels = 20;
    private static final int findThePairsCorrectAnswerPoints = 125;
    private static final int findThePairsTime = 80000;
    private static final int findThePairsBoostTime = 85000;
    private static final int findThePairsRemainingTimeBonus = 50000;
    private static final int findThePairsRemainingMsPerPoint = 20;
    private static final int findThePairsPenaltyMs = 1500;
    private static final int tutorialMinNumOfLevels = 6;
    // other
    private static final int numOfGameProgressSteps = 10;
    private static final int coinsAmountSpecialOffer = 125;
    private static final int coinsAmountWithAdvertEnabled = 300;
    private static final int coinsAmountSmallNum = 200;
    private static final int coinsAmountMediumNum = 400;
    private static final int coinsAmountLargeNum = 1000;
    private static final int rateUsRequestLevel = 33;
    private static final int fullScreenAdvertFrequency = 7;
    private static final int fullScreenAdvertFrequencyOtherGameMods = 3;
    private static final int removeLettersCost = 25;                    // indicated in image view
    private static final int showLetterCost = 10;                       // indicated in image view
    private static final int achievementRichPlayerCoinsNumber = 500;
    private static final int achievementNeedHelpHintsNumber = 100;
    private static final int achievementExpertLevelNumber = 75;
    private static final String rsaAppKey = "APP_KEY";

    public static int getFirstLaunchCoins() {
        return firstLaunchCoins;
    }

    public static int getWatchVideoReward() {
        return watchVideoReward;
    }

    public static int getWatchInterstitialAdvertReward() {
        return watchInterstitialAdvertReward;
    }

    public static int getShareAppReward() {
        return shareAppReward;
    }

    public static int getCorrectAnswerReward() {
        return correctAnswerReward;
    }

    public static int getMinimumRequiredLevelForOtherGameMods() {
        return minimumRequiredLevelForOtherGameMods;
    }

    public static int getUnlockOtherGameModsLevelCost() {
        return unlockOtherGameModsLevelCost;
    }

    public static int getMinimumRequiredLevelForTimeChallengeCorrectAnswerMark() {
        return minimumRequiredLevelForTimeChallengeCorrectAnswerMark;
    }

    public static int getTimeChallengeMaxNumOfLevels() {
        return timeChallengeMaxNumOfLevels;
    }

    public static int getYesOrNoMaxNumOfLevels() {
        return yesOrNoMaxNumOfLevels;
    }

    public static int getTimeChallengeCorrectAnswerPoints() {
        return timeChallengeCorrectAnswerPoints;
    }

    public static int getYesOrNoCorrectAnswerPoints() {
        return yesOrNoCorrectAnswerPoints;
    }

    public static int getYesOrNoTime() {
        return yesOrNoTime;
    }

    public static int getYesOrNoBoostTime() {
        return yesOrNoBoostTime;
    }

    public static int getTimeChallengeTime() {
        return timeChallengeTime;
    }

    public static int getTimeChallengeBoostTime() {
        return timeChallengeBoostTime;
    }

    public static int getGameModsBoostCoins() {
        return gameModsBoostCoins;
    }

    public static int getTimeChallengeMinTimeForAnswerWithSpeedBonus() {
        return timeChallengeMinTimeForAnswerWithSpeedBonus;
    }

    public static int getTimeChallengeMaxTimeForAnswerWithSpeedBonus() {
        return timeChallengeMaxTimeForAnswerWithSpeedBonus;
    }

    public static int getYesOrNoMinTimeForAnswerWithSpeedBonus() {
        return yesOrNoMinTimeForAnswerWithSpeedBonus;
    }

    public static int getYesOrNoMaxTimeForAnswerWithSpeedBonus() {
        return yesOrNoMaxTimeForAnswerWithSpeedBonus;
    }

    public static int getTimeChallengeMaxRemainingTimeBonus() {
        return timeChallengeMaxRemainingTimeBonus;
    }

    public static float getTimeChallengeMaxSpeedBonusCoefficient() {
        return timeChallengeMaxSpeedBonusCoefficient;
    }

    public static float getYesOrNoMaxSpeedBonusCoefficient() {
        return yesOrNoMaxSpeedBonusCoefficient;
    }

    public static float getTimeChallengeMaxRemainingTimeCoefficient() {
        return timeChallengeMaxRemainingTimeCoefficient;
    }

    public static int getFindThePairsNumOfLevels() {
        return findThePairsNumOfLevels;
    }

    public static int getFindThePairsCorrectAnswerPoints() {
        return findThePairsCorrectAnswerPoints;
    }

    public static int getFindThePairsTime() {
        return findThePairsTime;
    }

    public static int getFindThePairsBoostTime() {
        return findThePairsBoostTime;
    }

    public static int getFindThePairsRemainingTimeBonus() {
        return findThePairsRemainingTimeBonus;
    }

    public static int getFindThePairsRemainingMsPerPoint() {
        return findThePairsRemainingMsPerPoint;
    }

    public static int getFindThePairsPenaltyMs() {
        return findThePairsPenaltyMs;
    }

    public static int getTutorialMinNumOfLevels() {
        return tutorialMinNumOfLevels;
    }

    public static int getNumOfGameProgressSteps() {
        return numOfGameProgressSteps;
    }

    public static int getCoinsAmountSpecialOffer() {
        return coinsAmountSpecialOffer;
    }

    public static int getCoinsAmountWithAdvertEnabled() {
        return coinsAmountWithAdvertEnabled;
    }

    public static int getCoinsAmountSmallNum() {
        return coinsAmountSmallNum;
    }

    public static int getCoinsAmountMediumNum() {
        return coinsAmountMediumNum;
    }

    public static int getCoinsAmountLargeNum() {
        return coinsAmountLargeNum;
    }

    public static int getRateUsRequestLevel() {
        return rateUsRequestLevel;
    }

    public static int getFullScreenAdvertFrequency() {
        return fullScreenAdvertFrequency;
    }

    public static int getFullScreenAdvertFrequencyOtherGameMods() {
        return fullScreenAdvertFrequencyOtherGameMods;
    }

    public static int getRemoveLettersCost() {
        return removeLettersCost;
    }

    public static int getShowLetterCost() {
        return showLetterCost;
    }

    public static int getAchievementRichPlayerCoinsNumber() {
        return achievementRichPlayerCoinsNumber;
    }

    public static int getAchievementNeedHelpHintsNumber() {
        return achievementNeedHelpHintsNumber;
    }

    public static int getAchievementExpertLevelNumber() {
        return achievementExpertLevelNumber;
    }

    public static int getTimeChallengeLeaderboardMaxScore() {
        return (int)(timeChallengeMaxNumOfLevels * timeChallengeCorrectAnswerPoints *
                timeChallengeMaxSpeedBonusCoefficient * timeChallengeMaxRemainingTimeCoefficient);
    }

    public static int getYesOrNoLeaderboardMaxScore() {
        return (int)(yesOrNoMaxNumOfLevels * yesOrNoCorrectAnswerPoints *
                yesOrNoMaxSpeedBonusCoefficient);
    }

    public static int getFindThePairsLeaderboardMaxScore() {
        return (findThePairsNumOfLevels * findThePairsCorrectAnswerPoints) +
                (findThePairsRemainingTimeBonus / findThePairsRemainingMsPerPoint);
    }

    public static String getRsaAppKey() {
        return rsaAppKey;
    }

}
