package com.loop.quizapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class ImagePicker extends FrameLayout {

    Context myContext;

    FrameLayout mainLayout;
    ScrollView scrollView;
    LinearLayout elementsLayout;

    ArrayList<FindThePairsElementView> itemsViewsArray;
    FindThePairsElementView pickedItemView;

    public ImagePicker(Context context) {
        super(context);
    }

    public void setData(
            Context context, ArrayList<Drawable> drawables, int height, int width, int margin) {
        myContext = context;
        int imageViewHeight = height - 2 * margin;
        int imageViewWidth = width - 2 * margin;
        itemsViewsArray = new ArrayList<>();
        mainLayout = new FrameLayout(myContext);
        mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(mainLayout);
        scrollView = new ScrollView(myContext);
        scrollView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
        scrollView.setVerticalScrollBarEnabled(false);
        mainLayout.addView(scrollView);
        elementsLayout = new LinearLayout(myContext);
        elementsLayout.setOrientation(LinearLayout.VERTICAL);
        elementsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(elementsLayout);
        LinearLayout.LayoutParams elementsViewParams = new LinearLayout.LayoutParams(
                imageViewWidth, imageViewHeight);
        elementsViewParams.setMargins(margin, margin, margin, margin);
        for (int x = 0; x < drawables.size(); x ++) {
            ImageView currentImageView = new ImageView(myContext);
            currentImageView.setImageDrawable(drawables.get(x));
            currentImageView.setLayoutParams(elementsViewParams);
            final int position = x;
            FindThePairsElementView elementView = new FindThePairsElementView(
                    context, currentImageView, imageViewWidth, imageViewHeight, true);
            elementView.setLayoutParams(elementsViewParams);
            elementView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickOnItem(position);
                }
            });
            itemsViewsArray.add(elementView);
            elementsLayout.addView(elementView);
        }
    }

    public void tutorialScrollDown() {
        ObjectAnimator animator=ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getBottom() );
        animator.setDuration(3000);
        animator.start();
    }

    public void setTutorialActiveItemAll() {
        for (int x = 0; x < itemsViewsArray.size(); x++) {
            itemsViewsArray.get(x).setClickable(true);
        }
    }

    public void setTutorialActiveItem(int number) {
        for (int x = 0; x < itemsViewsArray.size(); x++) {
            FindThePairsElementView currentView = itemsViewsArray.get(x);
            if (x == number) {
                currentView.setClickable(true);
                currentView.startGlareAnimation();
            } else {
                currentView.setClickable(false);
                currentView.stopGlareAnimation();
            }
        }
    }

    private void clickOnItem(int itemPosition) {
        unpickItem();
        pickedItemView = itemsViewsArray.get(itemPosition);
        pickedItemView.pickItem();
        pickedItemView.stopGlareAnimation();
        if (itemClickListener != null) {
            itemClickListener.ItemClickListener(itemPosition);
        }
    }

    private void unpickItem() {
        if (pickedItemView != null) {
            pickedItemView.unpickItem();
        }
    }

    public void deletePickedItem() {
        if (pickedItemView == null || elementsLayout == null) {
            return;
        }
        elementsLayout.removeView(pickedItemView);
    }

    @SuppressWarnings("All")
    public interface ItemClickListener {
        public void ItemClickListener(int itemPosition);
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
