package com.loop.quizapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LevelInfoFragmentImageAndTextView extends RelativeLayout{

    private TextView textView;
    private ImageView imageView;

    public LevelInfoFragmentImageAndTextView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.level_info_fragment_image_and_text_view, this);
        initViews(context, null, null);
    }

    public LevelInfoFragmentImageAndTextView(Context context, String text, Drawable drawable) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_view, this);
        initViews(context, text, drawable);
    }

    public LevelInfoFragmentImageAndTextView(Context context, String text) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_view_one_line, this);
        initViews(context, text, null);
    }

    private void initViews(final Context context, String text, Drawable drawable) {
        textView = this.findViewById(R.id.text_view);
        textView.setText(text);
        imageView = findViewById(R.id.image_view);
        imageView.setImageDrawable(drawable);
    }

    public void setText(String text) {
        if (text != null) {
            textView.setText(text);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
    }

}
