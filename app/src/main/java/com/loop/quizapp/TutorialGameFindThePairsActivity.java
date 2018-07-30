package com.loop.quizapp;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
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

public class TutorialGameFindThePairsActivity extends AppCompatActivity {

    Context context;

    ImageView btnBack;
    TextView textViewTime;
    LinearLayout questionLayout;
    ImagePicker imagePicker;
    TextPicker textPicker;
    LinearLayout buttonsContainer;
    RelativeLayout mainLayout;
    FrameLayout fragmentLevelContainer;
    Button buttonCheck;
    ProgressBar progressBarTime;
    TextView textViewLeft;

    boolean isEndGameFragmentClosed = true;
    int remainingNumOfLevels = 0;
    int imageQuestionHeight;
    int textQuestionHeight;
    int imageQuestionWidth;
    int textQuestionWidth;
    int viewQuestionMargin;

    int retryCount;
    int AFTER_ANSWER_TIME = 200;
    int INTERVAL = 10;
    int TUTORIAL_NUM_OF_LEVELS;

    Animation animationScale;

    boolean isBackButtonsClickable;
    boolean isAdvertisementEnabled = true;
    int highScore;
    int lastHighScore;
    boolean isVolumeOn;
    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    int soundIdButtonClick;
    int soundIdWrongAnswer;
    int soundIdNextLevel;
    int soundIdHighScore;
    int soundIdProgressBar;

    ArrayList<Integer> imagesIds;
    ArrayList<Integer> textIds;
    int pickedImagePosition = -1;
    int pickedTextPosition = -1;

    boolean isFromSelectGameModesActivity = false;

