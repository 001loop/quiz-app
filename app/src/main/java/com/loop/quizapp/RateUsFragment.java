package com.loop.quizapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateUsFragment extends Fragment {

    ButtonView btnLater;
    ButtonView btnRate;
    RatingBar ratingBar;
    TextView textViewMessage;
    TextView textViewTitle;
    boolean isOutsideLayoutClickable = false;
    float outsideLayoutAlpha = 0.8f;
    int currentRating = 0;

    public RateUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_us, container, false);
        Bundle bundle = getArguments();
        LinearLayout btnContainer = view.findViewById(R.id.btn_container);
        textViewTitle = view.findViewById(R.id.fragment_title);
        textViewTitle.setText(getResources().getString(R.string.message_fragment_title_rate_us));
        textViewMessage = view.findViewById(R.id.fragment_text);
        textViewMessage.setText(getResources().getString(R.string.message_fragment_text_rate_us));
        ratingBar = view.findViewById(R.id.rating_bar);
        ratingBar.setMax(5);
        ratingBar.setStepSize(1.0f);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                currentRating = (int) ratingBar.getRating();
                if (currentRating > 0) {
                    btnRate.setButtonClickable(true);
                } else {
                    btnRate.setButtonClickable(false);
                }
            }
        });
        //
        LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btnContainerParams.setMargins(
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0,
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0);
        LinearLayout btnFirstContainer = new LinearLayout(getActivity());
        btnFirstContainer.setLayoutParams(btnContainerParams);
        //
        btnLater = new ButtonView(getActivity(), getResources().getString(R.string.message_fragment_btn_later),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnLater.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (buttonLaterClickListener != null) {
                    buttonLaterClickListener.ButtonLaterClickListener();
                } else {
                    closeFragment();
                }
            }
        });
        btnLater.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnFirstContainer.addView(btnLater);
        btnContainer.addView(btnFirstContainer);
        LinearLayout btnSecondContainer = new LinearLayout(getActivity());
        btnSecondContainer.setLayoutParams(btnContainerParams);
        btnRate = new ButtonView(getActivity(), getResources().getString(R.string.message_fragment_btn_rate),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnRate.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (buttonRateClickListener != null) {
                    buttonRateClickListener.ButtonRateClickListener(currentRating);
                } else {
                    closeFragment();
                }
            }
        });
        btnRate.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnRate.setButtonClickable(false);
        btnSecondContainer.addView(btnRate);
        btnContainer.addView(btnSecondContainer);
        LinearLayout outsideLayout = view.findViewById(R.id.fragment_outside_layout);
        outsideLayout.setAlpha(outsideLayoutAlpha);
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
        return view;
    }

    public void setOutsideLayoutClickable(boolean isClickable) {
        isOutsideLayoutClickable = isClickable;
    }

    @SuppressWarnings("All")
    public interface ButtonLaterClickListener {
        public void ButtonLaterClickListener();
    }

    private ButtonLaterClickListener buttonLaterClickListener;

    public void setButtonLaterClickListener(ButtonLaterClickListener buttonLaterClickListener) {
        this.buttonLaterClickListener = buttonLaterClickListener;
    }

    @SuppressWarnings("All")
    public interface ButtonRateClickListener {
        public void ButtonRateClickListener(int rating);
    }

    private ButtonRateClickListener buttonRateClickListener;

    public void setButtonRateClickListener(ButtonRateClickListener buttonRateClickListener) {
        this.buttonRateClickListener = buttonRateClickListener;
    }

    private void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

}
