package com.loop.quizapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EndGameTimeFragment extends Fragment {

    TextView textViewHighScore;
    TextView textViewScoreValue;
    ProgressBar progressBarScore;
    ButtonView btnFirst;
    ButtonView btnSecond;
    ButtonView btnThird;
    boolean isNewGameBtnClickable;

    boolean isOutsideLayoutClickable = false;

    public EndGameTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end_game_time, container, false);
        Bundle bundle = getArguments();
        int score = 0;
        int highScore = 0;
        int maxScore = 0;
        boolean needAnimation = true;
        if (bundle != null) {
            score = bundle.getInt("score");
            highScore = bundle.getInt("high_score");
            maxScore = bundle.getInt("max_score");
            needAnimation = bundle.getBoolean("need_animation");
        }
        LinearLayout btnContainer = view.findViewById(R.id.btn_container);
        textViewHighScore = view.findViewById(R.id.fragment_text_high_score);
        String stringHighScore = getResources().getString(R.string.high_score)
                + LocaleTextHelper.getLocaleNumber(highScore);
        textViewHighScore.setText(stringHighScore);
        textViewScoreValue = view.findViewById(R.id.fragment_text_your_score_value);
        progressBarScore = view.findViewById(R.id.progress_bar_score);
        progressBarScore.setMax(maxScore);
        progressBarScore.setSecondaryProgress(highScore);
        //
        LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btnContainerParams.setMargins(
                0, (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin),
                0, (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin));
        LinearLayout btnFirstContainer = new LinearLayout(getActivity());
        btnFirstContainer.setLayoutParams(btnContainerParams);
        //
        btnFirst = new ButtonView(getActivity(), getResources().getString(R.string.btn_exit),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnFirst.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (firstButtonClickListener != null) {
                    firstButtonClickListener.FirstButtonClickListener();
                } else {
                    closeFragment();
                }
            }
        });
        btnFirst.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnFirstContainer.addView(btnFirst);
        btnContainer.addView(btnFirstContainer);
        LinearLayout btnSecondContainer = new LinearLayout(getActivity());
        btnSecondContainer.setLayoutParams(btnContainerParams);
        btnSecond = new ButtonView(getActivity(), getResources().getString(R.string.game_modes_fragment_btn_new_game),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnSecond.setButtonClickListener(new ButtonView.ButtonClickListener() {
                @Override
                public void ButtonClickListener() {
                    if (isNewGameBtnClickable) {
                        if (secondButtonClickListener != null) {
                            secondButtonClickListener.SecondButtonClickListener();
                        } else {
                            closeFragment();
                        }
                    }
                }
            });
        btnSecond.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnSecondContainer.addView(btnSecond);
        btnContainer.addView(btnSecondContainer);
        LinearLayout btnThirdContainer = new LinearLayout(getActivity());
        btnThirdContainer.setLayoutParams(btnContainerParams);
        btnThird = new ButtonView(getActivity(),
                getResources().getString(R.string.game_modes_fragment_btn_new_game)
                        + getResources().getString(R.string.plus_bonus),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnThird.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (isNewGameBtnClickable) {
                    if (thirdButtonClickListener != null) {
                        thirdButtonClickListener.ThirdButtonClickListener();
                    } else {
                        closeFragment();
                    }
                }
            }
        });
        btnThird.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnThirdContainer.addView(btnThird);
        btnContainer.addView(btnThirdContainer);
        LinearLayout outsideLayout = view.findViewById(R.id.fragment_outside_layout);
        outsideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOutsideLayoutClickable) {
                    closeFragment();
                }
            }
        });
        LinearLayout insideLayout = view.findViewById(R.id.fragment_inside_layout);
        insideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        animateScore(score, highScore, needAnimation);
        return view;
    }

    private void backButtonsDisable() {
        btnFirst.setClickable(false);
        isNewGameBtnClickable = false;
        if (buttonsEnabledListener != null) {
            buttonsEnabledListener.ButtonsEnabledListener(false);
        }
    }

    private void backButtonsEnable() {
        btnFirst.setClickable(true);
        isNewGameBtnClickable = true;
        if (buttonsEnabledListener != null) {
            buttonsEnabledListener.ButtonsEnabledListener(true);
        }
    }

    private void animateScore(final int score, final int highScore, boolean needAnimation) {
        int ANIMATION_DURATION = 2000;
        if (!needAnimation) {
            progressBarScore.setProgress(score);
            progressBarScore.setSecondaryProgress(highScore);
            textViewScoreValue.setText(LocaleTextHelper.getLocaleNumber(score));
            if (highScore < score) {
                textViewHighScore.setText(getResources().getString(R.string.game_modes_new_high_score));
            }
            backButtonsEnable();
            return;
        }
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(0, score);
        valueAnimator.setDuration(ANIMATION_DURATION);
        backButtonsDisable();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentScore = (int) animation.getAnimatedValue();
                String animationScore = "" + LocaleTextHelper.getLocaleNumber(currentScore);
                textViewScoreValue.setText(animationScore);
                progressBarScore.setProgress(currentScore);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if (getActivity() != null) {
                    backButtonsEnable();
                    if (highScore < score) {
                        textViewHighScore.setText(getResources().getString(R.string.game_modes_new_high_score));
                        if (highScoreListener != null) {
                            highScoreListener.HighScoreListener();
                        }
                    }
                    viewAdvertisement();
                }
            }
        });
        valueAnimator.start();
        if (score > 0) {
            if (progressBarStartListener != null) {
                progressBarStartListener.ProgressBarStartListener();
            }
        }
    }

    private void viewAdvertisement() {
        if (viewAdvertisementListener != null) {
            viewAdvertisementListener.ViewAdvertisementListener();
        }
    }

    @SuppressWarnings("All")
    public interface FirstButtonClickListener {
        public void FirstButtonClickListener();
    }

    private FirstButtonClickListener firstButtonClickListener;

    public void setFirstButtonClickListener(FirstButtonClickListener firstButtonClickListener) {
        this.firstButtonClickListener = firstButtonClickListener;
    }

    @SuppressWarnings("All")
    public interface SecondButtonClickListener {
        public void SecondButtonClickListener();
    }

    private SecondButtonClickListener secondButtonClickListener;

    public void setSecondButtonClickListener(SecondButtonClickListener secondButtonClickListener) {
        this.secondButtonClickListener = secondButtonClickListener;
    }

    @SuppressWarnings("All")
    public interface ThirdButtonClickListener {
        public void ThirdButtonClickListener();
    }

    private ThirdButtonClickListener thirdButtonClickListener;

    public void setThirdButtonClickListener(ThirdButtonClickListener thirdButtonClickListener) {
        this.thirdButtonClickListener = thirdButtonClickListener;
    }

    @SuppressWarnings("All")
    public interface ButtonsEnabledListener {
        public void ButtonsEnabledListener(boolean isEnabled);
    }

    private ButtonsEnabledListener buttonsEnabledListener;

    public void setButtonsEnabledListener(ButtonsEnabledListener buttonsEnabledListener) {
        this.buttonsEnabledListener = buttonsEnabledListener;
    }

    @SuppressWarnings("All")
    public interface HighScoreListener {
        public void HighScoreListener();
    }

    private HighScoreListener highScoreListener;

    public void setHighScoreListener(HighScoreListener highScoreListener) {
        this.highScoreListener = highScoreListener;
    }

    @SuppressWarnings("All")
    public interface ProgressBarStartListener {
        public void ProgressBarStartListener();
    }

    private ProgressBarStartListener progressBarStartListener;

    public void setProgressBarStartListener(ProgressBarStartListener progressBarStartListener) {
        this.progressBarStartListener = progressBarStartListener;
    }

    @SuppressWarnings("All")
    public interface ViewAdvertisementListener {
        public void ViewAdvertisementListener();
    }

    private ViewAdvertisementListener viewAdvertisementListener;

    public void setViewAdvertisementListener(ViewAdvertisementListener viewAdvertisementListener) {
        this.viewAdvertisementListener = viewAdvertisementListener;
    }

    private void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

}
