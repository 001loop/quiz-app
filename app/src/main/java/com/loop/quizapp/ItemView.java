package com.loop.quizapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemView extends RelativeLayout{

    private TextView textViewTop;
    private TextView textViewBot;
    private ButtonView btn;

    public ItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_view, this);
    }

    public ItemView(Context context, String topText, String botText,
                    Drawable drawable, boolean isNeedToSetIconColorFilter) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_view, this);
        initViews(context, topText, botText, drawable, isNeedToSetIconColorFilter);
    }

    public ItemView(Context context, String text,
                    Drawable drawable, boolean isNeedToSetIconColorFilter) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_view_one_line, this);
        initViews(context, text, null, drawable, isNeedToSetIconColorFilter);
    }

    private void initViews(final Context context, String topText, String botText,
                           Drawable drawable, boolean isNeedToSetIconColorFilter) {
        textViewTop = this.findViewById(R.id.text_view_top);
        textViewTop.setText(topText);
        if (botText != null) {
            textViewBot = this.findViewById(R.id.text_view_bot);
            textViewBot.setText(botText);
        }
        LinearLayout btnContainer = findViewById(R.id.btn_container);
        btn = new ButtonView(context, drawable, isNeedToSetIconColorFilter);
        btn.setButtonClickListener(new ButtonView.ButtonClickListener() {
            @Override
            public void ButtonClickListener() {
                if (itemButtonClickListener != null) {
                    itemButtonClickListener.ItemButtonClickListener();
                }
            }
        });
        btnContainer.addView(btn);
    }

    public void setTextColor(int color) {
        if (textViewTop != null) {
            textViewTop.setTextColor(color);
        }
        if (textViewBot != null) {
            textViewBot.setTextColor(color);
        }
    }

    public void setBotText(String botTextNew) {
        textViewBot.setText(botTextNew);
    }

    public void setButtonIconDrawable(Drawable drawable, boolean isNeedToSetIconColorFilter) {
        btn.setBtnIconDrawable(drawable, isNeedToSetIconColorFilter);
    }

    public void setButtonClickable(boolean clickable) {
        btn.setButtonClickable(clickable);
    }

    @SuppressWarnings("All")
    public interface ItemButtonClickListener {
        public void ItemButtonClickListener();
    }

    private ItemButtonClickListener itemButtonClickListener;

    public void setItemButtonClickListener(ItemButtonClickListener itemButtonClickListener) {
        this.itemButtonClickListener = itemButtonClickListener;
    }

}
