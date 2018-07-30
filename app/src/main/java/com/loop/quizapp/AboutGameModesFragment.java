package com.loop.quizapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutGameModesFragment extends Fragment {

    ButtonView btnClose;
    ButtonView btnTutorial;

    public AboutGameModesFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_game_modes, container, false);
        Bundle bundle = getArguments();
        boolean isTutorialAvailable = false;
        if (bundle != null) {
            isTutorialAvailable = bundle.getBoolean("is_tutorial_available");
        }
        if (!isTutorialAvailable) {
            TextView tutorialTextView = view.findViewById(R.id.fragment_text_7);
            tutorialTextView.setVisibility(View.GONE);
        }
        LinearLayout btnContainer = view.findViewById(R.id.btn_container);
        LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btnContainerParams.setMargins(
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0,
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0);
        LinearLayout btnFirstContainer = new LinearLayout(getActivity());
        btnFirstContainer.setLayoutParams(btnContainerParams);
        //
        btnClose = new ButtonView(getActivity(), getResources().getString(R.string.message_fragment_btn_close),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnClose.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (buttonCloseClickListener != null) {
                    buttonCloseClickListener.ButtonCloseClickListener();
                } else {
                    closeFragment();
                }
            }
        });
        btnClose.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnFirstContainer.addView(btnClose);
        btnContainer.addView(btnFirstContainer);
        if (isTutorialAvailable) {
            LinearLayout btnSecondContainer = new LinearLayout(getActivity());
            btnSecondContainer.setLayoutParams(btnContainerParams);
            btnTutorial = new ButtonView(getActivity(),
                    getResources().getString(R.string.tutorial).toLowerCase(),
                    getResources().getDimension(R.dimen.fragment_button_size),
                    getResources().getDimension(R.dimen.fragment_button_size));
            btnTutorial.setButtonClickListener(new ButtonView.ButtonClickListener() {
                @Override
                public void ButtonClickListener() {
                    if (buttonTutorialClickListener != null) {
                        buttonTutorialClickListener.ButtonTutorialClickListener();
                    } else {
                        closeFragment();
                    }
                }
            });
            btnTutorial.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
            btnSecondContainer.addView(btnTutorial);
            btnContainer.addView(btnSecondContainer);
        }
        //
        LinearLayout btnYesOrNoContainer = view.findViewById(R.id.image_game_mode_yes_or_no);
        ButtonView btnYesOrNo = new ButtonView(
                getActivity(), getResources().getDrawable(R.drawable.icon_game_yes_or_no),
                true,
                getResources().getDimension(R.dimen.game_activity_button_size),
                getResources().getDimension(R.dimen.game_activity_button_size));
        btnYesOrNo.setButtonClickable(false);
        btnYesOrNoContainer.addView(btnYesOrNo);
        //
        LinearLayout btnTimeChallengeContainer = view.findViewById(R.id.image_game_mode_time_challenge);
        ButtonView btnTimeChallenge = new ButtonView(
                getActivity(), getResources().getDrawable(R.drawable.icon_game_time_challenge),
                true,
                getResources().getDimension(R.dimen.game_activity_button_size),
                getResources().getDimension(R.dimen.game_activity_button_size));
        btnTimeChallenge.setButtonClickable(false);
        btnTimeChallengeContainer.addView(btnTimeChallenge);
        //
        LinearLayout btnFindThePairsContainer = view.findViewById(R.id.image_game_mode_find_the_pairs);
        ButtonView btnFindThePairs = new ButtonView(
                getActivity(), getResources().getDrawable(R.drawable.icon_game_find_the_pairs),
                true,
                getResources().getDimension(R.dimen.game_activity_button_size),
                getResources().getDimension(R.dimen.game_activity_button_size));
        btnFindThePairs.setButtonClickable(false);
        btnFindThePairsContainer.addView(btnFindThePairs);
        //
        LinearLayout outsideLayout = view.findViewById(R.id.fragment_outside_layout);
        outsideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        LinearLayout insideLayout = view.findViewById(R.id.fragment_inside_layout);
        insideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    @SuppressWarnings("All")
    public interface ButtonCloseClickListener {
        public void ButtonCloseClickListener();
    }

    private ButtonCloseClickListener buttonCloseClickListener;

    public void setButtonCloseClickListener(ButtonCloseClickListener buttonCloseClickListener) {
        this.buttonCloseClickListener = buttonCloseClickListener;
    }

    @SuppressWarnings("All")
    public interface ButtonTutorialClickListener {
        public void ButtonTutorialClickListener();
    }

    private ButtonTutorialClickListener buttonTutorialClickListener;

    public void setButtonTutorialClickListener(ButtonTutorialClickListener buttonTutorialClickListener) {
        this.buttonTutorialClickListener = buttonTutorialClickListener;
    }

}