    boolean isSecondTutorialStepComplete = false;
    boolean isThirdTutorialStepComplete = false;
    boolean isFourthTutorialStepComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        viewsInitialization();
        isFromSelectGameModesActivity = getIntent().getBooleanExtra("isFromSelectGameModesActivity", false);
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
            showEndGameFragment();
        }
    }

    private void loadData() {
        retryCount = 0;
        DataForOtherGameModesActivity data = SaveManager.getDataForGameFindThePairsActivity(context);
        TUTORIAL_NUM_OF_LEVELS = ApplicationData.getTutorialMinNumOfLevels();
        isAdvertisementEnabled = data.getAdvertisementEnabled();
        isVolumeOn = data.getIsVolumeOn();
        highScore = data.getHighScore();
        lastHighScore = highScore;
    }

    private void viewsInitialization() {
        setContentView(R.layout.activity_game_find_the_pairs);
        mainLayout = findViewById(R.id.main_layout);
        textViewTime = findViewById(R.id.text_view_time);
        textViewTime.setVisibility(View.INVISIBLE);
        //
        initializeQuestionLayout();
        //
        ImageView clock = findViewById(R.id.image_view_clock);
        clock.setVisibility(View.INVISIBLE);
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
        progressBarTime = findViewById(R.id.progress_bar_time);
        textViewLeft = findViewById(R.id.text_view_left);
        textViewLeft.setVisibility(View.INVISIBLE);
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

    private void updateAnswerInfo(int left) {
        String stringLeft = String.format(
                getResources().getString(R.string.game_modes_left),
                LocaleTextHelper.getLocaleNumber(left));
        textViewLeft.setText(stringLeft);
        textViewLeft.startAnimation(animationScale);
    }

    private DataGameModeFindThePairs generateQuestions() {
        LevelManagerGameModes levels = new LevelManagerGameModes(context, TUTORIAL_NUM_OF_LEVELS);
        int numOfLevels = levels.getNumOfLevels();
        if (numOfLevels == 0) {
            return null;
        }
        ArrayList<String> imagesNames = new ArrayList<>();
        ArrayList<String> textNames = new ArrayList<>();
        imagesIds = new ArrayList<>();
        textIds = new ArrayList<>();
        for (int x = 0; x < numOfLevels; x++) {
            int secondId = numOfLevels - x - 1;
            imagesNames.add(levels.getLevel(secondId).getImage());
            imagesIds.add(secondId);
            textNames.add(levels.getLevel(x).getName());
            textIds.add(x);
        }
        remainingNumOfLevels = numOfLevels;
        return new DataGameModeFindThePairs(imagesNames, textNames, imagesIds, textIds);
    }

    @SuppressWarnings("deprecation")
    private void displayQuiz(final DataGameModeFindThePairs levels) {
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
                if (!isThirdTutorialStepComplete) {
                    startTutorialStep3();
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
            }
        });
        //
        textPicker.setItemClickListener(new TextPicker.ItemClickListener() {
            @Override
            public void ItemClickListener(int itemPosition) {
                if (pickedTextPosition != itemPosition) {
                    playSound(soundIdButtonClick);
                }
                if (!isSecondTutorialStepComplete) {
                    startTutorialStep2();
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
        buttonCheck.setClickable(false);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFourthTutorialStepComplete && isSecondTutorialStepComplete && isThirdTutorialStepComplete) {
                    startTutorialStep4();
                }
                if (pickedImagePosition != -1 && pickedTextPosition != -1) {
                    v.startAnimation(click_answer_btn);
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
        startTutorialStep1();
    }

    private void startTutorialStep1() {
        textPicker.setTutorialActiveItem(0);
        imagePicker.setTutorialActiveItem(-1);
    }

    private void startTutorialStep2() {
        isSecondTutorialStepComplete = true;
        MessageFragment startTutorialMessageFragment = createMessageFragment(
                getResources().getString(R.string.tutorial),
                getResources().getString(R.string.tutorial_message_2),
                getResources().getString(R.string.message_fragment_btn_close));
        startTutorialMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                imagePicker.tutorialScrollDown();
            }
        });
        imagePicker.setTutorialActiveItem(TUTORIAL_NUM_OF_LEVELS - 1);
    }

    private void startTutorialStep3() {
        isThirdTutorialStepComplete = true;
        String tutorialMessage = String.format(
                getResources().getString(R.string.tutorial_message_3),
                getResources().getString(R.string.check));
        MessageFragment startTutorialMessageFragment = createMessageFragment(
                getResources().getString(R.string.tutorial),
                tutorialMessage,
                getResources().getString(R.string.message_fragment_btn_close));
        startTutorialMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
    }

    private void startTutorialStep4() {
        isFourthTutorialStepComplete = true;
        imagePicker.setTutorialActiveItemAll();
        textPicker.setTutorialActiveItemAll();
        MessageFragment startTutorialMessageFragment = createMessageFragment(
                getResources().getString(R.string.tutorial),
                getResources().getString(R.string.tutorial_message_4),
                getResources().getString(R.string.message_fragment_btn_close));
        startTutorialMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
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
        playSound(soundIdNextLevel);
        remainingNumOfLevels --;
        updateAnswerInfo(remainingNumOfLevels);
        if (remainingNumOfLevels <= 1) {
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
        textViewTime.setVisibility(View.INVISIBLE);
        closeMessageFragment();
        showEndGameFragment();
    }

    private void showEndGameFragment() {
        MessageFragment endTutorialMessageFragment = createMessageFragment(
                getResources().getString(R.string.tutorial),
                getResources().getString(R.string.tutorial_end_message),
                getResources().getString(R.string.btn_exit));
        endTutorialMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                backButtonPressed();
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
            Intent intent;
            if (isFromSelectGameModesActivity) {
                intent = new Intent(TutorialGameFindThePairsActivity.this, SelectGameModeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            } else {
                intent = new Intent(TutorialGameFindThePairsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
            finish();
            startActivity(intent);
        }
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

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText) {
        fragmentLevelContainer.setVisibility(View.VISIBLE);
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("btn_first_text", btnFirstText);
        bundle.putFloat("outside_layout_alpha", 1.0f);
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

    private void createAreYouReadyFragment() {
        final DataGameModeFindThePairs newLevels = generateQuestions();
        if (newLevels == null) {
            backButtonPressed();
        }
        MessageFragment startTutorialMessageFragment = createMessageFragment(
                getResources().getString(R.string.tutorial),
                getResources().getString(R.string.tutorial_message_1),
                getResources().getString(R.string.start));
        startTutorialMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                startNewGame(newLevels);
            }
        });
    }

    private void startNewGame(DataGameModeFindThePairs levels) {
        isEndGameFragmentClosed = true;
        textViewLeft.setVisibility(View.VISIBLE);
        String stringLeft = String.format(
                getResources().getString(R.string.game_modes_left),
                LocaleTextHelper.getLocaleNumber(remainingNumOfLevels));
        textViewLeft.setText(stringLeft);
        initializeQuestionLayout();
        displayQuiz(levels);
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
                soundIdNextLevel = soundPool.load(this, R.raw.correct_answer, 1);
                soundIdHighScore = soundPool.load(this, R.raw.end_game, 1);
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
        releaseSoundPool();
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

}
