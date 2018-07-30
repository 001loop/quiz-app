package com.loop.quizapp;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loop.quizapp.util.billing.IabBroadcastReceiver;
import com.loop.quizapp.util.billing.IabHelper;
import com.loop.quizapp.util.billing.IabResult;
import com.loop.quizapp.util.billing.Inventory;
import com.loop.quizapp.util.billing.Purchase;
import com.loop.quizapp.util.billing.SkuDetails;

import java.util.Arrays;
import java.util.List;

public class StoreActivity
        extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {

    IabHelper iabHelper;
    IabBroadcastReceiver iabBroadcastReceiver;

    Context context;

    ItemView itemRemoveAdvertisement;
    ItemView itemPlayVideo;
    ItemView itemSpecialOffer;
    ItemView itemPremium;
    ItemView itemSmallNumOfCoins;
    ItemView itemMediumNumOfCoins;
    ItemView itemLargeNumOfCoins;

    TextView textViewCoins;

    ImageView coinsImgTopLayout;

    boolean isAdvertisementEnabled;
    boolean isSpecialOfferEnabled;
    boolean isPremiumActive;

    boolean isVolumeOn;
    SoundPool soundPool;
    final int MAX_SOUND_STREAMS = 3;
    int soundIdButtonClick;

    boolean isFromGameActivity = false;

    FrameLayout messageFragmentContainer;
    ImageView btnBack;

    static final int COINS_CHANGE_ANIMATION_DURATION = 1500;

    String securityToken;
    static final int BUY_SPECIAL_OFFER_REQUEST = 10000;
    static final int BUY_SMALL_NUM_OF_COINS_REQUEST = 10;
    static final int BUY_MEDIUM_NUM_OF_COINS_REQUEST = 100;
    static final int BUY_LARGE_NUM_OF_COINS_REQUEST = 1000;
    static final int BUY_REMOVE_ADVERTISEMENT_REQUEST = 1;
    static final int BUY_PREMIUM_REQUEST = 2;

    static final String SKU_REMOVE_ADV = "remove_advertisement";
    static final String SKU_PREMIUM = "premium";
    static final String SKU_SPECIAL_OFFER = "special_offer";
    static final String SKU_SMALL_NUM_OF_COINS = "small_number_of_coins";
    static final String SKU_MEDIUM_NUM_OF_COINS = "medium_number_of_coins";
    static final String SKU_LARGE_NUM_OF_COINS = "large_number_of_coins";

    List<String> SKUsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        context = getApplicationContext();
        isFromGameActivity = getIntent().getBooleanExtra("isFromGameActivity", false);
        SKUsList = Arrays.asList(SKU_REMOVE_ADV, SKU_PREMIUM, SKU_SPECIAL_OFFER,
                SKU_SMALL_NUM_OF_COINS, SKU_MEDIUM_NUM_OF_COINS, SKU_LARGE_NUM_OF_COINS);
        setupBillingService();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onResume() {
        super.onResume();
        LaunchManager.loadLocale(context);
        getClearButtonsLayout();
        loadData();
        LaunchManager.checkDataValid(context);
        loadSounds();
        viewsInitialization();
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

    private void loadData() {
        DataForStoreActivity data = SaveManager.getDataForStoreActivity(context);
        isAdvertisementEnabled = data.getAdvertisementEnabled();
        isSpecialOfferEnabled = data.getSpecialOfferEnabled();
        isPremiumActive = data.getIsPremiumActive();
        isVolumeOn = data.getIsVolumeOn();
    }

    private void viewsInitialization() {
        textViewCoins = findViewById(R.id.text_view_coins);
        textViewCoins.setText(LocaleTextHelper.getLocaleNumber(SaveManager.getCoins(context)));
        coinsImgTopLayout = findViewById(R.id.image_view_coin);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
        ButtonBack.SetOnTouchListenerAndColorFilter(context, btnBack);
        RelativeLayout watchVideoLayout = findViewById(R.id.layout_watch_video);
        watchVideoLayout.removeAllViews();
        String watchVideoText = String.format(
                getResources().getString(R.string.watch_video),
                LocaleTextHelper.getLocaleNumber(ApplicationData.getWatchVideoReward()));
        itemPlayVideo = new ItemView(this,
                watchVideoText ,
                getResources().getDrawable(R.drawable.icon_play_video),
                true);
        itemPlayVideo.setTextColor(getResources().getColor(R.color.textOnPlayVideoBackground));
        itemPlayVideo.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
            @Override
            public void ItemButtonClickListener() {
                playSound(soundIdButtonClick);
                AdvertisementReward();
            }
        });
        watchVideoLayout.addView(itemPlayVideo);
        LinearLayout storeMainLayout = getClearButtonsLayout();
        if (storeMainLayout != null) {
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textViewParams.gravity = Gravity.CENTER;
            int textViewPadding = (int) getResources().getDimension(R.dimen.button_view_text_margin);
            if (isAdvertisementEnabled || isSpecialOfferEnabled || !isPremiumActive) {
                TextView textViewLimitedOffers = new TextView(this);
                textViewLimitedOffers.setLayoutParams(textViewParams);
                textViewLimitedOffers.setText(getResources().getString(R.string.limited_offers_title));
                textViewLimitedOffers.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.item_text_size));
                textViewLimitedOffers.setTextColor(getResources().getColor(R.color.textOnPlayVideoBackground));
                textViewLimitedOffers.setBackgroundColor(getResources().getColor(R.color.playVideoBackground));
                textViewLimitedOffers.setGravity(Gravity.CENTER);
                textViewLimitedOffers.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
                storeMainLayout.addView(textViewLimitedOffers);
            }
            if (isAdvertisementEnabled) {
                itemRemoveAdvertisement = new ItemView(this,
                        getResources().getString(R.string.buy_remove_advertisement) +
                                getResources().getString(R.string.plus_bonus),
                        getResources().getString(R.string.no_price),
                        getResources().getDrawable(R.drawable.icon_remove_ads),
                        true);
                itemRemoveAdvertisement.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
                    @Override
                    public void ItemButtonClickListener() {
                        playSound(soundIdButtonClick);
                        clickOnRemoveAdvertisement();
                    }
                });
                itemRemoveAdvertisement.setButtonClickable(false);
                storeMainLayout.addView(itemRemoveAdvertisement);
            }
            if (isSpecialOfferEnabled) {
                String specialOfferText = String.format(
                        getResources().getString(R.string.buy_coins),
                        LocaleTextHelper.getLocaleNumber(ApplicationData.getCoinsAmountSpecialOffer()));
                itemSpecialOffer = new ItemView(this,
                        specialOfferText,
                        getResources().getString(R.string.no_price),
                        getResources().getDrawable(R.drawable.icon_special_offer),
                        true);
                itemSpecialOffer.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
                    @Override
                    public void ItemButtonClickListener() {
                        playSound(soundIdButtonClick);
                        buySpecialOffer();
                    }
                });
                itemSpecialOffer.setButtonClickable(false);
                storeMainLayout.addView(itemSpecialOffer);
            }
            if (!isPremiumActive) {
                String premiumText = getResources().getString(R.string.buy_premium);
                itemPremium = new ItemView(this,
                        premiumText,
                        getResources().getString(R.string.no_price),
                        getResources().getDrawable(R.drawable.icon_premium),
                        true);
                itemPremium.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
                    @Override
                    public void ItemButtonClickListener() {
                        playSound(soundIdButtonClick);
                        clickOnItemPremium();
                    }
                });
                itemPremium.setButtonClickable(false);
                storeMainLayout.addView(itemPremium);
            }
            if (isAdvertisementEnabled || isSpecialOfferEnabled || !isPremiumActive) {
                TextView textViewUnlimitedOffers = new TextView(this);
                textViewUnlimitedOffers.setLayoutParams(textViewParams);
                textViewUnlimitedOffers.setText(getResources().getString(R.string.unlimited_offers_title));
                textViewUnlimitedOffers.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.item_text_size));
                textViewUnlimitedOffers.setTextColor(getResources().getColor(R.color.textOnPlayVideoBackground));
                textViewUnlimitedOffers.setBackgroundColor(getResources().getColor(R.color.playVideoBackground));
                textViewUnlimitedOffers.setGravity(Gravity.CENTER);
                textViewUnlimitedOffers.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
                storeMainLayout.addView(textViewUnlimitedOffers);
            }
            String smallNumOfCoinsText = String.format(
                    getResources().getString(R.string.buy_coins),
                    LocaleTextHelper.getLocaleNumber(ApplicationData.getCoinsAmountSmallNum()));
            itemSmallNumOfCoins = new ItemView(this,
                    smallNumOfCoinsText,
                    getResources().getString(R.string.no_price),
                    getResources().getDrawable(R.drawable.icon_small_num_of_coins),
                    true);
            itemSmallNumOfCoins.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
                @Override
                public void ItemButtonClickListener() {
                    playSound(soundIdButtonClick);
                    buyCoins(ApplicationData.getCoinsAmountSmallNum());
                }
            });
            itemSmallNumOfCoins.setButtonClickable(false);
            storeMainLayout.addView(itemSmallNumOfCoins);
            String mediumNumOfCoinsText = String.format(
                    getResources().getString(R.string.buy_coins),
                    LocaleTextHelper.getLocaleNumber(ApplicationData.getCoinsAmountMediumNum()));
            itemMediumNumOfCoins = new ItemView(this,
                    mediumNumOfCoinsText,
                    getResources().getString(R.string.no_price),
                    getResources().getDrawable(R.drawable.icon_medium_num_of_coins),
                    true);
            itemMediumNumOfCoins.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
                @Override
                public void ItemButtonClickListener() {
                    playSound(soundIdButtonClick);
                    buyCoins(ApplicationData.getCoinsAmountMediumNum());
                }
            });
            itemMediumNumOfCoins.setButtonClickable(false);
            storeMainLayout.addView(itemMediumNumOfCoins);
            String largeNumOfCoinsText = String.format(
                    getResources().getString(R.string.buy_coins),
                    LocaleTextHelper.getLocaleNumber(ApplicationData.getCoinsAmountLargeNum()));
            itemLargeNumOfCoins = new ItemView(this,
                    largeNumOfCoinsText,
                    getResources().getString(R.string.no_price),
                    getResources().getDrawable(R.drawable.icon_large_num_of_coins),
                    true);
            itemLargeNumOfCoins.setItemButtonClickListener(new ItemView.ItemButtonClickListener() {
                @Override
                public void ItemButtonClickListener() {
                    playSound(soundIdButtonClick);
                    buyCoins(ApplicationData.getCoinsAmountLargeNum());
                }
            });
            itemLargeNumOfCoins.setButtonClickable(false);
            storeMainLayout.addView(itemLargeNumOfCoins);
        }
        initializeMessageContainer();
    }

    private LinearLayout getClearButtonsLayout() {
        LinearLayout buttonsLayout = findViewById(R.id.store_buttons_layout);
        if (buttonsLayout != null) {
            buttonsLayout.removeAllViews();
        }
        return buttonsLayout;
    }

    private void initializeMessageContainer() {
        messageFragmentContainer = new FrameLayout(this);
        messageFragmentContainer.setId(R.id.id_message_fragment);
        RelativeLayout.LayoutParams fragmentMessageParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        messageFragmentContainer.setLayoutParams(fragmentMessageParams);
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        mainLayout.addView(messageFragmentContainer);
    }

    private MessageFragment createMessageFragment(String title, String message,
                                                  String btnFirstText, String btnSecondText) {
        messageFragmentContainer.setVisibility(View.VISIBLE);
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("btn_first_text", btnFirstText);
        bundle.putString("btn_second_text", btnSecondText);
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

    private MessageFragment createMessageFragment(String title, String message, String btnFirstText) {
        return createMessageFragment(title, message, btnFirstText, null);
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

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        playSound(soundIdButtonClick);
        Intent intent;
        if (isFromGameActivity) {
            intent = new Intent(StoreActivity.this, GameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent = new Intent(StoreActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void clickOnItemPremium() {
        MessageFragment premiumMessageFragment = createMessageFragment(
                getResources().getString(R.string.buy_premium),
                getResources().getString(R.string.premium_description),
                getResources().getString(R.string.message_fragment_btn_close),
                getResources().getString(R.string.buy)
        );
        premiumMessageFragment.setOutsideLayoutClickable(true);
        premiumMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
        premiumMessageFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                buyPremium();
            }
        });
    }

    private void buyPremium() {
        securityToken = new TokensManager(32).getRandomString();
        try {
            iabHelper.launchPurchaseFlow(this, SKU_PREMIUM, BUY_PREMIUM_REQUEST,
                    purchaseFinishedListener, securityToken);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            //System.out.println("error premium purchase");
        }
    }

    private void clickOnRemoveAdvertisement() {
        String removeAdsMessage = String.format(
                getResources().getString(R.string.remove_advertisement_description),
                LocaleTextHelper.getLocaleNumber(ApplicationData.getCoinsAmountWithAdvertEnabled()));
        MessageFragment removeAdvertisementMessageFragment = createMessageFragment(
                getResources().getString(R.string.buy_remove_advertisement),
                removeAdsMessage,
                getResources().getString(R.string.message_fragment_btn_close),
                getResources().getString(R.string.buy)
        );
        removeAdvertisementMessageFragment.setOutsideLayoutClickable(true);
        removeAdvertisementMessageFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
        removeAdvertisementMessageFragment.setSecondButtonClickListener(new MessageFragment.SecondButtonClickListener() {
            @Override
            public void SecondButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
                removeAdvertisement();
            }
        });
    }

    private void removeAdvertisement() {
        securityToken = new TokensManager(32).getRandomString();
        try {
            iabHelper.launchPurchaseFlow(this, SKU_REMOVE_ADV, BUY_REMOVE_ADVERTISEMENT_REQUEST,
                purchaseFinishedListener, securityToken);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            //System.out.println("error advert purchase");
        }
    }

    private void buySpecialOffer() {
        securityToken = new TokensManager(32).getRandomString();
        try {
            iabHelper.launchPurchaseFlow(this, SKU_SPECIAL_OFFER, BUY_SPECIAL_OFFER_REQUEST,
                    purchaseFinishedListener, securityToken);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            //System.out.println("error special offer purchase");
        }
    }

    private void buyCoins(int numOfCoins) {
        if (numOfCoins == ApplicationData.getCoinsAmountSmallNum()) {
            securityToken = new TokensManager(32).getRandomString();
            try {
                iabHelper.launchPurchaseFlow(this, SKU_SMALL_NUM_OF_COINS,
                        BUY_SMALL_NUM_OF_COINS_REQUEST, purchaseFinishedListener, securityToken);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
                //System.out.println("error small num of coins purchase");
            }
        }
        if (numOfCoins == ApplicationData.getCoinsAmountMediumNum()) {
            securityToken = new TokensManager(32).getRandomString();
            try {
                iabHelper.launchPurchaseFlow(this, SKU_MEDIUM_NUM_OF_COINS,
                        BUY_MEDIUM_NUM_OF_COINS_REQUEST, purchaseFinishedListener, securityToken);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
                //System.out.println("error medium num of coins purchase");
            }
        }
        if (numOfCoins == ApplicationData.getCoinsAmountLargeNum()) {
            securityToken = new TokensManager(32).getRandomString();
            try {
                iabHelper.launchPurchaseFlow(this, SKU_LARGE_NUM_OF_COINS,
                        BUY_LARGE_NUM_OF_COINS_REQUEST, purchaseFinishedListener, securityToken);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
                //System.out.println("error large num of coins purchase");
            }
        }
    }

    private void setupBillingService() {
        iabHelper = new IabHelper(this, ApplicationData.getRsaAppKey());
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    //System.out.println("onIabSetupFinished error");
                    return;
                }
                if (iabHelper == null) {
                    return;
                }
                if (itemRemoveAdvertisement != null) {
                    itemRemoveAdvertisement.setButtonClickable(true);
                }
                if (itemSpecialOffer != null) {
                    itemSpecialOffer.setButtonClickable(true);
                }
                if (itemPremium != null) {
                    itemPremium.setButtonClickable(true);
                }
                itemSmallNumOfCoins.setButtonClickable(true);
                itemMediumNumOfCoins.setButtonClickable(true);
                itemLargeNumOfCoins.setButtonClickable(true);
                iabBroadcastReceiver = new IabBroadcastReceiver(StoreActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(iabBroadcastReceiver, broadcastFilter);
                try {
                    iabHelper.queryInventoryAsync(true, SKUsList, null, receivedInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                    //System.out.println("Error querying inventory. Another async operation in progress");
                }
            }
        });
    }

    // purchase finished
    IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener =
            new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (iabHelper == null) {
                //System.out.println("iabHelper == null!");
                return;
            }
            if (result.isFailure()) {
                //System.out.print("onIabPurchaseFinished failure");
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                //System.out.println("Error purchasing. Authenticity verification failed.");
                return;
            }
            switch (purchase.getSku()) {
                case SKU_SMALL_NUM_OF_COINS:
                case SKU_MEDIUM_NUM_OF_COINS:
                case SKU_LARGE_NUM_OF_COINS:
                    try {
                        iabHelper.consumeAsync(purchase, consumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                        //System.out.println("Error consume coins. Async in progress exception");
                    }
                    break;
                case SKU_REMOVE_ADV:
                    isAdvertisementEnabled = false;
                    SaveManager.setAdvertisementEnabled(context, false);
                    changeCoinsNumber(ApplicationData.getCoinsAmountWithAdvertEnabled());
                    break;
                case SKU_SPECIAL_OFFER:
                    isSpecialOfferEnabled = false;
                    SaveManager.setSpecialOfferEnabled(context, false);
                    changeCoinsNumber(ApplicationData.getCoinsAmountSpecialOffer());
                    break;
                case SKU_PREMIUM:
                    isPremiumActive = true;
                    SaveManager.setIsPremiumActive(context, true);
                    break;
            }
        }
    };

    // get purchased products
    IabHelper.QueryInventoryFinishedListener receivedInventoryListener =
            new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (iabHelper == null) {
                return;
            }
            if (result.isFailure()) {
                return;
            }
            if (itemRemoveAdvertisement != null) {
                SkuDetails skuRemoveAdvertisement = inventory.getSkuDetails(SKU_REMOVE_ADV);
                if (skuRemoveAdvertisement != null) {
                    itemRemoveAdvertisement.setBotText(skuRemoveAdvertisement.getPrice());
                }
            }
            if (itemSpecialOffer != null) {
                SkuDetails skuSpecialOffer = inventory.getSkuDetails(SKU_SPECIAL_OFFER);
                if (skuSpecialOffer != null) {
                    itemSpecialOffer.setBotText(skuSpecialOffer.getPrice());
                }
            }
            if (itemPremium != null) {
                SkuDetails skuPremium = inventory.getSkuDetails(SKU_PREMIUM);
                if (skuPremium != null) {
                    itemPremium.setBotText(skuPremium.getPrice());
                }
            }
            SkuDetails skuSmallNumOfCoins = inventory.getSkuDetails(SKU_SMALL_NUM_OF_COINS);
            if (skuSmallNumOfCoins != null) {
                itemSmallNumOfCoins.setBotText(skuSmallNumOfCoins.getPrice());
            }
            SkuDetails skuMediumNumOfCoins = inventory.getSkuDetails(SKU_MEDIUM_NUM_OF_COINS);
            if (skuMediumNumOfCoins != null) {
                itemMediumNumOfCoins.setBotText(skuMediumNumOfCoins.getPrice());
            }
            SkuDetails skuLargeNumOfCoins = inventory.getSkuDetails(SKU_LARGE_NUM_OF_COINS);
            if (skuLargeNumOfCoins != null) {
                itemLargeNumOfCoins.setBotText(skuLargeNumOfCoins.getPrice());
            }
            Purchase removeAdvertisement = inventory.getPurchase(SKU_REMOVE_ADV);
            if (removeAdvertisement != null) {
                SaveManager.setAdvertisementEnabled(context, false);
                if (isAdvertisementEnabled) {       // remove advert was purchased before, but btn for now visible
                    LaunchManager.checkDataValid(context);
                    getClearButtonsLayout();
                    loadData();
                    viewsInitialization();
                }
            } else {
                SaveManager.setAdvertisementEnabled(context, true);
            }
            Purchase specialOffer = inventory.getPurchase(SKU_SPECIAL_OFFER);
            if (specialOffer != null) {
                SaveManager.setSpecialOfferEnabled(context, false);
                if (isSpecialOfferEnabled) {       // special offer was purchased before, but btn for now visible
                    LaunchManager.checkDataValid(context);
                    getClearButtonsLayout();
                    loadData();
                    viewsInitialization();
                }
            } else {
                SaveManager.setSpecialOfferEnabled(context, true);
            }
            Purchase premium = inventory.getPurchase(SKU_PREMIUM);
            if (premium != null) {
                SaveManager.setIsPremiumActive(context, true);
                if (isPremiumActive) {       // premium was purchased before, but btn for now visible
                    LaunchManager.checkDataValid(context);
                    getClearButtonsLayout();
                    loadData();
                    viewsInitialization();
                }
            } else {
                SaveManager.setIsPremiumActive(context, false);
            }
            Purchase smallNumOfCoinsPurchase = inventory.getPurchase(SKU_SMALL_NUM_OF_COINS);
            if (smallNumOfCoinsPurchase != null) {
                try {
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_SMALL_NUM_OF_COINS),
                            consumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                    //System.out.println("Error consuming small num of coins. Another async operation in progress.");
                }
            }
            Purchase mediumNumOfCoinsPurchase = inventory.getPurchase(SKU_MEDIUM_NUM_OF_COINS);
            if (mediumNumOfCoinsPurchase != null) {
                try {
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_MEDIUM_NUM_OF_COINS),
                            consumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                    //System.out.println("Error consuming medium num of coins. Another async operation in progress.");
                }
            }
            Purchase LargeNumOfCoinsPurchase = inventory.getPurchase(SKU_LARGE_NUM_OF_COINS);
            if (LargeNumOfCoinsPurchase != null) {
                try {
                    iabHelper.consumeAsync(inventory.getPurchase(SKU_LARGE_NUM_OF_COINS),
                            consumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                    //System.out.println("Error consuming large num of coins. Another async operation in progress.");
                }
            }
        }
    };

    IabHelper.OnConsumeFinishedListener consumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (iabHelper == null) {
                return;
            }
            if (result.isSuccess()) {
                switch (purchase.getSku()) {
                    case SKU_SMALL_NUM_OF_COINS:
                        changeCoinsNumber(ApplicationData.getCoinsAmountSmallNum());
                        break;
                    case SKU_MEDIUM_NUM_OF_COINS:
                        changeCoinsNumber(ApplicationData.getCoinsAmountMediumNum());
                        break;
                    case SKU_LARGE_NUM_OF_COINS:
                        changeCoinsNumber(ApplicationData.getCoinsAmountLargeNum());
                        break;
                }
            } else {
                //System.out.println("onConsumeFinished error");
            }
        }
    };

    boolean verifyDeveloperPayload(Purchase p) {
        return p.getDeveloperPayload().equals(securityToken);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*
    private int getPurchaseId(Purchase purchase) {
        int id;
        String token = purchase.getDeveloperPayload();
        int startIndex = token.indexOf("id:") + 3;
        try {
            id = Integer.parseInt(token.substring(startIndex, startIndex + 1));
        } catch (NumberFormatException e) {
            return 0;
        }
        return  id;
    }
    */

    /*
    // delete on release
    private void consumeAdvert() {
        Inventory inventory  = null;
        try {
            inventory = iabHelper.queryInventory();
        } catch (IabException e) {
            e.printStackTrace();
        }
        if (inventory != null) {
            Purchase advertPurchase = inventory.getPurchase(SKU_REMOVE_ADV);
            if (advertPurchase != null) {
                try {
                    System.out.println("Try to consume removeAdvert");
                    iabHelper.consumeAsync(advertPurchase, null);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    System.out.println("Error consuming remove advert. Another async operation in progress.");
                }
            }
        }
    }
    private void consumeSpecialOffer() {
        Inventory inventory  = null;
        try {
            inventory = iabHelper.queryInventory();
        } catch (IabException e) {
            e.printStackTrace();
        }
        if (inventory != null) {
            Purchase specialOfferPurchase = inventory.getPurchase(SKU_SPECIAL_OFFER);
            if (specialOfferPurchase != null) {
                try {
                    System.out.println("Try to consume specialOffer");
                    iabHelper.consumeAsync(specialOfferPurchase, null);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    System.out.println("Error consuming special offer. Another async operation in progress.");
                }
            }
        }
    }
    // --
    */

    private void changeCoinsNumber(int number) {
        LaunchManager.checkDataValid(context);
        changeCoinsAnimation(SaveManager.getCoins(context), SaveManager.getCoins(context) + number);
        SaveManager.changeCoinsNumber(context, number);
        checkRichPlayerAchievement();
    }

    private void changeCoinsAnimation(int coinsBefore, int coinsAfter) {
        Animation animation = AnimationUtils.loadAnimation(getApplication(),
                R.anim.anim_num_of_coins_chage);
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(coinsBefore, coinsAfter);
        valueAnimator.setDuration(COINS_CHANGE_ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                String coinsString = "" + LocaleTextHelper.getLocaleNumber((int) animation.getAnimatedValue());
                textViewCoins.setText(coinsString);
            }
        });
        valueAnimator.start();
        coinsImgTopLayout.startAnimation(animation);
    }

    private void AdvertisementReward() {
        if (!isNetworkAvailable()) {
            connectionProblem();
            return;
        }
    }

    private void checkRichPlayerAchievement() {
        if (AchievementManager.getRichPlayer(context) == -1) {
            if (SaveManager.getCoins(context) >= ApplicationData.getAchievementRichPlayerCoinsNumber()) {
                AchievementManager.unlockRichPlayer(context);
            }
        }
    }

    private void connectionProblem() {
        MessageFragment errorFragment = createMessageFragment(
                getResources().getString(R.string.message_fragment_title_error),
                getResources().getString(R.string.message_fragment_text_internet_connection_problem),
                getResources().getString(R.string.message_fragment_btn_close)
        );
        errorFragment.setOutsideLayoutClickable(true);
        errorFragment.setFirstButtonClickListener(new MessageFragment.FirstButtonClickListener() {
            @Override
            public void FirstButtonClickListener() {
                playSound(soundIdButtonClick);
                closeMessageFragment();
            }
        });
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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (iabHelper == null) {
            return;
        }
        if (!iabHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        try {
            iabHelper.queryInventoryAsync(true, SKUsList, null, receivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (iabBroadcastReceiver != null) {
            unregisterReceiver(iabBroadcastReceiver);
        }
        if (iabHelper != null) {
            try {
                iabHelper.disposeWhenFinished();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        iabHelper = null;
    }

}
