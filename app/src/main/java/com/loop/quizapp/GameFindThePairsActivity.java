package com.loop.quizapp;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
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
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GameFindThePairsActivity extends AppCompatActivity {

    Context context;

    ImageView btnBack;
    ImageView imageViewClock;
    TextView textViewTime;
    LinearLayout questionLayout;
    ImagePicker imagePicker;
    TextPicker textPicker;
    LinearLayout buttonsContainer;
    CountDownTimer countDownTimer;
    RelativeLayout mainLayout;
    FrameLayout fragmentLevelContainer;
    Button buttonCheck;
    ProgressBar progressBarTime;
    TextView textViewLeft;

    boolean isEndGameFragmentClosed = true;
    int score;
    Date startGameLocalTime;
    long remainingTime;
    long startQuestionTime;
    boolean isTimerEndSoundEnable;
    int remainingNumOfLevels = 0;
    int imageQuestionHeight;
    int textQuestionHeight;
    int imageQuestionWidth;
    int textQuestionWidth;
    int viewQuestionMargin;

    int retryCount;
    int NUM_OF_LEVELS;
    int CORRECT_ANSWER_POINTS;
    int TIME;
    int AFTER_ANSWER_TIME = 200;
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

    ArrayList<Integer> imagesIds;
    ArrayList<Integer> textIds;
    int pickedImagePosition = -1;
    int pickedTextPosition = -1;

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
        DataForOtherGameModesActivity data = SaveManager.getDataForGameFindThePairsActivity(context);
        isAdvertisementEnabled = data.getAdvertisementEnabled();
        isPremiumActive = data.getIsPremiumActive();
        isVolumeOn = data.getIsVolumeOn();
        highScore = data.getHighScore();
        lastHighScore = highScore;
        NUM_OF_LEVELS = ApplicationData.getFindThePairsNumOfLevels();
        CORRECT_ANSWER_POINTS = ApplicationData.getFindThePairsCorrectAnswerPoints();
    }

    private void viewsInitialization() {
        setContentView(R.layout.activity_game_find_the_pairs);
        mainLayout = findViewById(R.id.main_layout);
        textViewTime = findViewById(R.id.text_view_time);
        textViewTime.setVisibility(View.INVISIBLE);
        //
        initializeQuestionLayout();
        //
        buttonsContainer = findViewById(R.id.buttons_container);
        buttonCheck = findViewById(R.id.btn_answer_check);
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
        textViewLeft = findViewById(R.id.text_view_left);
        animationScale = AnimationUtils.loadAnimation(getApplication(),
                R.anim.hint_btns_blinking);
        initializeMessageContainer();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void initializeQuestionLayout() {
        if (questionLayout != null) {
            questionLayout.removeAllViews();
            questionLayout = null;
        }
        //
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        float IMAGE_LAYOUT_WEIGHT = 0.4f;
        float TEXT_LAYOUT_WEIGHT = 0.6f;
        float MARGIN_WEIGHT = 0.02f;
        int imageContainerWidth = (int)(screenWidth * IMAGE_LAYOUT_WEIGHT);
        int textContainerWidth = (int)(screenWidth * TEXT_LAYOUT_WEIGHT);
        imageQuestionHeight = imageContainerWidth;
        textQuestionHeight = imageQuestionHeight/2;
        imageQuestionWidth = imageContainerWidth;
        textQuestionWidth = textContainerWidth;
        viewQuestionMargin = (int)(screenWidth * MARGIN_WEIGHT);
        //
        questionLayout = findViewById(R.id.question_layout);
        LinearLayout imagePickerContainer = new LinearLayout(context);
        imagePickerContainer.setLayoutParams(new LinearLayout.LayoutParams(
                imageContainerWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout textPickerContainer = new LinearLayout(context);
        textPickerContainer.setLayoutParams(new LinearLayout.LayoutParams(
                textContainerWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        questionLayout.addView(imagePickerContainer);
        questionLayout.addView(textPickerContainer);
        //
        imagePicker = new ImagePicker(context);
        imagePickerContainer.addView(imagePicker);
        textPicker = new TextPicker(context);
        textPickerContainer.addView(textPicker);
    }

    private void startTimer(boolean isBonusActive) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (isBonusActive) {
            TIME = ApplicationData.getFindThePairsBoostTime();
        } else {
            TIME = ApplicationData.getFindThePairsTime();
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

    private String getFormattedDateString(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    private void updateAnswerInfo(int left) {
        String stringLeft = String.format(
                getResources().getString(R.string.game_modes_left),
                LocaleTextHelper.getLocaleNumber(left));
        textViewLeft.setText(stringLeft);
        textViewLeft.startAnimation(animationScale);
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

    private DataGameModeFindThePairs generateQuestions() {
        LevelManagerGameModes levels = new LevelManagerGameModes(context);
        int numOfLevels = levels.getNumOfLevels();
        ArrayList<Integer> availableLevelNumbers = new ArrayList<>();
        for (int x = 0; x < numOfLevels; x++) {
            availableLevelNumbers.add(x);
        }
        int numOfRequiredLevels;
        if (numOfLevels > NUM_OF_LEVELS) {
            numOfRequiredLevels = NUM_OF_LEVELS;
        } else {
            numOfRequiredLevels = numOfLevels;
        }
        // pick levels
        ArrayList<Integer> pickedLevelNumbers = new ArrayList<>();
        ArrayList<Integer> availableImageIds = new ArrayList<>();
        ArrayList<Integer> availableTextIds = new ArrayList<>();
        imagesIds = new ArrayList<>();
        textIds = new ArrayList<>();
        ArrayList<String> imagesNames = new ArrayList<>();
        ArrayList<String> textNames = new ArrayList<>();
        int currentLevelNumber;
        for (int x = 0; x < numOfRequiredLevels; x++) {
            int generatedNumber = getRandomLevelNumber(availableLevelNumbers.size());
            if (generatedNumber == -1) {
                return null;
            }
            currentLevelNumber = availableLevelNumbers.get(generatedNumber);
            availableLevelNumbers.remove((Integer) currentLevelNumber);
            pickedLevelNumbers.add(currentLevelNumber);
        }
        // mix levels
        availableImageIds.addAll(pickedLevelNumbers);
        availableTextIds.addAll(pickedLevelNumbers);
        for (int x = 0; x < numOfRequiredLevels; x ++) {
            int imageIndex = getRandomNumber(availableImageIds.size());
            int imageId = availableImageIds.get(imageIndex);
            availableImageIds.remove(imageIndex);
            imagesNames.add(levels.getLevel(imageId).getImage());
            imagesIds.add(imageId);

            int textIndex = getRandomNumber(availableTextIds.size());
            int textId = availableTextIds.get(textIndex);
            availableTextIds.remove(textIndex);
            textNames.add(levels.getLevel(textId).getName());
            textIds.add(textId);
        }
        remainingNumOfLevels = numOfRequiredLevels;
        return new DataGameModeFindThePairs(imagesNames, textNames, imagesIds, textIds);
    }

    private int getRandomNumber(int maxNumber) {
        if (maxNumber <= 0) {
            return -1;
        }
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(maxNumber);
    }

    private int getRandomLevelNumber(int maxNumber) {
        if (maxNumber <= 0) {
            return -1;
        }
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(maxNumber);
    }


    @SuppressWarnings("deprecation")
    private void displayQuiz(final DataGameModeFindThePairs levels, final boolean isBonusActive) {
        final Animation click_answer_btn = AnimationUtils.loadAnimation(this, R.anim.click_on_answer_btn);
        LoadDrawablesManager loadDrawablesManager = new LoadDrawablesManager(getApplicationContext());
        //
        ArrayList<Drawable> imageDrawables = new ArrayList<>();
        ArrayList<String> stringArray = new ArrayList<>();
        for (int x = 0; x < levels.getImages().size(); x ++) {
            Drawable drawable = loadDrawablesManager.getDrawable(
                    "quiz_images/" + levels.getImages().get(x) + ".png",
                    1.0f, 1.0f);
            imageDrawables.add(drawable);
            stringArray.add(levels.getNames().get(x));
        }
        pickedImagePosition = -1;
        pickedTextPosition = -1;
        imagePicker.setData(context, imageDrawables,
                imageQuestionHeight, imageQuestionWidth, viewQuestionMargin);
        imagePicker.setItemClickListener(new ImagePicker.ItemClickListener() {
            @Override
            public void ItemClickListener(int itemPosition) {
                if (pickedImagePosition != itemPosition) {
                    playSound(soundIdButtonClick);
                }
                pickedImagePosition = itemPosition;
                if (pickedTextPosition != -1) {
                    unblockButton();
                }
            }
        });
        //      start timer after the question is displayed
        ViewTreeObserver imagePickerViewTreeObserver = imagePicker.getViewTreeObserver();
        imagePickerViewTreeObserver.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                imagePicker.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startTimer(isBonusActive);
            }
        });
        //
        textPicker.setItemClickListener(new TextPicker.ItemClickListener() {
            @Override
            public void ItemClickListener(int itemPosition) {
                if (pickedTextPosition != itemPosition) {
                    playSound(soundIdButtonClick);
                }
                pickedTextPosition = itemPosition;
                if (pickedImagePosition != -1) {
                    unblockButton();
                }
            }
        });
        textPicker.setData(context, stringArray,
                textQuestionHeight, textQuestionWidth, viewQuestionMargin);
        blockButton();
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click_answer_btn);
                if (pickedImagePosition != -1 && pickedTextPosition != -1) {
                    int pickedImageId = imagesIds.get(pickedImagePosition);
                    int pickedTextId = textIds.get(pickedTextPosition);
                    if (pickedImageId == pickedTextId) {
                        correctAnswer();
                        buttonDelay(false);
                    } else {
                        incorrectAnswer();
                        buttonDelay(true);
                    }
                }
            }
        });
        if (remainingTime == -1) {
            startQuestionTime = TIME;
        } else {
            startQuestionTime = remainingTime;
        }
    }

    private void unblockButton() {
        buttonCheck.setClickable(true);
        GradientDrawable btnBackground = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {getResources().getColor(R.color.gameModesNormalButtonEnd),
                        getResources().getColor(R.color.gameModesNormalButtonStart)});
        btnBackground.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
        buttonCheck.setBackgroundDrawable(btnBackground);
    }

    private void blockButton() {
        buttonCheck.setClickable(false);
        GradientDrawable btnBackground = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {getResources().getColor(R.color.buttonNonClickableBackgroundEnd),
                        getResources().getColor(R.color.buttonNonClickableBackgroundStart)});
        btnBackground.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
        buttonCheck.setBackgroundDrawable(btnBackground);
    }

    private void buttonDelay(final boolean isClickable) {
        buttonCheck.setClickable(false);
        new CountDownTimer(AFTER_ANSWER_TIME, INTERVAL) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                if (isClickable) {
                    unblockButton();
                } else {
                    blockButton();
                }
            }
        }.start();
    }

    private void correctAnswer() {
        pickedImagePosition = -1;
        pickedTextPosition = -1;
        buttonCheck.setClickable(false);
        GradientDrawable btnBackgroundCorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {getResources().getColor(R.color.gameModesCorrectButtonEnd),
                        getResources().getColor(R.color.gameModesCorrectButtonStart)});
        btnBackgroundCorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
        buttonCheck.setBackgroundDrawable(btnBackgroundCorrect);
        playSound(soundIdCorrectAnswer);
        remainingNumOfLevels --;
        updateAnswerInfo(remainingNumOfLevels);
        score = score + CORRECT_ANSWER_POINTS;
        if (remainingNumOfLevels <= 1) {
            score = score + CORRECT_ANSWER_POINTS;
            int remainingTimeBonus = ApplicationData.getFindThePairsRemainingTimeBonus();
            int remainingMsPerPoint = ApplicationData.getFindThePairsRemainingMsPerPoint();
            if (remainingTime >= remainingTimeBonus) {
                score = score + (remainingTimeBonus / remainingMsPerPoint);
            } else {
                score = score + (int) (remainingTime / remainingMsPerPoint);
            }
            endGame();
        }
        imagePicker.deletePickedItem();
        textPicker.deletePickedItem();
    }

    private void incorrectAnswer() {
        GradientDrawable btnBackgroundIncorrect = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {getResources().getColor(R.color.gameModesIncorrectButtonEnd),
                        getResources().getColor(R.color.gameModesIncorrectButtonStart)});
        btnBackgroundIncorrect.setCornerRadius(getResources().getDimension(R.dimen.time_challenge_button_view_radius));
        buttonCheck.setBackgroundDrawable(btnBackgroundIncorrect);
        playSound(soundIdWrongAnswer);
        createPenaltyFragment();
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
        imageViewClock.setVisibility(View.INVISIBLE);
        textViewTime.setVisibility(View.INVISIBLE);
        SaveManager.saveGameFindThePairsAverageScore(context, score);
        closeMessageFragment();
        checkHighScore(score, isLegitScore);
        showEndGameFragment(true);
        lastHighScore = highScore;
    }

    private void checkHighScore(int score, boolean isLegitScore) {
        if (highScore < score) {
            highScore = score;
            SaveManager.saveGameFindThePairsHighScore(context, score);
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
        final DataGameModeFindThePairs newLevels = generateQuestions();
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
        final DataGameModeFindThePairs newLevels = generateQuestions();
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
            Intent intent = new Intent(GameFindThePairsActivity.this, SelectGameModeActivity.class);
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

    private void createPenaltyFragment() {
        fragmentLevelContainer.setVisibility(View.VISIBLE);
        PenaltyFragment penaltyFragment = new PenaltyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("time", ApplicationData.getFindThePairsPenaltyMs());
        penaltyFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(fragmentLevelContainer.getId(), penaltyFragment,
                    "penalty_fragment");
            fragmentTransaction.commit();
        }
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
        final DataGameModeFindThePairs newLevels = generateQuestions();
        if (newLevels == null) {
            backButtonPressed();
        }
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
        final DataGameModeFindThePairs newLevels = generateQuestions();
        if (newLevels == null) {
            backButtonPressed();
        }
        MessageFragment areYouReadyPremiumFragment = createMessageFragment(
                getResources().getString(R.string.game_mode_find_the_pairs),
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

    private void getTimeBonus(DataGameModeFindThePairs levels) {
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

    private void startNewGame(DataGameModeFindThePairs levels, boolean isBonusActive) {
        isEndGameFragmentClosed = true;
        score = 0;
        remainingTime = -1;
        tickCount = 0;
        isTimerEndSoundEnable = true;
        String stringLeft = String.format(
                getResources().getString(R.string.game_modes_left),
                LocaleTextHelper.getLocaleNumber(remainingNumOfLevels));
        textViewLeft.setText(stringLeft);
        initializeQuestionLayout();
        displayQuiz(levels, isBonusActive);
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
