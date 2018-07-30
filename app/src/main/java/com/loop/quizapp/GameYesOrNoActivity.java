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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class GameYesOrNoActivity extends AppCompatActivity {

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
    Button buttonNo;
    Button buttonYes;
    ProgressBar progressBarTime;
    TextView textViewCurrentPoints;
    TextView textViewAnswersCount;
    TextView textViewQuestion;

    boolean isEndGameFragmentClosed = true;
    int currentQuiz;
    int score;
    Date startGameLocalTime;
    long remainingTime;
    long startQuestionTime;
    boolean isTimerEndSoundEnable;
    int correctAnswersCount = 0;

    int retryCount;
    int MAX_NUM_OF_LEVELS;
    int CORRECT_ANSWER_POINTS;
    int TIME;
    int AFTER_ANSWER_TIME = 600;
    int MIN_TIME_FOR_ANSWER;
    int MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS;
    float MAX_SPEED_BONUS_COEFFICIENT;
    int INTERVAL = 10;
    int END_SOUND_DURATION = 2000;
    int tickCount;
    float imageClockAlpha = 0.2f;

    Animation animationScale;

    boolean isBackButtonsClickable;
    boolean playGamesEnabled;
    boolean isAdvertisementEnabled = true;
    boolean isPremiumActive = false;
    int lastHighScore;
    int highScore;
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
        DataForOtherGameModesActivity data = SaveManager.getDataForGameYesOrNoActivity(context);
        isAdvertisementEnabled = data.getAdvertisementEnabled();
        isPremiumActive = data.getIsPremiumActive();
        isVolumeOn = data.getIsVolumeOn();
        highScore = data.getHighScore();
        lastHighScore = highScore;
        MAX_NUM_OF_LEVELS = ApplicationData.getYesOrNoMaxNumOfLevels();
        CORRECT_ANSWER_POINTS = ApplicationData.getYesOrNoCorrectAnswerPoints();
        MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS = ApplicationData.getYesOrNoMaxTimeForAnswerWithSpeedBonus();
        MIN_TIME_FOR_ANSWER = ApplicationData.getYesOrNoMinTimeForAnswerWithSpeedBonus();
        MAX_SPEED_BONUS_COEFFICIENT = ApplicationData.getYesOrNoMaxSpeedBonusCoefficient();
    }

    private void viewsInitialization() {
        setContentView(R.layout.activity_game_yes_or_no);
        mainLayout = findViewById(R.id.main_layout);
        textViewTime = findViewById(R.id.text_view_time);
        textViewTime.setVisibility(View.INVISIBLE);
        quizContainer = findViewById(R.id.quiz_container);
        quizContainerFake = findViewById(R.id.quiz_container_fake);
        buttonsContainer = findViewById(R.id.buttons_container);
        buttonNo = findViewById(R.id.btn_answer_no);
        buttonYes = findViewById(R.id.btn_answer_yes);
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
        imageViewClock.setVisibility(View.INVISIBLE);
        imageViewClock.setColorFilter(getResources().getColor(R.color.gameModesClock), PorterDuff.Mode.SRC_ATOP);
        progressBarTime = findViewById(R.id.progress_bar_time);
        textViewCurrentPoints = findViewById(R.id.text_view_current_points);
        textViewAnswersCount = findViewById(R.id.text_view_answers_count);
        textViewQuestion = findViewById(R.id.text_view_question);
        animationScale = AnimationUtils.loadAnimation(getApplication(),
                R.anim.hint_btns_blinking);
        initializeMessageContainer();
    }

    private void startTimer(boolean isBonusActive) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (isBonusActive) {
            TIME = ApplicationData.getYesOrNoBoostTime();
        } else {
            TIME = ApplicationData.getYesOrNoTime();
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
                int n = ((int) millisUntilFinished * 100) / TIME;
                int red = (255 * (100 - n)) / 100;
                int green = (255 * n) / 100;
                progressBarTime.getProgressDrawable().setColorFilter(
                        android.graphics.Color.rgb(red, green, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
                progressBarTime.setProgress(TIME - (int) millisUntilFinished);
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

    private ArrayList<DataGameModeYesOrNo> generateQuestions() {
        ArrayList<DataGameModeYesOrNo> dataGameModeYesOrNo = new ArrayList<>();
        LevelManagerGameModes levels = new LevelManagerGameModes(context);
        int numOfLevels = levels.getNumOfLevels();
        ArrayList<Integer> availableLevelNumbers = new ArrayList<>();
        for (int x = 0; x < numOfLevels; x++) {
            availableLevelNumbers.add(x);
        }
        int numOfRequiredLevels;
        if (numOfLevels > MAX_NUM_OF_LEVELS) {
            numOfRequiredLevels = MAX_NUM_OF_LEVELS;
        } else {
            numOfRequiredLevels = numOfLevels;
        }
        int currentLevelAnswer;
        int currentLevelImage;
        boolean currentLevelIsItTrue;
        for (int x = 0; x < numOfRequiredLevels; x++) {
            int generatedNumber = getRandomLevelNumber(availableLevelNumbers.size());
            if (generatedNumber == -1) {
                return dataGameModeYesOrNo;
            }
            currentLevelAnswer = availableLevelNumbers.get(generatedNumber);
            availableLevelNumbers.remove((Integer) currentLevelAnswer);
            currentLevelIsItTrue = getIsCorrectImageNeeded();
            if (currentLevelIsItTrue) {
                currentLevelImage = currentLevelAnswer;
            } else {
                generatedNumber = getRandomLevelNumber(availableLevelNumbers.size());
                if (generatedNumber == -1) {
                    return dataGameModeYesOrNo;
                }
                currentLevelImage = availableLevelNumbers.get(generatedNumber);
                availableLevelNumbers.remove((Integer) currentLevelImage);
            }
            dataGameModeYesOrNo.add(new DataGameModeYesOrNo(
                    levels.getLevel(currentLevelImage).getImage(),
                    levels.getLevel(currentLevelAnswer).getName(), currentLevelIsItTrue));
        }
        return dataGameModeYesOrNo;
    }

    private boolean getIsCorrectImageNeeded() {
        Random randomGenerator = new Random();
        int random = randomGenerator.nextInt(10);
        return (random % 2 == 0);
    }

    private int getRandomLevelNumber(int maxNumber) {
        if (maxNumber <= 0) {
            return -1;
        }
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(maxNumber);
    }

    @SuppressWarnings("deprecation")
    private void displayQuiz(final ArrayList<DataGameModeYesOrNo> levels, final DataGameModeYesOrNo question) {
        Animation animationDisappearance = AnimationUtils.loadAnimation(getApplication(),
                R.anim.disappearance_left_to_right);
        Animation animationAppearance = AnimationUtils.loadAnimation(getApplication(),
                R.anim.appearance_left_to_right);
        final Animation click_answer_btn = AnimationUtils.loadAnimation(this, R.anim.click_on_answer_btn);
        LoadDrawablesManager loadDrawablesManager = new LoadDrawablesManager(getApplicationContext());
        Drawable drawable = loadDrawablesManager.getDrawable(
                "quiz_images/" + question.getImageName() + ".png",
                1.0f, 1.0f);
        textViewQuestion.setText(question.getAnswer());
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
        buttonYes.setClickable(true);
        buttonNo.setClickable(true);
        final boolean isCorrect = question.isItTrue;
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click_answer_btn);
                nextLevel(levels, isCorrect, question.getAnswer(), true);
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click_answer_btn);
                nextLevel(levels, !isCorrect, question.getAnswer(), false);
            }
        });
        if (remainingTime == -1) {
            startQuestionTime = TIME;
        } else {
            startQuestionTime = remainingTime;
        }
    }

    @SuppressWarnings("deprecation")
    private void nextLevel(final ArrayList<DataGameModeYesOrNo> levels, boolean isCorrect,
                           final String answer, boolean isYesButtonClicked) {
        buttonNo.setClickable(false);
        buttonYes.setClickable(false);
        if (isCorrect) {
            correctAnswersCount++;
            int answerTime = (int) (startQuestionTime - remainingTime);
            float speedCoefficient = 1.0f;
            if (answerTime < MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS) {
                float tickSpeedCoefficient =
                        (MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS - MIN_TIME_FOR_ANSWER) / ((MAX_SPEED_BONUS_COEFFICIENT - 1) * 100);
                speedCoefficient =
                        ((((MAX_TIME_FOR_ANSWER_WITH_SPEED_BONUS - MIN_TIME_FOR_ANSWER) - (answerTime - MIN_TIME_FOR_ANSWER)) / tickSpeedCoefficient) / 100) + 1;
            }
            if (speedCoefficient > MAX_SPEED_BONUS_COEFFICIENT) {
                speedCoefficient = MAX_SPEED_BONUS_COEFFICIENT;
            }
            int answerScore = (int) (CORRECT_ANSWER_POINTS * speedCoefficient);
            score = score + answerScore;
            GradientDrawable btnBackgroundCorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.gameModesCorrectButtonEnd),
                            getResources().getColor(R.color.gameModesCorrectButtonStart)});
            btnBackgroundCorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
            if (isYesButtonClicked) {
                buttonYes.setBackgroundDrawable(btnBackgroundCorrect);
            } else {
                buttonNo.setBackgroundDrawable(btnBackgroundCorrect);
            }
            playSound(soundIdCorrectAnswer);
        } else {
            score = 0;
            correctAnswersCount = 0;
            GradientDrawable btnBackgroundCorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.gameModesCorrectButtonEnd),
                            getResources().getColor(R.color.gameModesCorrectButtonStart)});
            GradientDrawable btnBackgroundIncorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.gameModesIncorrectButtonEnd),
                            getResources().getColor(R.color.gameModesIncorrectButtonStart)});
            btnBackgroundCorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
            btnBackgroundIncorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
            if (isYesButtonClicked) {
                buttonYes.setBackgroundDrawable(btnBackgroundIncorrect);
            } else {
                buttonNo.setBackgroundDrawable(btnBackgroundIncorrect);
            }
            playSound(soundIdWrongAnswer);
        }
        updateAnswerInfo(score);
        new CountDownTimer(AFTER_ANSWER_TIME, INTERVAL) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                GradientDrawable btnBackground = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{getResources().getColor(R.color.gameModesNormalButtonEnd),
                                getResources().getColor(R.color.gameModesNormalButtonStart)});
                btnBackground.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
                buttonYes.setBackgroundDrawable(btnBackground);
                buttonNo.setBackgroundDrawable(btnBackground);
                currentQuiz++;
                if (levels.size() > currentQuiz) {
                    displayQuiz(levels, levels.get(currentQuiz));
                } else {
                    endGame();
                }
            }
        }.start();
    }

    private void endGame() {
        countDownTimer.cancel();
        // speedhack check
        Date currentTime = Calendar.getInstance().getTime();
        int gameplayTime = (int) (currentTime.getTime() - startGameLocalTime.getTime());
        float MAX_TIMER_ERROR = 1.1f;
        boolean isLegitScore = true;
        if (gameplayTime > TIME * MAX_TIMER_ERROR) {
            isLegitScore = false;
        }
        //
        textViewTime.setVisibility(View.INVISIBLE);
        imageViewClock.setVisibility(View.INVISIBLE);
        SaveManager.saveGameYesOrNoAverageScore(context, score);
        closeMessageFragment();
        checkHighScore(score, isLegitScore);
        showEndGameFragment(true);
        lastHighScore = highScore;
    }

    private void checkHighScore(int score, boolean isLegitScore) {
        if (highScore < score) {
            highScore = score;
            SaveManager.saveGameYesOrNoHighScore(context, score);
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
        final ArrayList<DataGameModeYesOrNo> newLevels = generateQuestions();
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
        final ArrayList<DataGameModeYesOrNo> newLevels = generateQuestions();
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

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        if (isBackButtonsClickable) {
            playSound(soundIdButtonClick);
            Intent intent = new Intent(GameYesOrNoActivity.this, SelectGameModeActivity.class);
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
        final ArrayList<DataGameModeYesOrNo> newLevels = generateQuestions();
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
        final ArrayList<DataGameModeYesOrNo> newLevels = generateQuestions();
        if (newLevels == null) {
            backButtonPressed();
        }
        MessageFragment areYouReadyPremiumFragment = createMessageFragment(
                getResources().getString(R.string.game_mode_yes_or_no),
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

    private void getTimeBonus(ArrayList<DataGameModeYesOrNo> levels) {
        int boostCoins = ApplicationData.getGameModsBoostCoins();
        int coins = SaveManager.getCoins(context);
        if (coins < boostCoins) {
            createWatchVideoMessageFragment();
        } else {
            SaveManager.changeCoinsNumber(context, -boostCoins);
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
                clickOnWatchVideo();
            }
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

    private void startNewGame(ArrayList<DataGameModeYesOrNo> levels, boolean isBonusActive) {
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
