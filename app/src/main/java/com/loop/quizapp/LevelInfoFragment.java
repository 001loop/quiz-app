package com.loop.quizapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LevelInfoFragment extends Fragment {

    ButtonView btnFirst;
    RelativeLayout imageContainer;
    float IMAGE_MAIN_WIDTH_FACTOR = 0.8f;
    float IMAGE_COUNTRY_AND_TEAM_WIDTH_FACTOR = 0.25f;
    float IMAGE_COUNTRY_AND_TEAM_WIDTH_FACTOR_SMALL = 0.18f;
    float IMAGE_ACHIEVEMENT_WIDTH_FACTOR = 0.25f;

    Context myContext;

    public LevelInfoFragment() {
        // Required empty public constructor
    }

    @Override
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_info, container, false);
        myContext = getActivity().getApplicationContext();
        Bundle bundle = getArguments();
        Level level = null;
        String btnFirstText = "";
        boolean isEndGameFragment = false;
        if (bundle != null) {
            level = (Level) bundle.getSerializable("level");
            btnFirstText = bundle.getString("btn_text");
            isEndGameFragment = bundle.getBoolean("is_end_game_fragment", false);
        }
        if (level == null) {
            closeFragment();
            return null;
        }
        String name = "";
        if (isEndGameFragment) {
            name = getResources().getString(R.string.end_game_fragment_title) +
                    System.getProperty("line.separator") + name;
        }
        String image = level.getImage();
        LinearLayout btnContainer = view.findViewById(R.id.btn_container);
        AppCompatTextView textName = view.findViewById(R.id.text_name);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textName, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        textName.setText(name);
        LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        btnContainerParams.setMargins(
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0,
                (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0);
        LinearLayout btnFirstContainer = new LinearLayout(myContext);
        btnFirstContainer.setLayoutParams(btnContainerParams);
        //
        btnFirst = new ButtonView(myContext, btnFirstText,
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
        LoadDrawablesManager loadDrawablesManager = new LoadDrawablesManager(myContext);
        if (image != null) {
            Drawable drawable = loadDrawablesManager.getDrawable(
                    "quiz_images/" + image + ".png",
                    IMAGE_MAIN_WIDTH_FACTOR, 1.0f);
            if (drawable != null) {
                setImageView(drawable, view);
            }
        }
        return view;
    }

    @SuppressWarnings("deprecation")
    private void setImageView(Drawable drawable, View view) {
        ImageView imageViewQuiz = new ImageView(myContext);
        imageViewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageClickListener != null) {
                    imageClickListener.ImageClickListener();
                }
            }
        });
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageParams.gravity = Gravity.CENTER;
        imageParams.setMargins(0, 0, 0,
                (int) getActivity().getResources().getDimension(R.dimen.padding_item));
        imageViewQuiz.setLayoutParams(imageParams);
        imageViewQuiz.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageViewQuiz.setAdjustViewBounds(true);
        imageViewQuiz.setImageDrawable(drawable);
        imageContainer = view.findViewById(R.id.image_description_container);
        imageContainer.addView(imageViewQuiz);
    }

    private void showImageInfo(String stringAuthor, final String stringSource) {
        final int TEXT_VIEW_AUTHOR_ID = 100;
        final int IMAGE_VIEW_INFO_ID = 101;
        final RelativeLayout layoutImageInfo = new RelativeLayout(myContext);
        int layoutPadding = getResources().getDimensionPixelSize(R.dimen.padding_item);
        layoutImageInfo.setPadding(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
        layoutImageInfo.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layoutImageInfo.setBackgroundColor(getResources().getColor(R.color.encyclopediaLevelCellForeground));
        ImageView imageViewImageInfo = new ImageView(myContext);
        imageViewImageInfo.setId(IMAGE_VIEW_INFO_ID);
        RelativeLayout.LayoutParams imageInfoParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.top_layout_button_back_size),
                getResources().getDimensionPixelSize(R.dimen.top_layout_button_back_size));
        imageInfoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            imageInfoParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        }
        imageInfoParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imageInfoParams.setMargins(0, 0, 0, layoutPadding);
        imageViewImageInfo.setLayoutParams(imageInfoParams);
        imageViewImageInfo.setImageDrawable(getResources().getDrawable(R.drawable.icon_image_info));
        layoutImageInfo.addView(imageViewImageInfo);
        TextView textViewAuthor = null;
        if (stringAuthor != null) {
            RelativeLayout.LayoutParams authorTextViewParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            authorTextViewParams.addRule(RelativeLayout.BELOW, IMAGE_VIEW_INFO_ID);
            if (stringSource != null) {
                authorTextViewParams.setMargins(0, 0, 0, layoutPadding);
            }
            textViewAuthor = new TextView(myContext);
            textViewAuthor.setId(TEXT_VIEW_AUTHOR_ID);
            textViewAuthor.setLayoutParams(authorTextViewParams);
            textViewAuthor.setTextColor(getResources().getColor(R.color.textInEncyclopediaLevelCell));
            textViewAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.message_fragment_text_size_small));
            textViewAuthor.setShadowLayer(8f, -1, 1,
                    getResources().getColor(R.color.textShadowInEncyclopediaLevelCell));
            String authorText = getResources().getString(R.string.author) + " " + stringAuthor;
            textViewAuthor.setText(authorText);
            layoutImageInfo.addView(textViewAuthor);
        }
        if (stringSource != null) {
            RelativeLayout.LayoutParams sourceTextViewParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (textViewAuthor != null) {
                sourceTextViewParams.addRule(RelativeLayout.BELOW, TEXT_VIEW_AUTHOR_ID);
            } else {
                sourceTextViewParams.addRule(RelativeLayout.BELOW, IMAGE_VIEW_INFO_ID);
            }
            TextView textViewSource = new TextView(myContext);
            textViewSource.setLayoutParams(sourceTextViewParams);
            textViewSource.setTextColor(getResources().getColor(R.color.textLink));
            textViewSource.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.message_fragment_text_size_small));
            textViewSource.setShadowLayer(8f, -1, 1,
                    getResources().getColor(R.color.textShadowInEncyclopediaLevelCell));
            SpannableString sourceSpannableString = new SpannableString(
                    getResources().getString(R.string.image_source));
            sourceSpannableString.setSpan(new UnderlineSpan(), 0, sourceSpannableString.length(), 0);
            textViewSource.setText(sourceSpannableString);
            textViewSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickOnSource(stringSource);
                }
            });
            layoutImageInfo.addView(textViewSource);
        }
        imageContainer.addView(layoutImageInfo);
        layoutImageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutImageInfo.setVisibility(View.GONE);
            }
        });
    }

    private void clickOnSource(String source) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(source));
        startActivity(browserIntent);
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

    @SuppressWarnings("All")
    public interface ImageClickListener {
        public void ImageClickListener();
    }

    private ImageClickListener imageClickListener;

    public void setImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

}
