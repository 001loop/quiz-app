package com.loop.quizapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AboutAppFragment extends Fragment {

    ButtonView btnFirst;

    public AboutAppFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);
        LinearLayout btnContainer = view.findViewById(R.id.btn_container);
        LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btnContainerParams.setMargins(
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0,
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0);
        LinearLayout btnFirstContainer = new LinearLayout(getActivity());
        btnFirstContainer.setLayoutParams(btnContainerParams);
        btnFirst = new ButtonView(getActivity(),
                getResources().getString(R.string.message_fragment_btn_close),
                getResources().getDimension(R.dimen.fragment_button_size),
                getResources().getDimension(R.dimen.fragment_button_size));
        btnFirst.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (fragmentButtonClickListener != null) {
                    fragmentButtonClickListener.FragmentButtonClickListener();
                } else {
                    closeFragment();
                }
            }
        });
        btnFirst.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
        btnFirstContainer.addView(btnFirst);
        btnContainer.addView(btnFirstContainer);
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
    public interface FragmentButtonClickListener {
        public void FragmentButtonClickListener();
    }

    private FragmentButtonClickListener fragmentButtonClickListener;

    public void setFragmentButtonClickListener(FragmentButtonClickListener fragmentButtonClickListener) {
        this.fragmentButtonClickListener = fragmentButtonClickListener;
    }

}
