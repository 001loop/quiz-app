package com.loop.quizapp;

import android.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrivacyPolicyFragment extends Fragment {

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        LinearLayout btnContainer = view.findViewById(R.id.btn_container);
        LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btnContainerParams.setMargins(
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0,
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0);
        LinearLayout btnFirstContainer = new LinearLayout(getActivity());
        btnFirstContainer.setLayoutParams(btnContainerParams);
        //
        final CheckBox checkBoxPersonalizedAds = view.findViewById(R.id.checkbox_personalized_ads);
        checkBoxPersonalizedAds.setChecked(true);
        //
        ButtonView btnAccept = new ButtonView(getActivity(), getResources().getString(R.string.agree_and_continue),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnAccept.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (acceptClickListener != null) {
                    acceptClickListener.AcceptClickListener(checkBoxPersonalizedAds.isChecked());
                } else {
                    closeFragment();
                }
            }
        });
        btnAccept.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnFirstContainer.addView(btnAccept);
        btnContainer.addView(btnFirstContainer);
        //
        TextView textViewTitle = view.findViewById(R.id.fragment_title);
        textViewTitle.setText(getResources().getString(R.string.app_name));
        //
        TextView textViewText = view.findViewById(R.id.fragment_text);
        String message = getResources().getString(R.string.message_privacy_policy);
        textViewText.setText(message);
        TextView textViewLinkText = view.findViewById(R.id.fragment_text_link);
        SpannableString privacyPolicyString = new SpannableString(getResources().getString(R.string.privacy_policy));
        privacyPolicyString.setSpan(new UnderlineSpan(), 0, privacyPolicyString.length(), 0);
        textViewLinkText.setText(privacyPolicyString);
        textViewLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readPrivacyPolicyListener != null) {
                    readPrivacyPolicyListener.ReadPrivacyPolicyListener();
                } else {
                    closeFragment();
                }
            }
        });
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
    public interface AcceptClickListener {
        public void AcceptClickListener(boolean isAdsPersonalized);
    }

    private AcceptClickListener acceptClickListener;

    public void setAcceptClickListener(AcceptClickListener acceptClickListener) {
        this.acceptClickListener = acceptClickListener;
    }

    @SuppressWarnings("All")
    public interface ReadPrivacyPolicyListener {
        public void ReadPrivacyPolicyListener();
    }

    private ReadPrivacyPolicyListener readPrivacyPolicyListener;

    public void setReadPrivacyPolicyListener(ReadPrivacyPolicyListener readPrivacyPolicyListener) {
        this.readPrivacyPolicyListener = readPrivacyPolicyListener;
    }

}
