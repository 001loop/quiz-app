package com.loop.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    ImageView btnBack;
    Context context;
    boolean isVolumeOn;

    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    int soundIdButtonClick;

    boolean isFromOptionsActivity;
    boolean isNeedToDisplayWelcomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        isFromOptionsActivity = getIntent().getBooleanExtra("isFromOptionsActivity", false);
        isNeedToDisplayWelcomeFragment
                = getIntent().getBooleanExtra("isNeedToDisplayWelcomeFragment", false);
        getClearMainLayout();
        viewsInitialization();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onResume() {
        super.onResume();
        loadData();
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

    private void getClearMainLayout() {
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.removeAllViews();
        }
    }

    private void viewsInitialization() {
        setContentView(R.layout.activity_privacy_policy);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
        ButtonBack.SetOnTouchListenerAndColorFilter(context, btnBack);
        //
        TextView textViewPrivacyPolicyLinkText = findViewById(R.id.privacy_policy_link_title);
        String messagePrivacyPolicyLink = getResources().getString(R.string.privacy_policy_link_title);
        textViewPrivacyPolicyLinkText.setText(Html.fromHtml(messagePrivacyPolicyLink));
        textViewPrivacyPolicyLinkText.setMovementMethod(LinkMovementMethod.getInstance());
        textViewPrivacyPolicyLinkText.setLinksClickable(true);
        //
        TextView textViewLastUpdate = findViewById(R.id.last_update_text);
        String lastUpdate = getResources().getString(R.string.last_update_text) +
                getResources().getString(R.string.last_update_date);
        textViewLastUpdate.setText(lastUpdate);
        //
        TextView textViewIntroductionParagraph1 = findViewById(R.id.introduction_paragraph_1);
        String introductionParagraph1 = getResources().getString(R.string.developer_name) +
                getResources().getString(R.string.introduction_paragraph_1_text_1) +
                getResources().getString(R.string.app_name) +
                getResources().getString(R.string.introduction_paragraph_1_text_2) +
                getResources().getString(R.string.developer_name) +
                getResources().getString(R.string.introduction_paragraph_1_text_3);
        textViewIntroductionParagraph1.setText(introductionParagraph1);
        //
        TextView textViewIntroductionParagraph2 = findViewById(R.id.introduction_paragraph_2);
        String introductionParagraph2 = getResources().getString(R.string.introduction_paragraph_2);
        textViewIntroductionParagraph2.setText(introductionParagraph2);
        //
        TextView textViewIntroductionParagraph3 = findViewById(R.id.introduction_paragraph_3);
        String introductionParagraph3 = getResources().getString(R.string.introduction_paragraph_3);
        textViewIntroductionParagraph3.setText(introductionParagraph3);
        //
        TextView textViewIntroductionParagraph4 = findViewById(R.id.introduction_paragraph_4);
        String introductionParagraph4 = getResources().getString(R.string.introduction_paragraph_4_text_1) +
                getResources().getString(R.string.app_name) +
                getResources().getString(R.string.introduction_paragraph_4_text_1);
        textViewIntroductionParagraph4.setText(introductionParagraph4);
        //
        TextView textViewInformationCollectionAndUseTitle = findViewById(R.id.information_collection_and_use_title);
        String informationCollectionAndUseTitle = getResources().getString(R.string.information_collection_and_use_title);
        textViewInformationCollectionAndUseTitle.setText(informationCollectionAndUseTitle);
        //
        TextView textViewInformationCollectionAndUse1 = findViewById(R.id.information_collection_and_use_1);
        String informationCollectionAndUse1 = getResources().getString(R.string.information_collection_and_use_1);
        textViewInformationCollectionAndUse1.setText(informationCollectionAndUse1);
        //
        TextView textViewInformationCollectionAndUse2 = findViewById(R.id.information_collection_and_use_2);
        String informationCollectionAndUse2 = getResources().getString(R.string.information_collection_and_use_2);
        textViewInformationCollectionAndUse2.setText(informationCollectionAndUse2);
        //
        TextView textViewInformationCollectionAndUse3 = findViewById(R.id.information_collection_and_use_3);
        String informationCollectionAndUse3 = getResources().getString(R.string.information_collection_and_use_3);
        textViewInformationCollectionAndUse3.setText(informationCollectionAndUse3);
        //
        TextView textViewInformationCollectionAndUse4 = findViewById(R.id.information_collection_and_use_4);
        String informationCollectionAndUse4 = getResources().getString(R.string.information_collection_and_use_4);
        textViewInformationCollectionAndUse4.setText(informationCollectionAndUse4);
        //
        TextView textViewInformationCollectionAndUse5 = findViewById(R.id.information_collection_and_use_5);
        String informationCollectionAndUse5 = getResources().getString(R.string.information_collection_and_use_5);
        textViewInformationCollectionAndUse5.setText(informationCollectionAndUse5);
        //
        TextView textViewInformationCollectionAndUse6 = findViewById(R.id.information_collection_and_use_6);
        String informationCollectionAndUse6 = getResources().getString(R.string.information_collection_and_use_6);
        textViewInformationCollectionAndUse6.setText(informationCollectionAndUse6);
        //
        TextView textViewInformationCollectionAndUse7 = findViewById(R.id.information_collection_and_use_7);
        String informationCollectionAndUse7 = getResources().getString(R.string.information_collection_and_use_7);
        textViewInformationCollectionAndUse7.setText(informationCollectionAndUse7);
        //
        TextView textViewInformationCollectionAndUse8 = findViewById(R.id.information_collection_and_use_8);
        String informationCollectionAndUse8 = getResources().getString(R.string.information_collection_and_use_8);
        textViewInformationCollectionAndUse8.setText(informationCollectionAndUse8);
        //
        TextView textViewLinkGooglePlayServices = findViewById(R.id.google_play_services_link);
        String messageLinkGooglePlayServices = getResources().getString(R.string.google_play_services_pp_link);
        textViewLinkGooglePlayServices.setText(Html.fromHtml(messageLinkGooglePlayServices));
        textViewLinkGooglePlayServices.setMovementMethod(LinkMovementMethod.getInstance());
        textViewLinkGooglePlayServices.setLinksClickable(true);
        //
        TextView textViewLinkAppodeal = findViewById(R.id.appodeal_link);
        String messageLinkAppodeal = getResources().getString(R.string.appodeal_pp_link);
        textViewLinkAppodeal.setText(Html.fromHtml(messageLinkAppodeal));
        textViewLinkAppodeal.setMovementMethod(LinkMovementMethod.getInstance());
        textViewLinkAppodeal.setLinksClickable(true);
        //
        TextView textViewLinkCrashlytics = findViewById(R.id.crashlytics_link);
        String messageLinkCrashlytics = getResources().getString(R.string.crashlytics_pp_link);
        textViewLinkCrashlytics.setText(Html.fromHtml(messageLinkCrashlytics));
        textViewLinkCrashlytics.setMovementMethod(LinkMovementMethod.getInstance());
        textViewLinkCrashlytics.setLinksClickable(true);
        //
        TextView textViewLinkFabric = findViewById(R.id.fabric_link);
        String messageLinkFabric = getResources().getString(R.string.fabric_pp_link);
        textViewLinkFabric.setText(Html.fromHtml(messageLinkFabric));
        textViewLinkFabric.setMovementMethod(LinkMovementMethod.getInstance());
        textViewLinkFabric.setLinksClickable(true);
        //
        TextView textViewInformationCollectionAndUse9 = findViewById(R.id.information_collection_and_use_9);
        String informationCollectionAndUse9 = getResources().getString(R.string.information_collection_and_use_9);
        textViewInformationCollectionAndUse9.setText(informationCollectionAndUse9);
        //
        TextView textViewInformationCollectionAndUse10 = findViewById(R.id.information_collection_and_use_10);
        String informationCollectionAndUse10 = getResources().getString(R.string.information_collection_and_use_10);
        textViewInformationCollectionAndUse10.setText(informationCollectionAndUse10);
        //
        TextView textViewInformationCollectionAndUse11 = findViewById(R.id.information_collection_and_use_11);
        String informationCollectionAndUse11 = getResources().getString(R.string.information_collection_and_use_11);
        textViewInformationCollectionAndUse11.setText(informationCollectionAndUse11);
        //
        TextView textViewInformationCollectionAndUse12 = findViewById(R.id.information_collection_and_use_12);
        String informationCollectionAndUse12 = getResources().getString(R.string.information_collection_and_use_12);
        textViewInformationCollectionAndUse12.setText(informationCollectionAndUse12);
        //
        TextView textViewInformationCollectionAndUse13 = findViewById(R.id.information_collection_and_use_13);
        String informationCollectionAndUse13 = getResources().getString(R.string.information_collection_and_use_13);
        textViewInformationCollectionAndUse13.setText(informationCollectionAndUse13);
        //
        TextView textViewLogDataTitle = findViewById(R.id.log_data_title);
        String logDataTitle = getResources().getString(R.string.log_data_title);
        textViewLogDataTitle.setText(logDataTitle);
        //
        TextView textViewLogData1 = findViewById(R.id.log_data_1);
        String logData1 = getResources().getString(R.string.log_data_1);
        textViewLogData1.setText(logData1);
        //
        TextView textViewCookiesTitle = findViewById(R.id.cookies_title);
        String cookiesTitle = getResources().getString(R.string.cookies_title);
        textViewCookiesTitle.setText(cookiesTitle);
        //
        TextView textViewCookies1 = findViewById(R.id.cookies_1);
        String cookies1 = getResources().getString(R.string.cookies_1);
        textViewCookies1.setText(cookies1);
        //
        TextView textViewCookies2 = findViewById(R.id.cookies_2);
        String cookies2 = getResources().getString(R.string.cookies_2);
        textViewCookies2.setText(cookies2);
        //
        TextView textViewServiceProvidersTitle = findViewById(R.id.service_providers_title);
        String serviceProvidersTitle = getResources().getString(R.string.service_providers_title);
        textViewServiceProvidersTitle.setText(serviceProvidersTitle);
        //
        TextView textViewServiceProviders1 = findViewById(R.id.service_providers_1);
        String serviceProviders1 = getResources().getString(R.string.service_providers_1);
        textViewServiceProviders1.setText(serviceProviders1);
        //
        TextView textViewServiceProviders2 = findViewById(R.id.service_providers_2);
        String serviceProviders2 = getResources().getString(R.string.service_providers_2);
        textViewServiceProviders2.setText(serviceProviders2);
        //
        TextView textViewServiceProviders3 = findViewById(R.id.service_providers_3);
        String serviceProviders3 = getResources().getString(R.string.service_providers_3);
        textViewServiceProviders3.setText(serviceProviders3);
        //
        TextView textViewServiceProviders4 = findViewById(R.id.service_providers_4);
        String serviceProviders4 = getResources().getString(R.string.service_providers_4);
        textViewServiceProviders4.setText(serviceProviders4);
        //
        TextView textViewServiceProviders5 = findViewById(R.id.service_providers_5);
        String serviceProviders5 = getResources().getString(R.string.service_providers_5);
        textViewServiceProviders5.setText(serviceProviders5);
        //
        TextView textViewServiceProviders6 = findViewById(R.id.service_providers_6);
        String serviceProviders6 = getResources().getString(R.string.service_providers_6);
        textViewServiceProviders6.setText(serviceProviders6);
        //
        TextView textViewSecurityTitle = findViewById(R.id.security_title);
        String securityTitle = getResources().getString(R.string.security_title);
        textViewSecurityTitle.setText(securityTitle);
        //
        TextView textViewSecurity1 = findViewById(R.id.security_1);
        String security1 = getResources().getString(R.string.security_1);
        textViewSecurity1.setText(security1);
        //
        TextView textViewLinksToOtherSitesTitle = findViewById(R.id.links_to_other_sites_title);
        String linksToOtherSitesTitle = getResources().getString(R.string.links_to_other_sites_title);
        textViewLinksToOtherSitesTitle.setText(linksToOtherSitesTitle);
        //
        TextView textViewLinksToOtherSites1 = findViewById(R.id.links_to_other_sites_1);
        String linksToOtherSites1 = getResources().getString(R.string.links_to_other_sites_1);
        textViewLinksToOtherSites1.setText(linksToOtherSites1);
        //
        TextView textViewChildrensPrivacyTitle = findViewById(R.id.childrens_privacy_title);
        String childrensPrivacyTitle = getResources().getString(R.string.childrens_privacy_title);
        textViewChildrensPrivacyTitle.setText(childrensPrivacyTitle);
        //
        TextView textViewChildrensPrivacy1 = findViewById(R.id.childrens_privacy_1);
        String childrensPrivacy1 = getResources().getString(R.string.childrens_privacy_1);
        textViewChildrensPrivacy1.setText(childrensPrivacy1);
        //
        TextView textViewDenialOfResponsibilityTitle = findViewById(R.id.denial_of_responsibility_title);
        String denialOfResponsibilityTitle = getResources().getString(R.string.denial_of_responsibility_title);
        textViewDenialOfResponsibilityTitle.setText(denialOfResponsibilityTitle);
        //
        TextView textViewDenialOfResponsibility1 = findViewById(R.id.denial_of_responsibility_1);
        String denialOfResponsibility1 = getResources().getString(R.string.denial_of_responsibility_1);
        textViewDenialOfResponsibility1.setText(denialOfResponsibility1);
        //
        TextView textViewDenialOfResponsibility2 = findViewById(R.id.denial_of_responsibility_2);
        String denialOfResponsibility2 = getResources().getString(R.string.denial_of_responsibility_2);
        textViewDenialOfResponsibility2.setText(denialOfResponsibility2);
        //
        TextView textViewDenialOfResponsibility3 = findViewById(R.id.denial_of_responsibility_3);
        String denialOfResponsibility3 = getResources().getString(R.string.denial_of_responsibility_3);
        textViewDenialOfResponsibility3.setText(denialOfResponsibility3);
        //
        TextView textViewContactUsTitle = findViewById(R.id.contact_us_title);
        String contactUsTitle = getResources().getString(R.string.contact_us_title);
        textViewContactUsTitle.setText(contactUsTitle);
        //
        TextView textViewContactUs1 = findViewById(R.id.contact_us_1);
        String contactUs1 = getResources().getString(R.string.contact_us_1);
        textViewContactUs1.setText(contactUs1);
        //
        TextView textViewContactUsLink = findViewById(R.id.contact_us_link);
        SpannableString contactUsLink = new SpannableString(getResources().getString(R.string.email_feedback));
        contactUsLink.setSpan(new UnderlineSpan(), 0, contactUsLink.length(), 0);
        textViewContactUsLink.setText(contactUsLink);
        textViewContactUsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnSendMail();
            }
        });
        //
        final CheckBox checkBoxPersonalizedAds = findViewById(R.id.checkbox_personalized_ads);
        checkBoxPersonalizedAds.setChecked(SaveManager.getIsPersonalizedAdvertisementEnabled(context));
        checkBoxPersonalizedAds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SaveManager.setPersonalizedAdvertisementEnabled(context, isChecked);
            }
        });
        if (!isFromOptionsActivity) {
            RelativeLayout btnAgreeContainer = findViewById(R.id.btn_agree_container);
            ButtonView btnAgree = new ButtonView(context,
                    getResources().getString(R.string.agree_and_continue),
                    getResources().getDimension(R.dimen.fragment_button_size),
                    getResources().getDimension(R.dimen.fragment_button_size));
            btnAgree.setButtonClickListener(new ButtonView.ButtonClickListener() {
                @Override
                public void ButtonClickListener() {
                    clickOnAgree();
                }
            });
            btnAgree.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
            btnAgreeContainer.addView(btnAgree);
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

    private void clickOnAgree() {
        SaveManager.setPrivacyPolicyAccepted(context, true);
        playSound(soundIdButtonClick);
        Intent intent;
        intent = new Intent(PrivacyPolicyActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("isPrivacyPolicyAcceptedAndNeedToDisplayWelcomeFragment", isNeedToDisplayWelcomeFragment);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        playSound(soundIdButtonClick);
        Intent intent;
        if (isFromOptionsActivity) {
            intent = new Intent(PrivacyPolicyActivity.this, OptionsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent = new Intent(PrivacyPolicyActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("isNeedToDisplayWelcomeFragmentFromOptionsActivity", isNeedToDisplayWelcomeFragment);
        }
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
            }
        }
    }


}
