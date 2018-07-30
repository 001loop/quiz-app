package com.loop.quizapp;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectGameModeActivity extends AppCompatActivity {

    Context context;

    FrameLayout messageFragmentContainer;

    ImageView btnBack;
    ImageView coinsImgTopLayout;
    TextView textViewCoins;

    boolean isVolumeOn;
    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    int soundIdButtonClick;
    int soundIdButtonMainMenuClick;

    @Override
    @SuppressWarnings("")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

    @SuppressWarnings("all")
    private void viewsInitialization() {
        setContentView(R.layout.activity_select_game_mode);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
        ButtonBack.SetOnTouchListenerAndColorFilter(context, btnBack);

        ButtonMainActivityView btnMoreInfo = new ButtonMainActivityView(
                context, getResources().getDrawable(R.drawable.icon_game_modes_more_info),
                true,
                getResources().getString(R.string.about_game_modes));
        btnMoreInfo.setButtonClickListener(new ButtonMainActivityView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonMainMenuClick);
                createGameModesMoreInfoFragment();
            }
        });
        LinearLayout btnMoreInfoContainer = (LinearLayout) findViewById(R.id.btn_container_game_modes_more_info);
        btnMoreInfoContainer.addView(btnMoreInfo);

        ButtonMainActivityView btnTimeChallenge = new ButtonMainActivityView(
                context, getResources().getDrawable(R.drawable.icon_game_time_challenge),
                true,
                getResources().getString(R.string.game_mode_time_challenge));
        btnTimeChallenge.setButtonClickListener(new ButtonMainActivityView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonMainMenuClick);
                Intent intent = new Intent(SelectGameModeActivity.this, GameTimeChallengeActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout btnTimeChallengeContainer = (LinearLayout) findViewById(R.id.btn_container_game_mode_time_challenge);
        btnTimeChallengeContainer.addView(btnTimeChallenge);

        ButtonMainActivityView btnGameModeYesOrNo = new ButtonMainActivityView(
                context, getResources().getDrawable(R.drawable.icon_game_yes_or_no),
                true,
                getResources().getString(R.string.game_mode_yes_or_no));
        btnGameModeYesOrNo.setButtonClickListener(new ButtonMainActivityView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonMainMenuClick);
                Intent intent = new Intent(SelectGameModeActivity.this, GameYesOrNoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout btnGameModeYesOrNoContainer = (LinearLayout) findViewById(R.id.btn_container_game_mode_yes_or_no);
        btnGameModeYesOrNoContainer.addView(btnGameModeYesOrNo);

        ButtonMainActivityView btnGameModeFindThePairs = new ButtonMainActivityView(
                context, getResources().getDrawable(R.drawable.icon_game_find_the_pairs),
                true,
                getResources().getString(R.string.game_mode_find_the_pairs));
        btnGameModeFindThePairs.setButtonClickListener(new ButtonMainActivityView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                playSound(soundIdButtonMainMenuClick);
                Intent intent = new Intent(SelectGameModeActivity.this, GameFindThePairsActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout btnGameModeFindThePairsContainer = (LinearLayout) findViewById(R.id.btn_container_game_mode_find_the_pairs);
        btnGameModeFindThePairsContainer.addView(btnGameModeFindThePairs);

        textViewCoins = (TextView) findViewById(R.id.text_view_coins);
        textViewCoins.setText(LocaleTextHelper.getLocaleNumber(SaveManager.getCoins(context)));
        coinsImgTopLayout = (ImageView) findViewById(R.id.image_view_coin);
        initializeMessageContainer();
    }

    private void createGameModesMoreInfoFragment() {
        messageFragmentContainer.setVisibility(View.VISIBLE);
        AboutGameModesFragment aboutGameModesFragment = new AboutGameModesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_tutorial_available", true);
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
                        Intent intent = new Intent(SelectGameModeActivity.this, TutorialGameFindThePairsActivity.class);
                        intent.putExtra("isFromSelectGameModesActivity", true);
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

    private void closeMessageFragment() {
        if (!getIsActivityFinished()) {
            getFragmentManager().beginTransaction().
                    remove(getFragmentManager().findFragmentById(R.id.id_message_fragment)).commit();
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

    private void playSound(int id) {
        if (isVolumeOn) {
            if (soundPool != null) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        }
    }

    private void loadData() {
        isVolumeOn = SaveManager.getVolumeOn(context);
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

    private void getClearMainLayout() {
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.removeAllViews();
        }
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        playSound(soundIdButtonClick);
        Intent intent = new Intent(SelectGameModeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
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
