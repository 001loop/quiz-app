package com.loop.quizapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FindThePairsElementView extends FrameLayout{

    View foreground;
    ImageView glare;
    View border;
    LinearLayout layout;
    boolean IsImage;

    public FindThePairsElementView(Context context) {
        super(context);
    }

    public FindThePairsElementView(Context context, View view, int width, int height, boolean isImage) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_find_the_pairs_element, this);
        IsImage = isImage;
        this.setLayoutParams(new LayoutParams(width, height));
        foreground = this.findViewById(R.id.item_foreground);
        glare = this.findViewById(R.id.item_glare);
        glare.setVisibility(INVISIBLE);
        border = this.findViewById(R.id.item_border);
        Drawable borderDrawable;
        if (isImage) {
            borderDrawable = getResources().getDrawable(R.drawable.picked_item_border_image);
        } else {
            borderDrawable = getResources().getDrawable(R.drawable.picked_item_border_text);
        }
        border.setBackgroundDrawable(borderDrawable);
        layout = this.findViewById(R.id.item_layout);
        LayoutParams viewParams = new LayoutParams(width, height);
        view.setLayoutParams(viewParams);
        layout.addView(view);
        unpickItem();
    }

    public void pickItem() {
        if (IsImage) {
            foreground.setVisibility(VISIBLE);
        }
        border.setVisibility(VISIBLE);
    }

    public void unpickItem() {
        foreground.setVisibility(INVISIBLE);
        border.setVisibility(INVISIBLE);
    }

    public void startGlareAnimation() {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                2.0f,
                Animation.RELATIVE_TO_SELF,
                -1.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f);
        animation.setDuration(1500);
        animation.setFillAfter(true);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        glare.startAnimation(animation);
    }

    public void stopGlareAnimation() {
        glare.clearAnimation();
    }

}
