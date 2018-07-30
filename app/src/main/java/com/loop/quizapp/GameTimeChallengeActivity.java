package com.loop.quizapp;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GameTimeChallengeActivity extends AppCompatActivity {

    Context context;

    ImageView btnBack;
    ImageView imageViewClock;
    TextView textViewTime;
    LinearLayout quizContainer;
    LinearLayout quizContainerFake;
    LinearLayout buttonsContainer;
    CountDownTimer countDownTimer;
    RelativeLayout mainLayout;
    FrameLayout fragmentLevelContainer;
    ArrayList<Button> answersButtons;
    ProgressBar progressBarTime;
    TextView textViewCurrentPoints;
    TextView textViewAnswersCount;

    boolean isEndGameFragmentClosed = true;
    int currentQuiz;
    int score;
    Date startGameLocalTime;
    int correctButtonNumber;
    long remainingTime;
    long startQuestionTime;
    boolean isTimerEndSoundEnable;
    int correctAnswersCount = 0;

    int retryCount;
    int MAX_NUM_OF_LEVELS;
    int NUM_OF_INCORRECT_ANSWERS = 3;
    int CORRECT_ANSWER_POINTS;
    int TIME;
    int AFTER_ANSWER_TIME = 600;
    int MIN_TIME_FOR_ANSWER;
    int MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS;
    float MAX_SPEED_BONUS_COEFFICIENT;
    int MAX_REMAINING_TIME_BONUS;
    float MAX_REMAINING_TIME_COEFFICIENT;
    int INTERVAL = 10;
    int END_SOUND_DURATION = 2000;
    int tickCount;
    float imageClockAlpha = 0.2f;

    Animation animationScale;

    boolean isBackButtonsClickable;
    boolean playGamesEnabled;
    boolean isAdvertisementEnabled = true;
    boolean isPremiumActive = false;
    int highScore;
    int lastHighScore;
    boolean isVolumeOn;
    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    int soundIdButtonClick;
    int soundIdWrongAnswer;
    int soundIdCorrectAnswer;
    int soundIdHighScore;
    int soundTimerTick;
    int soundTimerEnd;
    int soundIdProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        viewsInitialization();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onResume() {
        super.onResume();
        LaunchManager.loadLocale(context);
        getClearMainLayout();
        loadData();
        LaunchManager.checkDataValid(context);
        loadSounds();
        viewsInitialization();
        if (isEndGameFragmentClosed) {
            createAreYouReadyFragment();
        } else {
            showEndGameFragment(false);
        }
    }

    private void loadData() {
        retryCount = 0;
        DataForOtherGameModesActivity data = SaveManager.getDataForGameTimeChallengeActivity(context);
        isAdvertisementEnabled = data.getAdvertisementEnabled();
        isPremiumActive = data.getIsPremiumActive();
        isVolumeOn = data.getIsVolumeOn();
        highScore = data.getHighScore();
        lastHighScore = highScore;
        MAX_NUM_OF_LEVELS = ApplicationData.getTimeChallengeMaxNumOfLevels();
        CORRECT_ANSWER_POINTS = ApplicationData.getTimeChallengeCorrectAnswerPoints();
        MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS = ApplicationData.getTimeChallengeMaxTimeForAnswerWithSpeedBonus();
        MIN_TIME_FOR_ANSWER = ApplicationData.getTimeChallengeMinTimeForAnswerWithSpeedBonus();
        MAX_REMAINING_TIME_BONUS = ApplicationData.getTimeChallengeMaxRemainingTimeBonus();
        MAX_SPEED_BONUS_COEFFICIENT = ApplicationData.getTimeChallengeMaxSpeedBonusCoefficient();
        MAX_REMAINING_TIME_COEFFICIENT = ApplicationData.getTimeChallengeMaxRemainingTimeCoefficient();
    }

    private void viewsInitialization() {
        setContentView(R.layout.activity_game_time_challenge);
        mainLayout = findViewById(R.id.main_layout);
        textViewTime = findViewById(R.id.text_view_time);
        textViewTime.setVisibility(View.INVISIBLE);
        quizContainer = findViewById(R.id.quiz_container);
        quizContainerFake = findViewById(R.id.quiz_container_fake);
        buttonsContainer = findViewById(R.id.buttons_container);
        answersButtons = new ArrayList<>();
        answersButtons.add((Button) findViewById(R.id.btn_answer_1));
        answersButtons.add((Button) findViewById(R.id.btn_answer_2));
        answersButtons.add((Button) findViewById(R.id.btn_answer_3));
        answersButtons.add((Button) findViewById(R.id.btn_answer_4));
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
        ButtonBack.SetOnTouchListenerAndColorFilter(context, btnBack);
        isBackButtonsClickable = true;
        imageViewClock = findViewById(R.id.image_view_clock);
        imageViewClock.setColorFilter(getResources().getColor(R.color.gameModesClock), PorterDuff.Mode.SRC_ATOP);
        imageViewClock.setVisibility(View.INVISIBLE);
        progressBarTime = findViewById(R.id.progress_bar_time);
        textViewCurrentPoints = findViewById(R.id.text_view_current_points);
        textViewAnswersCount = findViewById(R.id.text_view_answers_count);
        animationScale = AnimationUtils.loadAnimation(getApplication(),
                R.anim.hint_btns_blinking);
        initializeMessageContainer();
    }

    private void startTimer(boolean isBonusActive) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (isBonusActive) {
            TIME = ApplicationData.getTimeChallengeBoostTime();
        } else {
            TIME = ApplicationData.getTimeChallengeTime();
        }
        progressBarTime.setMax(TIME);
        textViewTime.setVisibility(View.VISIBLE);
        startGameLocalTime = Calendar.getInstance().getTime();
        countDownTimer = new CountDownTimer(TIME, INTERVAL) {
            @SuppressWarnings("ConstantConditions")
            public void onTick(long millisUntilFinished) {
                if (tickCount == 0) {
                    timerTick();
                }
                int tickInterval = 100;
                if (millisUntilFinished > 35000) {
                    tickInterval = 100;
                } else if (millisUntilFinished <= 35000 && millisUntilFinished > 30000) {
                    tickInterval = 90;
                } else if (millisUntilFinished <= 30000 && millisUntilFinished > 26000) {
                    tickInterval = 80;
                } else if (millisUntilFinished <= 26000 && millisUntilFinished > 22000) {
                    tickInterval = 70;
                } else if (millisUntilFinished <= 22000 && millisUntilFinished > 18000) {
                    tickInterval = 60;
                } else if (millisUntilFinished <= 18000 && millisUntilFinished > 14000) {
                    tickInterval = 50;
                } else if (millisUntilFinished <= 14000 && millisUntilFinished > 10000) {
                    tickInterval = 40;
                } else if (millisUntilFinished <= 10000 && millisUntilFinished > 7000) {
                    tickInterval = 30;
                } else if (millisUntilFinished <= 7000 && millisUntilFinished > 4000) {
                    tickInterval = 20;
                } else if (millisUntilFinished <= 4000 && millisUntilFinished > END_SOUND_DURATION) {
                    tickInterval = 15;
                } else if (millisUntilFinished < END_SOUND_DURATION) {
                    tickInterval = 10000;
                    timerEnd();
                    isTimerEndSoundEnable = false;
                }
                tickCount++;
                if (tickCount >= tickInterval) {
                    tickCount = 0;
                }
                remainingTime = millisUntilFinished;
                textViewTime.setText(LocaleTextHelper.getLocaleTimer(
                        millisUntilFinished / 1000,
                        (millisUntilFinished / 100) % 10));
                int n = ((int)millisUntilFinished * 100)/TIME;
                int red = (255 * (100 - n)) / 100 ;
                int green = (255 * n) / 100;
                progressBarTime.getProgressDrawable().setColorFilter(
                        android.graphics.Color.rgb(red, green, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
                progressBarTime.setProgress(TIME - (int)millisUntilFinished);
            }
            public void onFinish() {
                timerFinish();
            }
        }.start();
    }

    private void updateAnswerInfo(int points) {
        String stringAnswersCount = "X" + LocaleTextHelper.getLocaleNumber(correctAnswersCount);
        textViewAnswersCount.setText(stringAnswersCount);
        String stringPoints = getResources().getString(R.string.score) + LocaleTextHelper.getLocaleNumber(points);
        textViewCurrentPoints.setText(stringPoints);
        textViewCurrentPoints.startAnimation(animationScale);
    }

    private void timerFinish() {
        textViewTime.setText(LocaleTextHelper.getLocaleTimer(
                0,
                0));
        endGame();
    }

    private void timerTick() {
        textViewTime.setVisibility(View.VISIBLE);
        imageViewClock.setVisibility(View.VISIBLE);
        int TICK_DURATION = 250;
        Animation animationTick = new AlphaAnimation(imageClockAlpha, 1.0f);
        animationTick.setRepeatMode(Animation.REVERSE);
        animationTick.setFillAfter(false);
        animationTick.setDuration(TICK_DURATION);
        playSound(soundTimerTick);
        imageViewClock.startAnimation(animationTick);
    }

    private void timerEnd() {
        if (isTimerEndSoundEnable) {
            textViewTime.setVisibility(View.VISIBLE);
            imageViewClock.setVisibility(View.VISIBLE);
            playSound(soundTimerEnd);
        }
    }

    private ArrayList<DataOtherGameModes> generateQuestions() {
        LevelManagerGameModes levels = new LevelManagerGameModes(context);
        int numOfLevels = levels.getNumOfLevels();
        int numOfRequiredLevels;
        if (numOfLevels > MAX_NUM_OF_LEVELS) {
            numOfRequiredLevels = MAX_NUM_OF_LEVELS;
        } else {
            numOfRequiredLevels = numOfLevels;
        }
        ArrayList<Integer> answersNumbers = getRandomNumbersArray(numOfRequiredLevels, numOfLevels);
        ArrayList<DataOtherGameModes> dataOtherGameModesData = new ArrayList<>();
        for (int x = 0; x < numOfRequiredLevels; x++) {
            // create level
            ArrayList<Integer> excludedNumbers = new ArrayList<>();
            excludedNumbers.add(answersNumbers.get(x));
            for (int i = 0; i < NUM_OF_INCORRECT_ANSWERS; i++) {
                // add 3 incorrect answers
                int currentNumber;
                ArrayList<Integer> excludedAnswers = new ArrayList<>();
                if (x > 0) {
                    for (int a = 0; a < x; a++) {
                        excludedAnswers.add(answersNumbers.get(a));
                    }
                }
                currentNumber = getRandomNumber(numOfLevels, excludedNumbers, excludedAnswers);
                excludedNumbers.add(currentNumber);
            }
            ArrayList<String> answers = new ArrayList<>();
            for (int i = 0; i < excludedNumbers.size(); i++) {
                answers.add(levels.getLevel(excludedNumbers.get(i)).getName());
            }
            dataOtherGameModesData.add(new DataOtherGameModes(
                    levels.getLevel(excludedNumbers.get(0)).getImage(),
                    answers));
        }
        return dataOtherGameModesData;
    }

    private int getRandomNumber(int maxNumber, ArrayList<Integer> excludedNumbers,
                                ArrayList<Integer> correctAnswersNumbers) {
        Random randomGenerator = new Random();
        int random;
        if (maxNumber <= NUM_OF_INCORRECT_ANSWERS) {
            return randomGenerator.nextInt(maxNumber);
        }
        if (maxNumber <= (excludedNumbers.size() + correctAnswersNumbers.size())) {
            do {
                random = randomGenerator.nextInt(maxNumber);
            } while (excludedNumbers.contains(random));
        } else {
            do {
                random = randomGenerator.nextInt(maxNumber);
            } while (excludedNumbers.contains(random) || correctAnswersNumbers.contains(random));
        }
        return random;
    }

    @SuppressWarnings("deprecation")
    private void displayQuiz(final ArrayList<DataOtherGameModes> levels, DataOtherGameModes question) {
        Animation animationDisappearance = AnimationUtils.loadAnimation(getApplication(),
                R.anim.disappearance_left_to_right);
        Animation animationAppearance = AnimationUtils.loadAnimation(getApplication(),
                R.anim.appearance_left_to_right);
        final Animation click_answer_btn = AnimationUtils.loadAnimation(this, R.anim.click_on_answer_btn);
        LoadDrawablesManager loadDrawablesManager = new LoadDrawablesManager(getApplicationContext());
        Drawable drawable = loadDrawablesManager.getDrawable(
                "quiz_images/" + question.getImageName() + ".png",
                1.0f, 1.0f);
        final ImageView imageViewQuiz = new ImageView(this);
        final ImageView imageViewQuizFake = new ImageView(this);
        if (drawable != null) {
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
            );
            imageParams.gravity = Gravity.CENTER;
            imageViewQuiz.setLayoutParams(imageParams);
            imageViewQuizFake.setLayoutParams(imageParams);
            imageViewQuiz.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewQuizFake.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewQuiz.setAdjustViewBounds(true);
            imageViewQuizFake.setAdjustViewBounds(true);
            imageViewQuiz.setImageDrawable(drawable);
            imageViewQuizFake.setImageDrawable(drawable);
            quizContainer.removeAllViews();
            quizContainer.addView(imageViewQuiz);
        }
        animationAppearance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                quizContainerFake.removeAllViews();
                quizContainerFake.addView(imageViewQuizFake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        quizContainerFake.startAnimation(animationDisappearance);
        quizContainer.startAnimation(animationAppearance);
        final ArrayList<Integer> randomizeButtons = getRandomNumbersArray(4,4);
        for (int i = 0; i < randomizeButtons.size(); i++) {
            final int buttonNumber = randomizeButtons.get(i);
            final boolean isCorrect;
            final String buttonText = question.getAnswers().get(i);
            final Button currentButton = answersButtons.get(buttonNumber);
            currentButton.setText(buttonText);
            if (i != 0) {
                isCorrect = false;
            } else {
                correctButtonNumber = buttonNumber;
                isCorrect = true;
            }
            currentButton.setClickable(true);
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(click_answer_btn);
                    nextLevel(levels, isCorrect, correctButtonNumber, buttonNumber);
                }
            });
        }
        if (remainingTime == -1) {
            startQuestionTime = TIME;
        } else {
            startQuestionTime = remainingTime;
        }
    }

    @SuppressWarnings("all")
    private void nextLevel(final ArrayList<DataOtherGameModes> levels, boolean isCorrect,
                           final int correctButton, final int pressedNumber) {
        for (int i = 0; i < answersButtons.size(); i++) {
            answersButtons.get(i).setClickable(false);
        }
        if (isCorrect) {
            correctAnswersCount++;
            int answerTime = (int)(startQuestionTime - remainingTime);
            float speedCoefficient = 1.0f;
            if (answerTime < MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS) {
                float tickSpeedCoefficient =
                        (MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS - MIN_TIME_FOR_ANSWER)/((MAX_SPEED_BONUS_COEFFICIENT - 1)*100);
                speedCoefficient =
                        ((((MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS - MIN_TIME_FOR_ANSWER) - (answerTime - MIN_TIME_FOR_ANSWER))/tickSpeedCoefficient)/100) + 1;
            }
            if (speedCoefficient > MAX_SPEED_BONUS_COEFFICIENT) {
                speedCoefficient = MAX_SPEED_BONUS_COEFFICIENT;
            }
            int answerScore = (int)(CORRECT_ANSWER_POINTS * speedCoefficient);
            score = score + answerScore;
            updateAnswerInfo(score);
            GradientDrawable btnBackgroundCorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {getResources().getColor(R.color.gameModesCorrectButtonEnd),
                            getResources().getColor(R.color.gameModesCorrectButtonStart)});
            btnBackgroundCorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
            answersButtons.get(correctButton).setBackgroundDrawable(btnBackgroundCorrect);
            playSound(soundIdCorrectAnswer);
        } else {
            GradientDrawable btnBackgroundCorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {getResources().getColor(R.color.gameModesCorrectButtonEnd),
                            getResources().getColor(R.color.gameModesCorrectButtonStart)});
            GradientDrawable btnBackgroundIncorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {getResources().getColor(R.color.gameModesIncorrectButtonEnd),
                            getResources().getColor(R.color.gameModesIncorrectButtonStart)});
            btnBackgroundCorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
            btnBackgroundIncorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
            if (SaveManager.getMaximumAchievedLevel(context) >=
                    ApplicationData.getMinimumRequiredLevelForTimeChallengeCorrectAnswerMark()) {
                answersButtons.get(correctButton).setBackgroundDrawable(btnBackgroundCorrect);
            }
            answersButtons.get(pressedNumber).setBackgroundDrawable(btnBackgroundIncorrect);
            playSound(soundIdWrongAnswer);
        }
        CountDownTimer nextLevelTimer = new CountDownTimer(AFTER_ANSWER_TIME, INTERVAL) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                GradientDrawable btnBackground = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {getResources().getColor(R.color.gameModesNormalButtonEnd),
                                getResources().getColor(R.color.gameModesNormalButtonStart)});
                btnBackground.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
                answersButtons.get(correctButton).setBackgroundDrawable(btnBackground);
                answersButtons.get(pressedNumber).setBackgroundDrawable(btnBackground);
                currentQuiz++;
                if (levels.size() > currentQuiz) {
                    displayQuiz(levels, levels.get(currentQuiz));
                } else {
                    if (remainingTime > MAX_REMAINING_TIME_BONUS) {
                        score = (int)(MAX_REMAINING_TIME_COEFFICIENT * score);
                    } else {
                        float coefficient = MAX_REMAINING_TIME_BONUS/((MAX_REMAINING_TIME_COEFFICIENT - 1)*100);
                        float resultedCoefficient = ((remainingTime/coefficient)/100) + 1;
                        score = (int)(score * resultedCoefficient);
                    }
                    endGame();
                }
            }
        }.start();
    }

    private void endGame() {
        countDownTimer.cancel();
        // speedhack check
        Date currentTime = Calendar.getInstance().getTime();
        int gameplayTime = (int)(currentTime.getTime() - startGameLocalTime.getTime());
        float MAX_TIMER_ERROR = 1.1f;
        boolean isLegitScore = true;
        if (gameplayTime > TIME * MAX_TIMER_ERROR) {
            isLegitScore = false;
        }
        //
        textViewTime.setVisibility(View.INVISIBLE);
        imageViewClock.setVisibility(View.INVISIBLE);
        SaveManager.saveGameTimeChallengeAverageScore(context, score);
        closeMessageFragment();
        checkHighScore(score, isLegitScore);
        showEndGameFragment(true);
        lastHighScore = highScore;
    }

    private void checkHighScore(int score, boolean isLegitScore) {
        if (highScore < score) {
            highScore = score;
            SaveManager.saveGameTimeChallengeHighScore(context, score);
            if (isLegitScore) {
            }
        }
    }

    private void showEndGameFragment(boolean needAnimation) {
        if (isPremiumActive) {
            showEndGameFragmentPremium(needAnimation);
        } else {
            showEndGameFragmentNormal(needAnimation);
        }
    }

    private void showEndGameFragmentNormal(boolean needAnimation) {
        final ArrayList<DataOtherGameModes> newLevels = generateQuestions();
        EndGameTimeFragment endGameFragment = createEndGameFragmentNormal(score, lastHighScore, needAnimation);
        isEndGameFragmentClosed = false;
        endGameFragment.setFirstButtonClickListener(new EndGameTimeFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                isEndGameFragmentClosed = true;
                backToMainMenu();
            }
        });
        endGameFragment.setSecondButtonClickListener(new EndGameTimeFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                isEndGameFragmentClosed = true;
                retryCount++;
                startNewGame(newLevels, false);
            }
        });
        endGameFragment.setThirdButtonClickListener(new EndGameTimeFragment.ThirdButtonClickListener() {
            @Override
            public void ThirdButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                isEndGameFragmentClosed = true;
                retryCount++;
                getTimeBonus(newLevels);
            }
        });
        endGameFragment.setButtonsEnabledListener(new EndGameTimeFragment.ButtonsEnabledListener() {
            @Override
            public void ButtonsEnabledListener(boolean isEnabled) {
                isBackButtonsClickable = isEnabled;
            }
        });
        endGameFragment.setProgressBarStartListener(new EndGameTimeFragment.ProgressBarStartListener() {
            @Override
            public void ProgressBarStartListener() {
                playSound(soundIdProgressBar);
            }
        });
        endGameFragment.setHighScoreListener(new EndGameTimeFragment.HighScoreListener() {
            @Override
            public void HighScoreListener() {
                playSound(soundIdHighScore);
            }
        });
        endGameFragment.setViewAdvertisementListener(new EndGameTimeFragment.ViewAdvertisementListener() {
            @Override
            public void ViewAdvertisementListener() {
                viewAdvertisement();
            }
        });
    }

    private void showEndGameFragmentPremium(boolean needAnimation) {
        final ArrayList<DataOtherGameModes> newLevels = generateQuestions();
        EndGameTimeFragmentPremium endGameFragment = createEndGameFragmentPremium(score, lastHighScore, needAnimation);
        isEndGameFragmentClosed = false;
        endGameFragment.setFirstButtonClickListener(new EndGameTimeFragmentPremium.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                isEndGameFragmentClosed = true;
                backToMainMenu();
            }
        });
        endGameFragment.setSecondButtonClickListener(new EndGameTimeFragmentPremium.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                isEndGameFragmentClosed = true;
                retryCount++;
                startNewGame(newLevels, true);
            }
        });
        endGameFragment.setButtonsEnabledListener(new EndGameTimeFragmentPremium.ButtonsEnabledListener() {
            @Override
            public void ButtonsEnabledListener(boolean isEnabled) {
                isBackButtonsClickable = isEnabled;
            }
        });
        endGameFragment.setProgressBarStartListener(new EndGameTimeFragmentPremium.ProgressBarStartListener() {
            @Override
            public void ProgressBarStartListener() {
                playSound(soundIdProgressBar);
            }
        });
        endGameFragment.setHighScoreListener(new EndGameTimeFragmentPremium.HighScoreListener() {
            @Override
            public void HighScoreListener() {
                playSound(soundIdHighScore);
            }
        });
        endGameFragment.setViewAdvertisementListener(new EndGameTimeFragmentPremium.ViewAdvertisementListener() {
            @Override
            public void ViewAdvertisementListener() {
                viewAdvertisement();
            }
        });
    }

    private ArrayList<Integer> getRandomNumbersArray(int numbersCount, int maxNumber) {
        ArrayList<Integer> numbers = new ArrayList<>();
        Random randomGenerator = new Random();
        while (numbers.size() < numbersCount) {
            int random = randomGenerator.nextInt(maxNumber);
            if (!numbers.contains(random)) {
                numbers.add(random);
            }
        }
        return numbers;
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        if (isBackButtonsClickable) {
            playSound(soundIdButtonClick);
            Intent intent = new Intent(GameTimeChallengeActivity.this, SelectGameModeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }

    private MessageFragment createMessageFragment(String title, String message, String btnText) {
        return createMessageFragment(title, message, btnText, null);
    }

    private EndGameTimeFragment createEndGameFragmentNormal(int myScore, int myHighScore, boolean needAnimation) {
        fragmentLevelContainer.setVisibility(View.VISIBLE);
        EndGameTimeFragment endGameFragment = new EndGameTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("score", myScore);
        bundle.putInt("high_score", myHighScore);
        bundle.putInt("max_score", ApplicationData.getFindThePairsLeaderboardMaxScore());
        bundle.putBoolean("need_animation", needAnimation);
        endGameFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(fragmentLevelContainer.getId(), endGameFragment,
                    "message_fragment");
            fragmentTransaction.commit();
        }
        return endGameFragment;
    }

    private EndGameTimeFragmentPremium createEndGameFragmentPremium(int myScore, int myHighScore, boolean needAnimation) {
        fragmentLevelContainer.setVisibility(View.VISIBLE);
        EndGameTimeFragmentPremium endGameFragment = new EndGameTimeFragmentPremium();
        Bundle bundle = new Bundle();
        bundle.putInt("score", myScore);
        bundle.putInt("high_score", myHighScore);
        bundle.putInt("max_score", ApplicationData.getFindThePairsLeaderboardMaxScore());
        bundle.putBoolean("need_animation", needAnimation);
        endGameFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(fragmentLevelContainer.getId(), endGameFragment,
                    "message_fragment");
            fragmentTransaction.commit();
        }
        return endGameFragment;
    }

    private boolean getIsActivityFinished() {
        boolean isActivityFinished;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isActivityFinished = (isFinishing() && isDestroyed());
        } else {
            isActivityFinished = isFinishing();
        }
        return isActivityFinished;
    }

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText,
                                                  String btnSecondText) {
        fragmentLevelContainer.setVisibility(View.VISIBLE);
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("btn_first_text", btnFirstText);
        bundle.putFloat("outside_layout_alpha", 1.0f);
        if (btnSecondText != null) {
            bundle.putString("btn_second_text", btnSecondText);
        }
        messageFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(fragmentLevelContainer.getId(), messageFragment,
                    "message_fragment");
            fragmentTransaction.commit();
        }
        return messageFragment;
    }

    private void closeMessageFragment() {
        if (!getIsActivityFinished()) {
            if (getFragmentManager().findFragmentById(R.id.id_message_fragment) != null) {
                getFragmentManager().beginTransaction().
                        remove(getFragmentManager().findFragmentById(R.id.id_message_fragment)).commit();
            }
        }
    }

    private void initializeMessageContainer() {
        fragmentLevelContainer = new FrameLayout(this);
        fragmentLevelContainer.setId(R.id.id_message_fragment);
        RelativeLayout.LayoutParams fragmentMessageParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fragmentMessageParams.addRule(RelativeLayout.BELOW, R.id.layout_top);
        fragmentMessageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fragmentLevelContainer.setLayoutParams(fragmentMessageParams);
        if (mainLayout != null) {
            mainLayout.addView(fragmentLevelContainer);
        }
    }

    private void backToMainMenu() {
        backButtonPressed();
    }

    private void createAreYouReadyFragment() {
        if (isPremiumActive) {
            createPremiumAreYouReadyFragment();
        } else {
            createNormalAreYouReadyFragment();
        }
    }

    private void createNormalAreYouReadyFragment() {
        final ArrayList<DataOtherGameModes> newLevels = generateQuestions();
        int boostCoins = ApplicationData.getGameModsBoostCoins();
        String message = String.format(
                getResources().getString(R.string.game_modes_bonus_message),
                LocaleTextHelper.getLocaleNumber(boostCoins));
        MessageFragment useBonusMessageFragment = createMessageFragment(
                getResources().getString(R.string.game_modes_bonus_title),
                message,
                getResources().getString(R.string.no),
                getResources().getString(R.string.yes));
        useBonusMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                startNewGame(newLevels, false);
            }
        });
        useBonusMessageFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                getTimeBonus(newLevels);
            }
        });
    }

    private void createPremiumAreYouReadyFragment() {
        final ArrayList<DataOtherGameModes> newLevels = generateQuestions();
        if (newLevels == null) {
            backButtonPressed();
        }
        MessageFragment areYouReadyPremiumFragment = createMessageFragment(
                getResources().getString(R.string.game_mode_time_challenge),
                getResources().getString(R.string.are_you_ready),
                getResources().getString(R.string.start));
        areYouReadyPremiumFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                startNewGame(newLevels, true);
            }
        });
    }

    private void getTimeBonus(ArrayList<DataOtherGameModes> levels) {
        int boostCoins = ApplicationData.getGameModsBoostCoins();
        int coins = SaveManager.getCoins(context);
        if (coins < boostCoins) {
            createWatchVideoMessageFragment();
        } else {
            SaveManager.changeCoinsNumber(context, - boostCoins);
            startNewGame(levels, true);
        }
    }

    private void createWatchVideoMessageFragment() {
        String watchVideoOfferText = String.format(
                getResources().getString(R.string.message_fragment_text_watch_video_offer),
                LocaleTextHelper.getLocaleNumber(ApplicationData.getWatchVideoReward()));
        MessageFragment watchVideoFragment = createMessageFragment(
                getResources().getString(R.string.not_enough_coins),
                watchVideoOfferText,
                getResources().getString(R.string.message_fragment_btn_later),
                getResources().getString(R.string.message_fragment_btn_watch_video)
        );
        watchVideoFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                createAreYouReadyFragment();
            }
        });
        watchVideoFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                clickOnWatchVideo();            }
        });
    }

    private void clickOnWatchVideo() {

    }

    private void noVideo() {
        String messageText =
                getResources().getString(R.string.message_fragment_text_video_not_loaded) +
                        " " +
                        getResources().getString(R.string.try_again_later);
        MessageFragment errorFragment = createMessageFragment(
                getResources().getString(R.string.message_fragment_title_error),
                messageText,
                getResources().getString(R.string.message_fragment_btn_close)
        );
        errorFragment.setOutsideLayoutClickable(true);
        errorFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                createAreYouReadyFragment();
            }
        });
    }

    private void startNewGame(ArrayList<DataOtherGameModes> levels, boolean isBonusActive) {
        isEndGameFragmentClosed = true;
        score = 0;
        currentQuiz = 0;
        remainingTime = -1;
        correctAnswersCount = 0;
        tickCount = 0;
        isTimerEndSoundEnable = true;
        String stringAnswersCount = "X" + LocaleTextHelper.getLocaleNumber(correctAnswersCount);
        textViewAnswersCount.setText(stringAnswersCount);
        String stringPoints = getResources().getString(R.string.score) + LocaleTextHelper.getLocaleNumber(score);
        textViewCurrentPoints.setText(stringPoints);
        displayQuiz(levels, levels.get(currentQuiz));
        startTimer(isBonusActive);
    }

    public void viewAdvertisement() {
        if (isAdvertisementEnabled) {
            if (retryCount == 0 || (retryCount % ApplicationData.getFullScreenAdvertFrequencyOtherGameMods() == 0)) {

            }
        }
    }

    private void getClearMainLayout() {
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.removeAllViews();
        }
    }

    @SuppressWarnings("deprecation")
    private void loadSounds() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (isVolumeOn) {
            if (soundPool == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    soundPool = new SoundPool.Builder()
                            .setMaxStreams(MAX_SOUND_STREAMS)
                            .build();
                } else {
                    soundPool = new SoundPool(MAX_SOUND_STREAMS, AudioManager.STREAM_MUSIC, 1);
                }
                soundIdButtonClick = soundPool.load(this, R.raw.button_click, 1);
                soundIdWrongAnswer = soundPool.load(this, R.raw.wrong_answer, 1);
                soundIdCorrectAnswer = soundPool.load(this, R.raw.correct_answer, 1);
                soundIdHighScore = soundPool.load(this, R.raw.high_score, 1);
                soundTimerTick = soundPool.load(this, R.raw.timer_tick, 1);
                soundTimerEnd = soundPool.load(this, R.raw.timer_end, 1);
                soundIdProgressBar = soundPool.load(this, R.raw.progress_bar, 1);
            }
        }
    }

    private void playSound(int id) {
        if (isVolumeOn) {
            if (soundPool != null) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        }
    }

    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        releaseSoundPool();
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

}
