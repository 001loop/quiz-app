package com.loop.quizapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    ButtonView btnFirst;
    ButtonView btnSecond;
    ButtonView btnThird;
    TextView textViewMessage;
    TextView textViewTitle;
    boolean isOutsideLayoutClickable = false;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Bundle bundle = getArguments();
        String title = "";
        String message = "";
        String btnFirstText = "";
        String btnSecondText = "";
        String btnThirdText = "";
        float outsideLayoutAlpha = 0.8f;
        if (bundle != null) {
            title = bundle.getString("title");
            message = bundle.getString("message");
            btnFirstText = bundle.getString("btn_first_text");
            btnSecondText = bundle.getString("btn_second_text");
            btnThirdText = bundle.getString("btn_third_text");
            if (bundle.containsKey("outside_layout_alpha")) {
                outsideLayoutAlpha = bundle.getFloat("outside_layout_alpha");
            }
        }
        LinearLayout btnContainer = view.findViewById(R.id.btn_container);
        textViewTitle = view.findViewById(R.id.fragment_title);
        textViewTitle.setText(title);
        textViewMessage = view.findViewById(R.id.fragment_text);
        textViewMessage.setText(message);
        //
        LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btnContainerParams.setMargins(
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0,
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0);
        LinearLayout btnFirstContainer = new LinearLayout(getActivity());
        btnFirstContainer.setLayoutParams(btnContainerParams);
        //
        btnFirst = new ButtonView(getActivity(), btnFirstText,
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
        //btnContainer.addView(btnLater);
        if (btnSecondText != null) {
            LinearLayout btnSecondContainer = new LinearLayout(getActivity());
            btnSecondContainer.setLayoutParams(btnContainerParams);
            btnSecond = new ButtonView(getActivity(), btnSecondText,
                    getResources().getDimension(R.dimen.fragment_button_size),
                    getResources().getDimension(R.dimen.fragment_button_size));
            btnSecond.setButtonClickListener(new ButtonView.ButtonClickListener() {
                @Override
                public void ButtonClickListener() {
                    if (secondButtonClickListener != null) {
                        secondButtonClickListener.SecondButtonClickListener();
                    } else {
                        closeFragment();
                    }
                }
            });
            btnSecond.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
            btnSecondContainer.addView(btnSecond);
            btnContainer.addView(btnSecondContainer);
        }
        if (btnThirdText != null) {
            LinearLayout secondBtnContainer = view.findViewById(R.id.second_btn_container);
            LinearLayout.LayoutParams secondBtnContainerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            secondBtnContainerParams.setMargins(0,
                    (int) getResources().getDimension(R.dimen.fragment_between_elements_margin), 0, 0);
            secondBtnContainerParams.gravity = Gravity.CENTER_HORIZONTAL;
            secondBtnContainer.setLayoutParams(secondBtnContainerParams);
            LinearLayout btnThirdContainer = new LinearLayout(getActivity());
            btnThirdContainer.setLayoutParams(btnContainerParams);
            btnThird = new ButtonView(getActivity(), btnThirdText,
                    getResources().getDimension(R.dimen.fragment_button_size),
                    getResources().getDimension(R.dimen.fragment_button_size));
            btnThird.setButtonClickListener(new ButtonView.ButtonClickListener() {
                @Override
                public void ButtonClickListener() {
                    if (thirdButtonClickListener != null) {
                        thirdButtonClickListener.ThirdButtonClickListener();
                    } else {
                        closeFragment();
                    }
                }
            });
            btnThird.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
            btnThirdContainer.addView(btnThird);
            secondBtnContainer.addView(btnThirdContainer);
        }
        LinearLayout outsideLayout = view.findViewById(R.id.fragment_outside_layout);
        outsideLayout.setAlpha(outsideLayoutAlpha);
        outsideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOutsideLayoutClickable) {
                    if (outsideLayoutClickListener != null) {
                        outsideLayoutClickListener.OutsideLayoutClickListener();
                    }
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

    private void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    @SuppressWarnings("All")
    public interface OutsideLayoutClickListener {
        public void OutsideLayoutClickListener();
    }

    private OutsideLayoutClickListener outsideLayoutClickListener;

    public void setOutsideLayoutClickListener(OutsideLayoutClickListener outsideLayoutClickListener) {
        this.outsideLayoutClickListener = outsideLayoutClickListener;
    }

}
