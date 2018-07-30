package com.loop.quizapp;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Context context;

    FrameLayout messageFragmentContainer;

    ImageView coinsImgTopLayout;
    TextView textViewCoins;

    boolean isVolumeOn;
    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    int soundIdButtonClick;
    int soundIdButtonMainMenuClick;

    boolean isGameEnd = false;
    boolean isMessageFragmentGameIsOverVisible = false;
    boolean isPrivacyPolicyFragmentVisible = false;
    boolean isNeedToDisplayWelcomeFragment = false;
    boolean isWelcomeFragmentVisible = false;
    boolean isAboutAppFragmentVisible = false;

    @Override
    @SuppressWarnings("")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        if (LaunchManager.isFirstLaunch(context)) {
            isNeedToDisplayWelcomeFragment = true;
        } else if (getIntent().getBooleanExtra(
                "isNeedToDisplayWelcomeFragmentFromOptionsActivity", false)) {
            isNeedToDisplayWelcomeFragment = true;
        } else if (getIntent().getBooleanExtra(
                "isPrivacyPolicyAcceptedAndNeedToDisplayWelcomeFragment", false)) {
            isWelcomeFragmentVisible = true;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @SuppressWarnings("all")
    private void viewsInitialization() {
        setContentView(R.layout.activity_main);
        ImageView appIcon = findViewById(R.id.image_view_app_icon);
        appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(soundIdButtonClick);
                createAboutAppFragment();
            }
        });
        ButtonMainActivityView btnNormalGame = new ButtonMainActivityView(
                context, getResources().getDrawable(R.drawable.icon_normal_game),
                true,
                getResources().getString(R.string.game_mode_normal_game));
        btnNormalGame.setButtonClickListener(new ButtonMainActivityView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonMainMenuClick);
                if (!isGameEnd) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);
                } else {
                    gameIsOver();
                }
            }
        });
        LinearLayout btnContainerNormalGame = (LinearLayout) findViewById(R.id.btn_container_normal_game);
        btnContainerNormalGame.addView(btnNormalGame);

        ButtonMainActivityView btnStatistic = new ButtonMainActivityView(
                context, getResources().getDrawable(R.drawable.icon_statistic),
                true,
                getResources().getString(R.string.statistics));
        btnStatistic.setButtonClickListener(new ButtonMainActivityView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonMainMenuClick);
                Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout btnContainerStatistic = (LinearLayout) findViewById(R.id.btn_container_statistic);
        btnContainerStatistic.addView(btnStatistic);

        ButtonMainActivityView btnSelectGameMode = new ButtonMainActivityView(
                context, getResources().getDrawable(R.drawable.icon_game_modes),
                true,
                getResources().getString(R.string.game_modes));
        btnSelectGameMode.setButtonClickListener(new ButtonMainActivityView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonMainMenuClick);
                if (isOtherGameModsUnlocked()) {
                    Intent intent = new Intent(MainActivity.this, SelectGameModeActivity.class);
                    startActivity(intent);
                } else {
                    otherGameModesNoAccess();
                }
            }
        });
        LinearLayout btnSelectGameModeContainer = (LinearLayout) findViewById(R.id.btn_container_select_game_modes);
        btnSelectGameModeContainer.addView(btnSelectGameMode);

        RelativeLayout.LayoutParams btnContainerParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        btnContainerParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        ButtonView btnAchievements = new ButtonView(
                context, getResources().getDrawable(R.drawable.icon_achievements),
                true);
        btnAchievements.setLayoutParams(btnContainerParams);
        btnAchievements.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonClick);
            }
        });

        RelativeLayout btnAchievementContainer = (RelativeLayout) findViewById(R.id.btn_container_1);
        btnAchievementContainer.addView(btnAchievements);
        ButtonView btnLeaderboard = new ButtonView(
                context, getResources().getDrawable(R.drawable.icon_leaderboard),
                true);
        btnLeaderboard.setLayoutParams(btnContainerParams);
        btnLeaderboard.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonClick);
            }
        });
        RelativeLayout btnLeaderboardContainer = (RelativeLayout) findViewById(R.id.btn_container_2);
        btnLeaderboardContainer.addView(btnLeaderboard);

        ButtonView btnStore = new ButtonView(
                context, getResources().getDrawable(R.drawable.icon_store),
                true);
        btnStore.setLayoutParams(btnContainerParams);
        btnStore.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonClick);
                Intent intent = new Intent(MainActivity.this, StoreActivity.class);
                intent.putExtra("isFromGameActivity", false);
                startActivity(intent);
            }
        });
        RelativeLayout btnStoreContainer = (RelativeLayout) findViewById(R.id.btn_container_3);
        btnStoreContainer.addView(btnStore);

        ButtonView btnOptions = new ButtonView(
                context, getResources().getDrawable(R.drawable.icon_options),
                true);
        btnOptions.setLayoutParams(btnContainerParams);
        btnOptions.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonClick);
                Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(intent);
            }
        });
        RelativeLayout btnOptionsContainer = (RelativeLayout) findViewById(R.id.btn_container_4);
        btnOptionsContainer.addView(btnOptions);
        textViewCoins = (TextView) findViewById(R.id.text_view_coins);
        textViewCoins.setText(LocaleTextHelper.getLocaleNumber(SaveManager.getCoins(context)));
        coinsImgTopLayout = (ImageView) findViewById(R.id.image_view_coin);
        initializeMessageContainer();
    }

    private void playSound(int id) {
        if (isVolumeOn) {
            if (soundPool != null) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        }
    }

    private void loadData() {
        if (LaunchManager.isFirstLaunchAfterUpdate(context)) {
            SaveManager.resetLevelData(context);
        }
        isGameEnd = !isAvailableLevels(SaveManager.getCurrentLevel(context));
        isVolumeOn = SaveManager.getVolumeOn(context);
    }

    private boolean isOtherGameModsUnlocked() {
        return ((SaveManager.getMaximumAchievedLevel(context) >= ApplicationData.getMinimumRequiredLevelForOtherGameMods())
                || SaveManager.getIsGameModesUnlocked(context));
    }

    private boolean isAvailableLevels(int currentLevelNumber) {
        if (currentLevelNumber == -1) {
            currentLevelNumber = 0;
        }
        LevelManager levelManager = new LevelManager(getApplicationContext());
        Level currentLevel = levelManager.getFullLevelInfo(currentLevelNumber);
        return currentLevel != null;
    }

    @SuppressWarnings("all")
    private void initializeMessageContainer() {
        messageFragmentContainer = new FrameLayout(this);
        messageFragmentContainer.setId(R.id.id_message_fragment);
        RelativeLayout.LayoutParams fragmentMessageParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fragmentMessageParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        messageFragmentContainer.setLayoutParams(fragmentMessageParams);
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mainLayout.addView(messageFragmentContainer);
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

    private boolean getIsActivityFinished() {
        boolean isActivityFinished;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isActivityFinished = (isFinishing() && isDestroyed());
        } else {
            isActivityFinished = isFinishing();
        }
        return isActivityFinished;
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
                soundIdButtonMainMenuClick = soundPool.load(this, R.raw.button_main_menu_click, 1);
            }
        }
    }

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText) {
        return createMessageFragment(title, message, btnFirstText, null, null);
    }

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText, String btnSecondText) {
        return createMessageFragment(title, message, btnFirstText, btnSecondText, null);
    }

    private void gameIsOver() {
        isMessageFragmentGameIsOverVisible = true;
        MessageFragment gameCompleteFragment = createMessageFragment(
                getResources().getString(R.string.message_fragment_title_warning),
                getResources().getString(R.string.message_fragment_text_game_already_completed),
                getResources().getString(R.string.message_fragment_btn_close),
                getResources().getString(R.string.statistics).toLowerCase());
        gameCompleteFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                isMessageFragmentGameIsOverVisible = false;
            }
        });
        gameCompleteFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                isMessageFragmentGameIsOverVisible = false;
                Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });
    }

    private void otherGameModesNoAccess() {
        final int coinsForUnlock = (ApplicationData.getMinimumRequiredLevelForOtherGameMods()
                - SaveManager.getMaximumAchievedLevel(context))
                * ApplicationData.getUnlockOtherGameModsLevelCost();
        String fewLevelsString = String.format(
                getResources().getString(R.string.message_fragment_text_game_modes_few_level_passed),
                LocaleTextHelper.getLocaleNumber(ApplicationData.getMinimumRequiredLevelForOtherGameMods()),
                LocaleTextHelper.getLocaleNumber(coinsForUnlock));
        MessageFragment noAccessMessageFragment =
                createMessageFragment(
                        getResources().getString(R.string.message_fragment_title_warning),
                        fewLevelsString,
                        getResources().getString(R.string.message_fragment_btn_close),
                        getResources().getString(R.string.message_fragment_text_game_modes_few_level_passed_btn_more_info),
                        getResources().getString(R.string.message_fragment_text_game_modes_few_level_passed_btn_unlock));
        noAccessMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
        noAccessMessageFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                createGameModesMoreInfoFragment();
            }
        });
        noAccessMessageFragment.setThirdButtonClickListener(new MessageFragment.ThirdButtonClickListener() {
            @Override
            public void ThirdButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                if (SaveManager.getCoins(context) >= coinsForUnlock) {
                    unlockGameModes(coinsForUnlock);
                } else {
                    notEnoughCoinsMessage();
                }
            }
        });
        noAccessMessageFragment.setOutsideLayoutClickable(true);
    }

    private void createGameModesMoreInfoFragment() {
        boolean isTutorialAvailable = (SaveManager.getMaximumAchievedLevel(context)
                >= ApplicationData.getTutorialMinNumOfLevels());
        messageFragmentContainer.setVisibility(View.VISIBLE);
        AboutGameModesFragment aboutGameModesFragment = new AboutGameModesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_tutorial_available", isTutorialAvailable);
        aboutGameModesFragment.setArguments(bundle);
        aboutGameModesFragment.setButtonCloseClickListener(new AboutGameModesFragment.ButtonCloseClickListener() {
            @Override
            public void ButtonCloseClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
        aboutGameModesFragment.setButtonTutorialClickListener(
                new AboutGameModesFragment.ButtonTutorialClickListener() {
                    @Override
                    public void ButtonTutorialClickListener() {
                        playSound(soundIdButtonClick);
                        Intent intent = new Intent(MainActivity.this, TutorialGameFindThePairsActivity.class);
                        intent.putExtra("isFromSelectGameModesActivity", false);
                        startActivity(intent);
                    }
                });
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), aboutGameModesFragment,
                    "message_fragment");
            fragmentTransaction.commit();
        }
    }

    private void unlockGameModes(int price) {
        SaveManager.setGameModesUnlocked(context, true);
        changeCoinsNumber(- price);
    }

    private void notEnoughCoinsMessage() {
        MessageFragment notEnoughCoinsMessageFragment =
                createMessageFragment(
                        getResources().getString(R.string.message_fragment_title_error),
                        getResources().getString(R.string.not_enough_coins),
                        getResources().getString(R.string.message_fragment_btn_close));
        notEnoughCoinsMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
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

    private void createAboutAppFragment() {
        isAboutAppFragmentVisible = true;
        messageFragmentContainer.setVisibility(View.VISIBLE);
        AboutAppFragment aboutAppFragment = new AboutAppFragment();
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), aboutAppFragment,
                    "message_fragment");
            fragmentTransaction.commit();
            aboutAppFragment.setFragmentButtonClickListener(new AboutAppFragment.FragmentButtonClickListener() {
                @Override
                public void FragmentButtonClickListener() {
                    playSound(soundIdButtonClick);
                    isAboutAppFragmentVisible = false;
                    closeMessageFragment();
                }
            });
        }
    }

    private void createWelcomeFragment() {
        isWelcomeFragmentVisible = true;
        messageFragmentContainer.setVisibility(View.VISIBLE);
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), welcomeFragment,
                    "message_fragment");
            fragmentTransaction.commit();
            welcomeFragment.setPlayGamesDisableClickListener(new WelcomeFragment.PlayGamesDisableClickListener() {
                @Override
                public void PlayGamesDisableClickListener() {
                    SaveManager.setPlayGamesEnabled(context, false);
                    playSound(soundIdButtonClick);
                    isWelcomeFragmentVisible = false;
                    closeMessageFragment();
                }
            });
            welcomeFragment.setPlayGamesEnableClickListener(new WelcomeFragment.PlayGamesEnableClickListener() {
                @Override
                public void PlayGamesEnableClickListener() {
                    SaveManager.setPlayGamesEnabled(context, true);
                    playSound(soundIdButtonClick);
                    isWelcomeFragmentVisible = false;
                    closeMessageFragment();
                }
            });
        }
    }

    private void createPrivacyPolicyFragment() {
        isPrivacyPolicyFragmentVisible = true;
        messageFragmentContainer.setVisibility(View.VISIBLE);
        PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), privacyPolicyFragment,
                    "message_fragment");
            fragmentTransaction.commit();
            privacyPolicyFragment.setAcceptClickListener(new PrivacyPolicyFragment.AcceptClickListener() {
                @Override
                public void AcceptClickListener(boolean isAdsPersonalized) {
                    SaveManager.setPersonalizedAdvertisementEnabled(context, isAdsPersonalized);
                    SaveManager.setPrivacyPolicyAccepted(context, true);
                    playSound(soundIdButtonClick);
                    isPrivacyPolicyFragmentVisible = false;
                    closeMessageFragment();
                    if (isNeedToDisplayWelcomeFragment) {
                        createWelcomeFragment();
                    }
                }
            });
            privacyPolicyFragment.setReadPrivacyPolicyListener(new PrivacyPolicyFragment.ReadPrivacyPolicyListener() {
                @Override
                public void ReadPrivacyPolicyListener() {
                    Intent intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("isFromOptionsActivity", false);
                    intent.putExtra("isNeedToDisplayWelcomeFragment", isNeedToDisplayWelcomeFragment);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    private void closeMessageFragment() {
        if (!getIsActivityFinished()) {
            getFragmentManager().beginTransaction().
                    remove(getFragmentManager().findFragmentById(R.id.id_message_fragment)).commit();
        }
    }

    private void getClearMainLayout() {
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.removeAllViews();
        }
    }

    private void showMessageFragments() {
        if (isPrivacyPolicyFragmentVisible) {
            createPrivacyPolicyFragment();
        } else if (isWelcomeFragmentVisible) {
            createWelcomeFragment();
        } else if (isMessageFragmentGameIsOverVisible){
            gameIsOver();
        } else if (isAboutAppFragmentVisible) {
            createAboutAppFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LaunchManager.loadLocale(context);
        getClearMainLayout();
        viewsInitialization();
        loadData();
        LaunchManager.checkDataValid(context);
        loadSounds();
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
