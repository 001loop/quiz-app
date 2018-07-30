package com.loop.quizapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class GameActivity extends AppCompatActivity implements GameActivityInterface {


    RelativeLayout mainLayout;
    FrameLayout messageFragmentContainer;
    LevelInfoFragment levelInfoFragment;
    FullScreenImageFragment fullScreenImageFragment;
    AnswerFragment answerFragment;
    CharsFragment charsFragment;
    TextView textViewQuiz;
    TextView textViewCoins;
    TextView levelTextView;
    ButtonView btnRemoveLetters;
    ButtonView btnShowLetter;
    ImageView btnBack;
    ImageView coinsImgTopLayout;
    ButtonView btnStore;
    ButtonView btnAskFriend;
    LinearLayout btnShowLetterContainer;
    LinearLayout btnRemoveLettersContainer;
    LinearLayout btnStoreContainer;
    LinearLayout quizContainer;
    RelativeLayout quizImageTextContainer;
    RelativeLayout topLayout;
    Level currentLevel;
    LevelManager levelManager;

    boolean isVolumeOn;
    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 2;
    int soundIdButtonClick;
    int soundIdShowLetter;
    int soundIdRemoveLetters;
    int soundIdNotEnoughCoins;
    int soundIdInsertLetter;
    int soundIdCantInsertLetter;
    int soundIdRemoveLetter;
    int soundIdWrongAnswer;
    int soundIdNextLevel;
    int soundIdEndGame;
    boolean isInsertRemoveSoundEnabled = true;
    boolean isWrongAnswerEnabled = true;
    float IMAGE_WIDTH_FACTOR = 0.8f;

    Context context;

    private StatisticManager statisticManager;

    private boolean isGlobalLayoutListenerAvailable = true;
    boolean playGamesEnabled;

    @SuppressWarnings("FieldCanBeLocal")
    private final int MAX_NUM_OF_IMAGE_QUIZ_TEXT_VIEW_LINES = 2;
    @SuppressWarnings("FieldCanBeLocal")
    private final int RATE_US_MINIMUM_ALLOWABLE_RATING = 4;
    int numOfLevels;
    int currentLevelNumber;
    int maximumAchievedLevel;
    boolean isGameEnd = false;
    boolean isRemoveCharsAvailable = true;
    boolean isSkipLevelAvailable = true;
    boolean isShowLetterAvailable = true;
    boolean isAdvertisementEnabled = true;
    boolean isPremiumActive = false;
    boolean isShareAppRewardEnabled = false;
    boolean isShareAppRewardAnimationEnabled = false;
    boolean isNextLevelAnimationEnabled = false;
    boolean isLevelInfoFragmentVisible = false;
    boolean isEndGameFragmentVisible = false;
    boolean isMessageFragmentShareAppVisible = false;
    boolean isMessageFragmentRefuseShareAppVisible = false;
    boolean isMessageFragmentRateUsVisible = false;
    boolean isMessageFragmentWatchVideoVisible = false;
    boolean isMessageFragmentAdvertisementNotLoadedVisible = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setFragmentsInvisible();
        statisticManager = new StatisticManager(context);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    private void setFragmentsInvisible() {
        isLevelInfoFragmentVisible = false;
        isEndGameFragmentVisible = false;
        isMessageFragmentRateUsVisible = false;
        isMessageFragmentShareAppVisible = false;
        isMessageFragmentRefuseShareAppVisible = false;
        isMessageFragmentWatchVideoVisible = false;
        isMessageFragmentAdvertisementNotLoadedVisible = false;
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
                soundIdShowLetter = soundPool.load(this, R.raw.button_show_letter_click, 1);
                soundIdRemoveLetters = soundPool.load(this, R.raw.button_remove_letters_click, 1);
                soundIdNotEnoughCoins = soundPool.load(this, R.raw.not_enough_coins, 1);
                soundIdInsertLetter = soundPool.load(this, R.raw.insert_letter, 1);
                soundIdCantInsertLetter = soundPool.load(this, R.raw.cant_insert_letter, 1);
                soundIdRemoveLetter = soundPool.load(this, R.raw.remove_letter, 1);
                soundIdWrongAnswer = soundPool.load(this, R.raw.wrong_answer, 1);
                soundIdNextLevel = soundPool.load(this, R.raw.correct_answer, 1);
                soundIdEndGame = soundPool.load(this, R.raw.end_game, 1);
            }
        }
    }

    private void loadData() {
        DataForGameActivity data = SaveManager.getDataForGameActivity(context);
        isAdvertisementEnabled = data.getAdvertisementEnabled();
        isPremiumActive = data.getIsPremiumActive();
        isShareAppRewardEnabled = data.getIsShareAppRewardEnabled();
        isShareAppRewardAnimationEnabled = data.getIsShareAppRewardAnimationEnabled();
        currentLevelNumber = data.getCurrentLevel();
        maximumAchievedLevel = data.getMaxAchievedLevel();
        isRemoveCharsAvailable = data.getIsRemoveCharsAvailable();
        isVolumeOn = data.getIsVolumeOn();
        isShowLetterAvailable = true;
    }

    @SuppressWarnings("All")
    private void viewsInitialization() {
        setContentView(R.layout.activity_game);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        topLayout = (RelativeLayout) findViewById(R.id.layout_top);
        textViewCoins = (TextView) findViewById(R.id.text_view_coins);
        levelTextView = (TextView) findViewById(R.id.text_view_top_title);
        levelTextView.setMovementMethod(new ScrollingMovementMethod());
        coinsImgTopLayout = (ImageView) findViewById(R.id.image_view_coin);
        quizContainer = (LinearLayout) findViewById(R.id.quiz_container);
        quizImageTextContainer = (RelativeLayout) findViewById(R.id.quiz_image_text_container);
        btnShowLetter = new ButtonView(
                this,
                getResources().getDrawable(R.drawable.icon_hint_put_character), true,
                getResources().getDrawable(R.drawable.icon_coin_put_character), false,
                getResources().getDimension(R.dimen.game_activity_hint_button_width),
                getResources().getDimension(R.dimen.game_activity_hint_button_height));
        btnShowLetterContainer = (LinearLayout) findViewById(R.id.btn_left_hint_container);
        btnShowLetterContainer.addView(btnShowLetter);
        btnRemoveLetters = new ButtonView(
                this,
                getResources().getDrawable(R.drawable.icon_coin_remove_characters), false,
                getResources().getDrawable(R.drawable.icon_hint_remove_characters), true,
                getResources().getDimension(R.dimen.game_activity_hint_button_width),
                getResources().getDimension(R.dimen.game_activity_hint_button_height));
        if (!isRemoveCharsAvailable) {
            btnRemoveLetters.setButtonClickable(false);
        }
        btnRemoveLettersContainer = (LinearLayout) findViewById(R.id.btn_right_hint_container);
        btnRemoveLettersContainer.addView(btnRemoveLetters);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnAskFriend = new ButtonView(this, getResources().getDrawable(R.drawable.icon_ask_friend),
                true,
                getResources().getDimension(R.dimen.game_activity_button_size),
                getResources().getDimension(R.dimen.game_activity_button_size));
        LinearLayout btnAskFriendContainer = (LinearLayout) findViewById(R.id.btn_ask_friend_container);
        btnAskFriendContainer.addView(btnAskFriend);
        btnStore = new ButtonView(this, getResources().getDrawable(R.drawable.icon_store),
                true,
                getResources().getDimension(R.dimen.game_activity_button_size),
                getResources().getDimension(R.dimen.game_activity_button_size));
        btnStoreContainer = (LinearLayout) findViewById(R.id.btn_store_container);
        btnStoreContainer.addView(btnStore);
        setButtonsListeners();
        textViewCoins.setText(LocaleTextHelper.getLocaleNumber(SaveManager.getCoins(context)));
        levelManager = new LevelManager(context);
        numOfLevels = levelManager.getNumOfLevels();
        currentLevel = levelManager.getFullLevelInfo(currentLevelNumber);
        if (currentLevel != null) {
            loadLevel(currentLevel);
            loadLevelContent(currentLevel);
        } else {
            isGameEnd = true;
        }
        if (isShareAppRewardAnimationEnabled) {
            SaveManager.setShareAppRewardAnimationEnabled(context, false);
            changeCoinsNumber(ApplicationData.getShareAppReward());
            saveData();
        }
        initializeLevelInfoContainer();
        messageFragmentContainer.setVisibility(View.GONE);
    }

    private void saveData() {
        LaunchManager.checkDataValid(context);
        Symbol[] enteredChars = answerFragment.getEnteredChars();
        boolean[] isVisibleItem = charsFragment.getIsVisibleItem();
        if (enteredChars != null && isVisibleItem != null) {
            SaveManager.saveDataFromGameActivity(context, currentLevelNumber, maximumAchievedLevel,
                    enteredChars, charsFragment.getIsVisibleItem(), isRemoveCharsAvailable);
        }
    }

    private void setButtonsListeners() {
        btnAskFriend.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonClick);
                isGlobalLayoutListenerAvailable = true;
                final RelativeLayout bannerLayout = drawBannerLayout();
                ViewTreeObserver bannerViewTreeObserver = bannerLayout.getViewTreeObserver();
                bannerViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (isGlobalLayoutListenerAvailable) {
                            String screenshotName = getResources().getString(R.string.text_screenshot) +
                                    String.valueOf(currentLevel.getId()) + ".jpg";
                            File file = takeScreenshot(screenshotName);
                            if (file != null) {
                                Uri uriImage;
                                String askFriendText =
                                        getResources().getString(R.string.ask_friend_text) +
                                                "http://play.google.com/store/apps/details?id=" + context.getPackageName() + ")";
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, askFriendText);
                                shareIntent.setType("image/jpeg");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uriImage = FileProvider.getUriForFile(GameActivity.this,
                                            BuildConfig.APPLICATION_ID + ".provider", file);
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                } else {
                                    uriImage = Uri.fromFile(file);
                                }
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uriImage);
                                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.ask_friend_title)));
                            }
                            isGlobalLayoutListenerAvailable = false;
                        }
                    }
                });
                cleanCacheDirectory();
            }
        });
        btnStore.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonClick);
                Intent intent = new Intent(GameActivity.this, StoreActivity.class);
                intent.putExtra("isFromGameActivity", true);
                startActivity(intent);
            }
        });
        btnShowLetter.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (!isGameEnd && isShowLetterAvailable) {
                    if (isPremiumActive) {
                        showLetter(false);
                    } else if (SaveManager.getCoins(context) >= ApplicationData.getShowLetterCost()) {
                        showLetter(true);
                    } else {
                        notEnoughCoins();
                    }
                }
            }
        });
        btnRemoveLetters.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (!isGameEnd && isRemoveCharsAvailable) {
                    if (isPremiumActive) {
                        removeLetters(false);
                    } else if (SaveManager.getCoins(context) >= ApplicationData.getRemoveLettersCost()) {
                        removeLetters(true);
                    } else {
                        notEnoughCoins();
                    }
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
        ButtonBack.SetOnTouchListenerAndColorFilter(context, btnBack);
    }

    private void notEnoughCoins() {
        playSound(soundIdNotEnoughCoins);
        Animation animation = AnimationUtils.loadAnimation(getApplication(),
                R.anim.animation_store_btn_shake);
        btnStoreContainer.startAnimation(animation);
        btnAskFriend.startAnimation(animation);
        shareAppOrWatchVideo();
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        playSound(soundIdButtonClick);
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    private void removeLetters(boolean isNeedToChangeCoinsNumber) {
        playSound(soundIdRemoveLetters);
        isInsertRemoveSoundEnabled = false;
        if (isNeedToChangeCoinsNumber) {
            changeCoinsNumber(-ApplicationData.getRemoveLettersCost());
        }
        statisticManager.removeLetters();
        Symbol[] unlockedSymbols = answerFragment.getSymbolsByLock(false);
        int numOfUnlocked = unlockedSymbols.length;
        int[] unlockedPositions = new int[numOfUnlocked];
        for (int index = 0; index < numOfUnlocked; index++) {
            unlockedPositions[index] = unlockedSymbols[index].getPosition();
        }
        charsFragment.showSymbols(unlockedPositions);
        currentLevel = levelManager.getFullLevelInfo(currentLevelNumber);
        if (currentLevel != null) {
            String[] answer = getStringFromSymbol(currentLevel.getAnswer());
            charsFragment.removeIncorrectSymbols(answer, null);
        }
        isRemoveCharsAvailable = false;
        btnRemoveLetters.setButtonClickable(false);
        saveData();
        isInsertRemoveSoundEnabled = true;
    }

    private void showLetter(boolean isNeedToChangeCoinsNumber) {
        isWrongAnswerEnabled = false;
        if (isNeedToChangeCoinsNumber) {
            changeCoinsNumber(-ApplicationData.getShowLetterCost());
        }
        playSound(soundIdShowLetter);
        isInsertRemoveSoundEnabled = false;
        statisticManager.showLetter();
        answerFragment.addSymbolHint();
        saveData();
        isInsertRemoveSoundEnabled = true;
        isWrongAnswerEnabled = true;
    }

    private void loadLevel(Level level) {
        charsFragment = new CharsFragment();
        sendDataToCharsFragment(getStringFromSymbol(level.getKeyboardChars()));
        answerFragment = new AnswerFragment();
        sendDataToAnswerFragment(
                getStringFromSymbol(level.getAnswer()),
                level.getLinesIndex(),
                null);
    }

    @SuppressWarnings("deprecation")
    private void loadLevelContent(Level level) {
        quizContainer.removeAllViews();
        quizImageTextContainer.removeAllViews();
        final String imageName = level.getImage();
        LoadDrawablesManager loadDrawablesManager = new LoadDrawablesManager(getApplicationContext());
        Drawable drawable = loadDrawablesManager.getDrawable(
                "quiz_images/" + imageName + ".png",
                IMAGE_WIDTH_FACTOR, 1.0f);
        if (drawable != null) {
            ImageView imageViewQuiz = new ImageView(context);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
            );
            imageParams.gravity = Gravity.CENTER;
            imageViewQuiz.setLayoutParams(imageParams);
            imageViewQuiz.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewQuiz.setAdjustViewBounds(true);
            imageViewQuiz.setImageDrawable(drawable);
            imageViewQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createFullScreenImageFragment(imageName);
                }
            });
            quizContainer.addView(imageViewQuiz);
        }
        textViewQuiz = getTextViewContainer(MAX_NUM_OF_IMAGE_QUIZ_TEXT_VIEW_LINES);
        quizImageTextContainer.addView(textViewQuiz);
        textViewQuiz.setText(getResources().getString(R.string.game_activity_question));
        levelTextView.setText(LocaleTextHelper.getLevelNumberText(context, level.getId()));
        if (isNextLevelAnimationEnabled) {
            Animation moveLeftToRight = AnimationUtils.loadAnimation(getApplication(),
                    R.anim.move_left_to_right);
            quizContainer.startAnimation(moveLeftToRight);
            quizImageTextContainer.startAnimation(moveLeftToRight);
            levelTextView.startAnimation(moveLeftToRight);
        }
        isNextLevelAnimationEnabled = false;
    }

    @SuppressWarnings("deprecation")
    private TextView getTextViewContainer(int maxNumOfLines) {
        final TextView textViewQuiz = new TextView(this);
        if (maxNumOfLines == MAX_NUM_OF_IMAGE_QUIZ_TEXT_VIEW_LINES) {
            textViewQuiz.setMaxLines(maxNumOfLines);
            textViewQuiz.setHorizontalScrollBarEnabled(true);
            textViewQuiz.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.game_activity_image_quiz_font_size));
                textViewQuiz.post(new Runnable() {
                    @Override
                    public void run() {
                        if (textViewQuiz.getLineCount() > 2) {
                            int height = textViewQuiz.getLineHeight() * (textViewQuiz.getLineCount() - 2);
                            int START_DELAY_ANIMATOR = 3000;
                            int DURATION_ANIMATOR = 4000;
                            final ObjectAnimator animator = ObjectAnimator.ofInt(textViewQuiz, "scrollY", height);
                            animator.setStartDelay(START_DELAY_ANIMATOR);
                            animator.setDuration(DURATION_ANIMATOR);
                            animator.start();

                            textViewQuiz.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    animator.cancel();
                                }
                            });
                        }
                    }
                });
        } else {
            textViewQuiz.setMaxLines(maxNumOfLines);
            textViewQuiz.setVerticalScrollBarEnabled(true);
            textViewQuiz.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.game_activity_quiz_font_size));
        }
        textViewQuiz.setTextColor(getResources().getColor(R.color.textOnActivityBackground));
        textViewQuiz.setMovementMethod(new ScrollingMovementMethod());
        textViewQuiz.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsTextQuiz = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTextQuiz.setMargins(
                (int)getResources().getDimension(R.dimen.game_activity_quiz_text_view_vertical_margin), 0,
                (int)getResources().getDimension(R.dimen.game_activity_quiz_text_view_vertical_margin), 0);
        textViewQuiz.setLayoutParams(layoutParamsTextQuiz);
        return textViewQuiz;
    }

    @Override
    public void sendDataFromCharsFragment(Symbol symbol) {
        if (answerFragment.addChar(symbol)) {
            playSound(soundIdInsertLetter);
            charsFragment.removeSymbol(symbol.getPosition());
            saveData();
        } else {
            playSound(soundIdCantInsertLetter);
        }
    }

    public void sendDataFromAnswerFragment(Symbol symbol) {
        playSound(soundIdRemoveLetter);
        charsFragment.showSymbol(symbol.getPosition());
        saveData();
    }

    public Symbol getSymbolFromCharsFragment(String character) {
        return charsFragment.getSymbolWithChar(character);
    }

    @Override
    public void swapTwoSymbols(Symbol fromCharsToAnswer, Symbol fromAnswerToChars, int indexForLock) {
        charsFragment.removeSymbol(fromCharsToAnswer.getPosition());
        if (fromAnswerToChars.getPosition() != -1) {
            answerFragment.removeSymbolFromAnswerField(indexForLock);
            charsFragment.showSymbol(fromAnswerToChars.getPosition());
        }
        answerFragment.setLockedSymbol(fromCharsToAnswer, indexForLock);
        saveData();
    }

    public void sendCorrectAnswerMessage() {
        LaunchManager.checkDataValid(context);
        if (SaveManager.getStatCurrentLevelWithoutMistakes(context)) {
            SaveManager.setStatAnswersWithoutMistakes(
                    context, SaveManager.getStatAnswersWithoutMistakes(context) + 1);
        }
        SaveManager.setStatCurrentLevelWithoutMistakes(context, true);
        statisticManager.correctAnswer();
        int levelNumberToDisplay = currentLevelNumber;
        currentLevelNumber++;
        currentLevel = levelManager.getFullLevelInfo(currentLevelNumber);
        boolean isNewLevelAchieved = false;
        if (maximumAchievedLevel < currentLevelNumber) {
            maximumAchievedLevel = currentLevelNumber;
            isNewLevelAchieved = true;
        }
        isRemoveCharsAvailable = false;
        btnRemoveLetters.setButtonClickable(false);
        isShowLetterAvailable = false;
        isSkipLevelAvailable = false;
        if (currentLevel != null) {
            createLevelInfoFragment(levelNumberToDisplay, getResources().getString(R.string.level_info_btn_text));
            if (isNewLevelAchieved) {
                changeCoinsNumber(ApplicationData.getCorrectAnswerReward());
            }
            loadLevel(currentLevel);
        } else {
            SaveManager.setStatAnswersNeedToSend(context, true);
            createEndGameFragment(levelNumberToDisplay, getResources().getString(R.string.btn_exit));
        }
        SaveManager.saveDataFromGameActivity(context, currentLevelNumber,
                maximumAchievedLevel,null, null, true);
    }

    private void restoreLevelInfoFragment(int levelId, boolean isEndGame) {
        if (!isEndGame) {
            createLevelInfoFragment(levelId, getResources().getString(R.string.level_info_btn_text));
        } else {
            createEndGameFragment(levelId, getResources().getString(R.string.btn_exit));
        }
    }

    public void sendWrongAnswerMessage() {
        if (!isWrongAnswerEnabled) {
            return;
        }
        SaveManager.setStatCurrentLevelWithoutMistakes(context, false);
        Animation animation = AnimationUtils.loadAnimation(getApplication(),
                R.anim.hint_btns_blinking);
        btnShowLetterContainer.startAnimation(animation);
        btnRemoveLettersContainer.startAnimation(animation);
        playSound(soundIdWrongAnswer);
    }

    private void viewAdvertisement() {

    }

    private void playSound(int id) {
        if (!isInsertRemoveSoundEnabled) {
            return;
        }
        if (isVolumeOn) {
            if (soundPool != null) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        }
    }

    @Override
    public void closeLevelInfoFragment() {
        if (currentLevel != null) {
            isNextLevelAnimationEnabled = true;
            loadLevelContent(currentLevel);
            isRemoveCharsAvailable = true;
            isShowLetterAvailable = true;
            isSkipLevelAvailable = true;
            btnRemoveLetters.setButtonClickable(true);
        }
        if (currentLevelNumber == ApplicationData.getRateUsRequestLevel()) {
            rateUs();
        }
        viewAdvertisement();
    }

    private void sendDataToAnswerFragment(String[] data, int[] linesIndex, int[] specSymbolsIndexes) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("answer", data);
        bundle.putIntArray("lines_index", linesIndex);
        bundle.putIntArray("spec_symbols_indexes", specSymbolsIndexes);
        answerFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_answer_container, answerFragment, "fragment_answer");
            fragmentTransaction.commit();
        }
    }

    private void sendDataToCharsFragment(String[] data) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("chars", data);
        charsFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_chars_container, charsFragment, "fragment_chars");
            fragmentTransaction.commit();
        }
    }

    private void createFullScreenImageFragment(String imageName) {
        messageFragmentContainer.setVisibility(View.VISIBLE);
        fullScreenImageFragment = new FullScreenImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image_name", imageName);
        fullScreenImageFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), fullScreenImageFragment,
                    "full_screen_image_fragment");
            fragmentTransaction.commit();
        }
    }

    private void createLevelInfoFragment(int levelId, String btnText) {
        isLevelInfoFragmentVisible = true;
        isInsertRemoveSoundEnabled = true;
        playSound(soundIdNextLevel);
        messageFragmentContainer.setVisibility(View.VISIBLE);
        levelInfoFragment = new LevelInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("level", levelManager.getFullLevelInfo(levelId));
        bundle.putString("btn_text", btnText);
        levelInfoFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), levelInfoFragment,
                    "level_info_fragment");
            fragmentTransaction.commit();
            levelInfoFragment.setFragmentButtonClickListener(new LevelInfoFragment.FragmentButtonClickListener() {
                @Override
                public void FragmentButtonClickListener() {
                    playSound(soundIdButtonClick);
                    if (!getIsActivityFinished()) {
                        getFragmentManager().beginTransaction().
                                remove(getFragmentManager().findFragmentById(R.id.id_message_fragment)).commit();
                    }
                    closeLevelInfoFragment();
                    isLevelInfoFragmentVisible = false;
                }
            });
        }
    }

    private void createEndGameFragment(int levelId, String btnText) {
        isEndGameFragmentVisible = true;
        isInsertRemoveSoundEnabled = true;
        playSound(soundIdEndGame);
        messageFragmentContainer.setVisibility(View.VISIBLE);
        levelInfoFragment = new LevelInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("level", levelManager.getFullLevelInfo(levelId));
        bundle.putString("btn_text", btnText);
        bundle.putBoolean("is_end_game_fragment", true);
        levelInfoFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), levelInfoFragment,
                    "level_info_fragment");
            fragmentTransaction.commit();
            playSound(soundIdEndGame);
            levelInfoFragment.setFragmentButtonClickListener(new LevelInfoFragment.FragmentButtonClickListener() {
                @Override
                public void FragmentButtonClickListener() {
                    playSound(soundIdButtonClick);
                    isEndGameFragmentVisible = false;
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    private void initializeLevelInfoContainer() {
        messageFragmentContainer = new FrameLayout(context);
        messageFragmentContainer.setId(R.id.id_message_fragment);
        RelativeLayout.LayoutParams fragmentLevelInfoParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fragmentLevelInfoParams.addRule(RelativeLayout.BELOW, R.id.layout_top);
        fragmentLevelInfoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        messageFragmentContainer.setLayoutParams(fragmentLevelInfoParams);
        if (mainLayout != null) {
            mainLayout.addView(messageFragmentContainer);
        }
    }

    private void changeCoinsNumber(int number) {
        Animation animation = AnimationUtils.loadAnimation(getApplication(),
                R.anim.anim_num_of_coins_chage);
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(
                SaveManager.getCoins(context), SaveManager.getCoins(context) + number);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                String coinsString = "" + LocaleTextHelper.getLocaleNumber((int) animation.getAnimatedValue());
                textViewCoins.setText(coinsString);
            }
        });
        SaveManager.changeCoinsNumber(context, number);
        valueAnimator.start();
        coinsImgTopLayout.startAnimation(animation);
    }

    private String[] getStringFromSymbol(Symbol[] symbol) {
        int length = symbol.length;
        String[] string = new String[length];
        for (int x = 0; x < length; x++) {
            string[x] = symbol[x].getString();
        }
        return string;
    }

    private void getClearMainLayout() {
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.removeAllViews();
        }
    }

    private MessageFragment createMessageFragment(String title, String message, String btnText) {
        return createMessageFragment(title, message, btnText, null, null);
    }

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText,
                                                  String btnSecondText) {
        return createMessageFragment(title, message, btnFirstText, btnSecondText, null);
    }

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText,
                                       String btnSecondText, String btnThirdText) {
        messageFragmentContainer.setVisibility(View.VISIBLE);
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("btn_first_text", btnFirstText);
        if (btnSecondText != null) {
            bundle.putString("btn_second_text", btnSecondText);
        }
        if (btnThirdText != null) {
            bundle.putString("btn_third_text", btnThirdText);
        }
        messageFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), messageFragment,
                    "message_fragment");
            fragmentTransaction.commit();
        }
        return messageFragment;
    }

    private RateUsFragment createRateUsFragment() {
        messageFragmentContainer.setVisibility(View.VISIBLE);
        RateUsFragment rateUsFragment = new RateUsFragment();
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), rateUsFragment,
                    "rate_us_fragment");
            fragmentTransaction.commit();
        }
        return rateUsFragment;
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

    private void closeMessageFragment() {
        if (!getIsActivityFinished()) {
            getFragmentManager().beginTransaction().
                    remove(getFragmentManager().findFragmentById(R.id.id_message_fragment)).commit();
        }
    }

    @SuppressWarnings("deprecation")
    private void cleanCacheDirectory() {
        File dir = context.getExternalCacheDir();
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private RelativeLayout drawBannerLayout() {
        int buttonSize = (int) (getResources().getDimension(R.dimen.top_layout_button_back_size));
        int headerPadding = (int)getResources().getDimension(R.dimen.top_layout_padding);
        RelativeLayout bannerLayout = new RelativeLayout(this);
        bannerLayout.setBackgroundColor(getResources().getColor(R.color.headerBackgroundStart));
        RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, buttonSize + 2 * headerPadding);
        bannerLayoutParams.setMargins(-headerPadding, -headerPadding, -headerPadding, -headerPadding);
        bannerLayout.setPadding(headerPadding, headerPadding, headerPadding, headerPadding);
        bannerLayout.setLayoutParams(bannerLayoutParams);
        TextView bannerTextView = new TextView(this);
        bannerTextView.setText(getResources().getString(R.string.app_name));
        bannerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.top_layout_font_size));
        bannerTextView.setTextColor(getResources().getColor(R.color.textOnTopLayout));
        RelativeLayout.LayoutParams bannerTextViewLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bannerTextView.setTypeface(null, Typeface.BOLD);
        bannerTextViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        bannerTextViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        bannerTextView.setLayoutParams(bannerTextViewLayoutParams);
        bannerTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        ImageView appIcon = new ImageView(this);
        appIcon.setImageDrawable(getApplicationInfo().loadIcon(getPackageManager()));
        RelativeLayout.LayoutParams appIconLayoutParams = new RelativeLayout.LayoutParams(
                buttonSize, buttonSize);
        appIconLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        appIconLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        appIcon.setLayoutParams(appIconLayoutParams);
        bannerLayout.addView(bannerTextView);
        bannerLayout.addView(appIcon);
        topLayout.addView(bannerLayout);
        return bannerLayout;
    }

    private File takeScreenshot(String screenshotName) {
        File imageFile = null;
        try {
            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            imageFile = new File(context.getExternalCacheDir(), screenshotName);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
        return imageFile;
    }

    @SuppressWarnings("All")
    private void clickOnRateApp(int rating) {
        if (rating >= RATE_US_MINIMUM_ALLOWABLE_RATING) {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
                PackageManager packageManager = getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                }
            }
        } else {
            String mailTitle = getResources().getString(R.string.app_name) +
                    " " +
                    rating + getResources().getString(R.string.message_rate_us_mail_title_2);
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setType("message/rfc822");
            i.setData(Uri.parse("mailto:")); // only email apps should handle this
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_feedback)});
            i.putExtra(Intent.EXTRA_SUBJECT, mailTitle);
            i.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.message_rate_us_mail_message));
            try {
                startActivity(Intent.createChooser(i, getResources().getString(
                        R.string.message_rate_us_mail_client_chooser_message)));
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void clickOnWatchVideo() {

    }

    private void rateUs() {
        isMessageFragmentRateUsVisible = true;
        RateUsFragment rateUsFragment = createRateUsFragment();
        rateUsFragment.setButtonLaterClickListener(new RateUsFragment.ButtonLaterClickListener() {
            @Override
            public void ButtonLaterClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                isMessageFragmentRateUsVisible = false;
            }
        });
        rateUsFragment.setButtonRateClickListener(new RateUsFragment.ButtonRateClickListener() {
            @Override
            public void ButtonRateClickListener(int rating) {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                clickOnRateApp(rating);
                isMessageFragmentRateUsVisible = false;
            }
        });
    }

    private void watchVideo() {
        isMessageFragmentWatchVideoVisible = true;
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
                isMessageFragmentWatchVideoVisible = false;
            }
        });
        watchVideoFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                clickOnWatchVideo();
                isMessageFragmentWatchVideoVisible = false;
            }
        });
    }

    private void shareAppOrWatchVideo() {
        if (isShareAppRewardEnabled) {
            isMessageFragmentShareAppVisible = true;
            final MessageFragment shareAppFragment = createMessageFragment(
                    getResources().getString(R.string.not_enough_coins),
                    getResources().getString(R.string.message_fragment_text_share_app),
                    getResources().getString(R.string.message_fragment_btn_refuse),
                    getResources().getString(R.string.message_fragment_btn_later),
                    getResources().getString(R.string.message_fragment_btn_share)
            );
            shareAppFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
                @Override
                public void FirstButtonClickListener() {
                    playSound(soundIdButtonClick);
                    closeMessageFragment();
                    refuseShareAppReward();
                    isMessageFragmentShareAppVisible = false;
                }
            });
            shareAppFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
                @Override
                public void SecondButtonClickListener() {
                    playSound(soundIdButtonClick);
                    closeMessageFragment();
                    isMessageFragmentShareAppVisible = false;
                }
            });
            shareAppFragment.setThirdButtonClickListener(new MessageFragment.ThirdButtonClickListener() {
                @Override
                public void ThirdButtonClickListener() {
                    playSound(soundIdButtonClick);
                    closeMessageFragment();
                    shareAppAccepted();
                    isMessageFragmentShareAppVisible = false;
                }
            });
        }
    }

    private void shareAppAccepted() {
        String shareAppText =
                getResources().getString(R.string.share_app_text) +
                        "http://play.google.com/store/apps/details?id=" + context.getPackageName();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareAppText);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_app_title)));
        SaveManager.setShareAppRewardAnimationEnabled(context, true);
        SaveManager.setShareAppRewardEnabled(context, false);
    }

    private void refuseShareAppReward() {
        isMessageFragmentRefuseShareAppVisible = true;
        final MessageFragment refuseShareAppFragment = createMessageFragment(
                getResources().getString(R.string.message_fragment_title_refuse_share_app),
                getResources().getString(R.string.message_fragment_text_refuse_share_app),
                getResources().getString(R.string.message_fragment_btn_refuse),
                getResources().getString(R.string.message_fragment_btn_share)
        );
        refuseShareAppFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                isShareAppRewardEnabled = false;
                SaveManager.setShareAppRewardEnabled(context, false);
                closeMessageFragment();
                isMessageFragmentRefuseShareAppVisible = false;
            }
        });
        refuseShareAppFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                shareAppAccepted();
                isMessageFragmentRefuseShareAppVisible = false;
            }
        });
    }

    private void showAdvertisementNotLoaded() {
        isMessageFragmentAdvertisementNotLoadedVisible = true;
        final MessageFragment advertisementNotLoadedFragment = createMessageFragment(
                getResources().getString(R.string.message_fragment_title_advertisement_not_loaded),
                getResources().getString(R.string.message_fragment_text_advertisement_not_loaded),
                getResources().getString(R.string.message_fragment_btn_close)
        );
        advertisementNotLoadedFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                isMessageFragmentAdvertisementNotLoadedVisible = false;
            }
        });
    }

    private void showMessageFragments() {
        if (isLevelInfoFragmentVisible) {
            restoreLevelInfoFragment(currentLevelNumber - 1, false);
        } else if (isMessageFragmentShareAppVisible) {
            shareAppOrWatchVideo();
        }
        else if (isMessageFragmentRefuseShareAppVisible) {
            refuseShareAppReward();
        }
        else if (isMessageFragmentRateUsVisible) {
            rateUs();
        }
        else if (isMessageFragmentWatchVideoVisible) {
            watchVideo();
        }
        else if (isMessageFragmentAdvertisementNotLoadedVisible) {
            showAdvertisementNotLoaded();
        } else if (isEndGameFragmentVisible) {
            restoreLevelInfoFragment(currentLevelNumber - 1, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LaunchManager.loadLocale(context);
        getClearMainLayout();
        loadData();
        LaunchManager.checkDataValid(context);
        loadSounds();
        viewsInitialization();
        showMessageFragments();
    }

    @Override
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