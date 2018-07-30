package com.loop.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerFilterImageAndTextAdapter extends BaseAdapter {
    Context myContext;
    String[] myImagesNames;
    String[] myText;
    int myImagesTypes;
    int myItemHeight;
    final int IMAGE_TYPE_COUNTRY = 1;
    final int IMAGE_TYPE_TEAM = 2;
    final float IMAGE_WIDTH_FACTOR = 0.1f;
    LayoutInflater inflater;

    public SpinnerFilterImageAndTextAdapter(Context context, int itemHeight, String[] imagesNames,
                                            String[] text, int imagesType) {
        myContext = context;
        myImagesNames = imagesNames;
        myText = text;
        myImagesTypes = imagesType;
        myItemHeight = itemHeight;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        if (myImagesNames == null) {
            return 0;
        }
        return myImagesNames.length;
    }

    @Override
    public Object getItem(int i) {
        if (myImagesNames == null) {
            return null;
        }
        if (myImagesNames.length < i) {
            return null;
        }
        return myImagesNames[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_filter_image_and_text, null);
        int margin = (int)(myItemHeight*0.15f);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, myItemHeight);
        view.setPadding(margin, margin, margin, margin);
        view.setLayoutParams(params);
        ImageView image = view.findViewById(R.id.image);
        TextView text = view.findViewById(R.id.text);
        String directory;
        switch (myImagesTypes) {
            case IMAGE_TYPE_COUNTRY:
                directory = "country/";
                break;
            case IMAGE_TYPE_TEAM:
                directory = "team/";
                break;
            default:
                directory = "";
                break;
        }
        if (myImagesNames.length > i) {
            LoadDrawablesManager loadDrawablesManager = new LoadDrawablesManager(myContext);
            Drawable drawable = loadDrawablesManager.getDrawable(
                    directory + myImagesNames[i] + ".png",
                    IMAGE_WIDTH_FACTOR, 1.0f);
            if (drawable != null) {
                image.setImageDrawable(drawable);
            }
        }
        if (myText.length > i) {
            text.setText(myText[i]);
        }
        return view;
    }

    public int getPosition(String string) {
        if (myImagesNames == null || string == null) {
            return 0;
        }
        for (int x = 0; x < myImagesNames.length; x++) {
            if (myImagesNames[x].equals(string)) {
                return x;
            }
        }
        return 0;
    }

}
