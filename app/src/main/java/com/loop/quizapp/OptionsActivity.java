package com.loop.quizapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class OptionsActivity extends AppCompatActivity {

    ImageView btnBack;
    Context context;
    boolean isVolumeOn;
    boolean playGamesEnabled;
    boolean isMessageFragmentResetGameVisible = false;
    boolean isAboutAppFragmentVisible = false;
    boolean isMessageFragmentRateUsVisible = false;

    @SuppressWarnings("FieldCanBeLocal")
    private final int RATE_US_MINIMUM_ALLOWABLE_RATING = 4;

    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    int soundIdButtonClick;


    ItemView itemVolume;

    FrameLayout messageFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setFragmentsInvisible();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onResume() {
        super.onResume();
        LaunchManager.loadLocale(context);
        loadData();
        loadSounds();
        getClearMainLayout();
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

    private void setFragmentsInvisible() {
        isMessageFragmentResetGameVisible = false;
        isAboutAppFragmentVisible = false;
        isMessageFragmentRateUsVisible = false;
    }

    private void getClearMainLayout() {
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.removeAllViews();
        }
    }

    private void viewsInitialization() {
        setContentView(R.layout.activity_options);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
        ButtonBack.SetOnTouchListenerAndColorFilter(context, btnBack);
        LinearLayout optionsLayout = findViewById(R.id.options_buttons_layout);
        ItemView itemMail = new ItemView(this, getResources().getString(R.string.options_mail),
                getResources().getDrawable(R.drawable.icon_mail),
                true);
        itemMail.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                playSound(soundIdButtonClick);
                clickOnSendMail();
            }
        });
        ItemView itemRate = new ItemView(this, getResources().getString(R.string.options_rate),
                getResources().getDrawable(R.drawable.icon_rate_app),
                true);
        itemRate.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                playSound(soundIdButtonClick);
                showRateUs();
            }
        });
        ItemView itemReset = new ItemView(this, getResources().getString(R.string.options_reset),
                getResources().getDrawable(R.drawable.icon_reset),
                true);
        itemReset.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                playSound(soundIdButtonClick);
                showResetGameFragment();
            }
        });
        ItemView itemMoreApps = new ItemView(this, getResources().getString(R.string.options_more_apps),
                getResources().getDrawable(R.drawable.icon_more_apps),
                true);
        itemMoreApps.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                playSound(soundIdButtonClick);
                clickOnMoreApps();
            }
        });
        ItemView itemPrivacyPolicy = new ItemView(this, getResources().getString(R.string.privacy_policy),
                getResources().getDrawable(R.drawable.icon_privacy_policy),
                true);
        itemPrivacyPolicy.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                playSound(soundIdButtonClick);
                clickOnPrivacyPolicy();
            }
        });
        ItemView itemAboutApp = new ItemView(this, getResources().getString(R.string.options_about_app),
                getResources().getDrawable(R.drawable.icon_about_app),
                true);
        itemAboutApp.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                playSound(soundIdButtonClick);
                showAboutAppFragment();
            }
        });
        String playGamesState;
        if (playGamesEnabled) {
            playGamesState = getResources().getString(R.string.options_play_game_services_on);
        } else {
            playGamesState = getResources().getString(R.string.options_play_game_services_off);
        }
        Drawable volumeOnDrawable;
        String volumeState;
        if (isVolumeOn) {
            volumeOnDrawable = getResources().getDrawable(R.drawable.icon_volume_on);
            volumeState = getResources().getString(R.string.options_volume_on);
        } else {
            volumeOnDrawable = getResources().getDrawable(R.drawable.icon_volume_off);
            volumeState = getResources().getString(R.string.options_volume_off);
        }
        itemVolume = new ItemView(this, getResources().getString(R.string.options_sound), volumeState, volumeOnDrawable, true);
        itemVolume.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                clickOnVolume();
            }
        });

        optionsLayout.addView(itemVolume);
        optionsLayout.addView(itemReset);
        optionsLayout.addView(itemMoreApps);
        optionsLayout.addView(itemRate);
        optionsLayout.addView(itemMail);
        optionsLayout.addView(itemPrivacyPolicy);
        optionsLayout.addView(itemAboutApp);
        initializeMessageContainer();
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        playSound(soundIdButtonClick);
        Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    private void playSound(int id) {
        if (isVolumeOn) {
            if (soundPool != null) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        }
    }

    private void loadData() {
        DataForOptionsActivity dataForOptionsActivity =  SaveManager.getDataForOptionsActivity(context);
        isVolumeOn = dataForOptionsActivity.getIsVolumeOn();
        playGamesEnabled = dataForOptionsActivity.getPlayGamesEnabled();
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
            }
        }
    }

    @SuppressWarnings("all")
    private void initializeMessageContainer() {
        messageFragmentContainer = new FrameLayout(this);
        messageFragmentContainer.setId(R.id.id_message_fragment);
        RelativeLayout.LayoutParams fragmentMessageParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        messageFragmentContainer.setLayoutParams(fragmentMessageParams);
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mainLayout.addView(messageFragmentContainer);
    }

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText,
                                       String btnSecondText) {
        messageFragmentContainer.setVisibility(View.VISIBLE);
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("btn_first_text", btnFirstText);
        if (btnSecondText != null) {
            bundle.putString("btn_second_text", btnSecondText);
        }
        messageFragment.setArguments(bundle);
        if (!getIsActivityFinished()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(messageFragmentContainer.getId(), messageFragment,
                    "message_fragment");
            fragmentTransaction.commit();
        }
        return  messageFragment;
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

    private void closeMessageFragment() {
        if (!getIsActivityFinished()) {
            getFragmentManager().beginTransaction().
                    remove(getFragmentManager().findFragmentById(R.id.id_message_fragment)).commit();
        }
    }

    @SuppressWarnings("deprecation")
    private void clickOnVolume() {
        isVolumeOn = !isVolumeOn;
        SaveManager.setVolumeOn(context, isVolumeOn);
        playSound(soundIdButtonClick);
        if (isVolumeOn) {
            loadSounds();
            itemVolume.setButtonIconDrawable(
                    getResources().getDrawable(R.drawable.icon_volume_on), true);
            itemVolume.setBotText(getResources().getString(R.string.options_volume_on));
        } else {
            itemVolume.setButtonIconDrawable(
                    getResources().getDrawable(R.drawable.icon_volume_off), true);
            itemVolume.setBotText(getResources().getString(R.string.options_volume_off));
        }
    }

    private void clickOnSendMail() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:")); // only email apps should handle this
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_feedback)});
        try {
            startActivity(Intent.createChooser(i, getResources().getString(
                    R.string.message_rate_us_mail_client_chooser_message)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void showMessageFragments() {
        if (isMessageFragmentResetGameVisible) {
            showResetGameFragment();
        } else if (isAboutAppFragmentVisible) {
            showAboutAppFragment();
        } else if (isMessageFragmentRateUsVisible) {
            showRateUs();
        }
    }

    private void showRateUs() {
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

    @SuppressLint("InlinedApi")
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

    private void showResetGameFragment() {
        isMessageFragmentResetGameVisible = true;
        MessageFragment resetGameFragment = createMessageFragment(
                getResources().getString(R.string.message_fragment_title_warning),
                getResources().getString(R.string.message_fragment_text_reset_game_progress),
                getResources().getString(R.string.no),
                getResources().getString(R.string.yes));
        resetGameFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                isMessageFragmentResetGameVisible = false;
                closeMessageFragment();
            }
        });
        resetGameFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                isMessageFragmentResetGameVisible = true;
                resetGame();
            }
        });
    }

    private void showAboutAppFragment() {
        createAboutAppFragment();
    }

    public void resetGame() {
        SaveManager.resetGameData(context);
        StatisticManager statisticManager = new StatisticManager(context);
        statisticManager.resetStatistic();
        closeMessageFragment();
    }

    @SuppressLint("InlinedApi")
    private void clickOnMoreApps() {
        Uri uri = Uri.parse("market://search?q=pub:"
                + getResources().getString(R.string.publisher_name));
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/search?q=pub:"
                        + getResources().getString(R.string.publisher_name)));
        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        }
    }

    private void clickOnPrivacyPolicy() {
        Intent intent = new Intent(OptionsActivity.this, PrivacyPolicyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("isFromOptionsActivity", true);
        finish();
        startActivity(intent);
    }

}
