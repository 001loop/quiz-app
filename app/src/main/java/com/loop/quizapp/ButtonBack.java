package com.loop.quizapp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ButtonBack {

    public static void SetOnTouchListenerAndColorFilter(final Context context, final ImageView btnBack) {
        btnBack.setColorFilter(context.getResources().getColor(R.color.icon), PorterDuff.Mode.SRC_IN);
        //noinspection AndroidLintClickableViewAccessibility
        btnBack.setOnTouchListener(new View.OnTouchListener() {
            Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        Drawable drawable;
                        drawable = ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.icon_button_back_clicked, null);
                        btnBack.setImageDrawable(drawable);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Drawable drawable;
                        drawable = ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.icon_button_back, null);
                        btnBack.setImageDrawable(drawable);
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (rect == null) {
                            rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        }
                        if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                            Drawable drawable;
                            drawable = ResourcesCompat.getDrawable(context.getResources(),
                                    R.drawable.icon_button_back, null);
                            btnBack.setImageDrawable(drawable);
                        }
                    }
                }
                return false;
            }
        });
    }

}
