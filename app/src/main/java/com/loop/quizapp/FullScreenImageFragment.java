package com.loop.quizapp;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FullScreenImageFragment extends Fragment {

    Context myContext;

    public FullScreenImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_screen_image, container, false);
        myContext = getActivity().getApplicationContext();
        Bundle bundle = getArguments();
        String imageName = "quiz";
        if (bundle != null) {
            imageName = bundle.getString("image_name");
        }
        LinearLayout imageContainer = view.findViewById(R.id.image_container);
        ImageView imageViewQuiz = new ImageView(getActivity());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        LoadDrawablesManager loadDrawablesManager = new LoadDrawablesManager(myContext);
        Drawable drawable = loadDrawablesManager.getDrawable(
                "quiz_images/" + imageName + ".png",
                1.0f, 1.0f);
        if (drawable != null) {
            imageParams.gravity = Gravity.CENTER;
            imageViewQuiz.setLayoutParams(imageParams);
            imageViewQuiz.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageViewQuiz.setAdjustViewBounds(true);
            imageViewQuiz.setImageDrawable(drawable);
            imageContainer.addView(imageViewQuiz);
        }
        LinearLayout outsideLayout = view.findViewById(R.id.fragment_outside_layout);
        outsideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        return view;
    }

    private void closeFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

}
