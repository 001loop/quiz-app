package com.loop.quizapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class PenaltyFragment extends Fragment{

    public PenaltyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penalty, container, false);
        Bundle bundle = getArguments();
        int time = 0;
        if (bundle != null) {
            time = bundle.getInt("time");
        }
        final AppCompatTextView textViewTime = view.findViewById(R.id.text_view_time);
        final LinearLayout textViewTitleContainer = view.findViewById(R.id.text_title_container);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setMax(time);
        final int finalTime = time;
        CountDownTimer countDownTimer = new CountDownTimer(finalTime,10) {
            int currentTime = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                currentTime++;
                progressBar.setProgress(finalTime - (int)millisUntilFinished);
                String timeString = LocaleTextHelper.getLocaleNumber(millisUntilFinished / 1000)
                        + "," + LocaleTextHelper.getLocaleNumber((millisUntilFinished / 100) % 10);
                textViewTime.setText(timeString);
            }
            @Override
            public void onFinish() {
                timerFinish();
            }
        };
        countDownTimer.start();
        Animation titleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.penalty_title);
        textViewTitleContainer.startAnimation(titleAnimation);
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

    private void timerFinish() {
        if (getActivity() != null) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

}
