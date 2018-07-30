package com.loop.quizapp;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class TextPicker extends FrameLayout {

    Context myContext;

    FrameLayout mainLayout;
    ScrollView scrollView;
    LinearLayout elementsLayout;

    ArrayList<FindThePairsElementView> itemsViewsArray;
    FindThePairsElementView pickedItemView;

    public TextPicker(Context context) {
        super(context);
    }

    public void setData(
            Context context, ArrayList<String> stringArray, int height, int width, int margin) {
        myContext = context;
        int elementViewHeight = height - 2 * margin;
        int elementViewWidth = width - 2 * margin;
        float TEXT_HEIGHT_COEFFICIENT = 0.6f;
        float TEXT_WIDTH_COEFFICIENT = 0.8f;
        int textViewHeight = (int)(height * TEXT_HEIGHT_COEFFICIENT);
        int textViewWidth = (int)(width * TEXT_WIDTH_COEFFICIENT);
        itemsViewsArray = new ArrayList<>();
        mainLayout = new FrameLayout(myContext);
        mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
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
                elementViewWidth, elementViewHeight);
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(textViewWidth, textViewHeight);
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        elementsViewParams.setMargins(margin, margin, margin, margin);
        for (int x = 0; x < stringArray.size(); x ++) {
            RelativeLayout currentTextViewLayout = new RelativeLayout(context);
            currentTextViewLayout.setLayoutParams(elementsViewParams);
            currentTextViewLayout.setBackgroundColor(getResources().getColor(R.color.findThePairsTextItemBackground));
            AppCompatTextView currentTextView = new AppCompatTextView(myContext);
            currentTextView.setLayoutParams(textViewParams);
            currentTextView.setGravity(Gravity.CENTER);
            currentTextView.setText(stringArray.get(x));
            currentTextView.setTextColor(getResources().getColor(R.color.textFindThePairsTextPickerTextColor));
            currentTextView.setMaxLines(2);
            TextViewCompat.setAutoSizeTextTypeWithDefaults(currentTextView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            currentTextViewLayout.addView(currentTextView);
            final int position = x;
            FindThePairsElementView elementView = new FindThePairsElementView(
                    context, currentTextViewLayout, elementViewWidth, elementViewHeight, false);
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

    public void setTutorialActiveItemAll() {
        for (int x = 0; x < itemsViewsArray.size(); x++) {
            itemsViewsArray.get(x).setClickable(true);
        }
    }

    @SuppressWarnings("SameParameterValue")
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
