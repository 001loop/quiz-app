package com.loop.quizapp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ButtonView extends RelativeLayout{

    Context _context;
    private RelativeLayout btn;
    private ImageView btnIcon;
    private TextView btnText;
    private boolean isAnimationAvailable = true;
    private boolean clickable = true;

    public ButtonView(Context context) {
        super(context);
        _context = context;
        LayoutInflater.from(context).inflate(R.layout.view_button, this);
        initView(context);
    }

    @SuppressWarnings("unused")
    public ButtonView(Context context, AttributeSet attrs) {
        super(context);
        _context = context;
        LayoutInflater.from(context).inflate(R.layout.view_button, this);
        initView(context);
    }

    public ButtonView(Context context, Drawable iconDrawable, boolean isNeedToSetIconColorFilter) {
        super(context);
        _context = context;
        LayoutInflater.from(context).inflate(R.layout.view_button, this);
        initView(context);
        setBtnIconDrawable(iconDrawable, isNeedToSetIconColorFilter);
    }

    public ButtonView(Context context, String text, float width, float height) {
        super(context);
        _context = context;
        LayoutInflater.from(context).inflate(R.layout.view_button, this);
        initView(context);
        setBtnText(text);
        setBtnSize(width, height);
    }

    public ButtonView(Context context, Drawable iconDrawable, boolean isNeedToSetIconColorFilter,
                      float width, float height) {
        super(context);
        _context = context;
        LayoutInflater.from(context).inflate(R.layout.view_button, this);
        initView(context);
        setBtnIconDrawable(iconDrawable, isNeedToSetIconColorFilter);
        setBtnSize(width, height);
    }

    public ButtonView(Context context,
                      Drawable firstIconDrawable, boolean isNeedToSetFirstIconColorFilter,
                      Drawable secondIconDrawable, boolean isNeedToSetSecondIconColorFilter,
                      float width, float height) {
        super(context);
        _context = context;
        LayoutInflater.from(context).inflate(R.layout.view_button_two_image_views, this);
        btn = this.findViewById(R.id.btn);
        ImageView btnIconFirst = findViewById(R.id.btn_icon_first);
        btnIconFirst.setImageDrawable(firstIconDrawable);
        ImageView btnIconSecond = findViewById(R.id.btn_icon_second);
        btnIconSecond.setImageDrawable(secondIconDrawable);
        btn.performClick();
        setBtnTouchListener();
        if (isNeedToSetFirstIconColorFilter) {
            btnIconFirst.setColorFilter(getResources().getColor(R.color.icon), PorterDuff.Mode.SRC_ATOP);
        }
        if (isNeedToSetSecondIconColorFilter) {
            secondIconDrawable.setColorFilter(getResources().getColor(R.color.icon), PorterDuff.Mode.SRC_ATOP);
        }
        setBtnSize(width, height);
    }

    private void initView(final Context context) {
        btn = this.findViewById(R.id.btn);
        btnIcon = this.findViewById(R.id.btn_icon);
        btnText = this.findViewById(R.id.btn_text);
        //btnBorder = this.findViewById(R.id.btn_border);
        //btnBackground = this.findViewById(R.id.btn_background);
        btn.performClick();
        //noinspection AndroidLintClickableViewAccessibility
        setBtnTouchListener();
    }

    private void setBtnTouchListener() {
        //noinspection AndroidLintClickableViewAccessibility
        btn.setOnTouchListener(new View.OnTouchListener() {
            Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (clickable) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                            Animation animation = AnimationUtils.loadAnimation(_context,
                                    R.anim.anim_touch_down_btn);
                            btn.startAnimation(animation);
                            isAnimationAvailable = true;
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            if (isAnimationAvailable) {
                                Animation animation = AnimationUtils.loadAnimation(_context,
                                        R.anim.anim_touch_up_cell);
                                btn.startAnimation(animation);
                                isAnimationAvailable = false;
                            }
                            if (rect == null) {
                                rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                            }
                            if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                                if (buttonClickListener != null) {
                                    buttonClickListener.ButtonClickListener();
                                }
                            }
                            break;
                        }
                        case MotionEvent.ACTION_MOVE: {
                            if (rect == null) {
                                rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                            }
                            if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                                if (isAnimationAvailable) {
                                    Animation animation = AnimationUtils.loadAnimation(_context,
                                            R.anim.anim_touch_up_cell);
                                    btn.startAnimation(animation);
                                    isAnimationAvailable = false;
                                }
                            }
                            break;
                        }
                    }
                }
                return true;
            }
        });
    }

    public void setBtnSize(float width, float height) {
        btn.setLayoutParams(new RelativeLayout.LayoutParams((int) width, (int) height));
    }

    public void setBtnHeight(float height) {
        btn.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, (int) height));
    }

    public void setBtnIconDrawable(Drawable drawable, boolean isNeedToSetIconColorFilter) {
        if (isNeedToSetIconColorFilter) {
            drawable.setColorFilter(getResources().getColor(R.color.icon), PorterDuff.Mode.SRC_ATOP);
        }
        btnIcon.setImageDrawable(drawable);
    }

    public void setBtnText(String text) {
        RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                (int)getResources().getDimension(R.dimen.button_size));
        btn.setLayoutParams(btnLayoutParams);
        btnText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.message_fragment_btn_text_size));
        btnText.setText(text);
        btnText.setSingleLine(true);
        btnText.setHorizontallyScrolling(true);
        btnText.setMarqueeRepeatLimit(-1);
        btnText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        btnText.setSelected(true);
    }

    /*
    public void setBtnBorderDrawable(Drawable drawable) {
        btnBorder.setImageDrawable(drawable);
    }

    public void setBtnBackgroundDrawable(Drawable drawable) {
        btnBackground.setImageDrawable(drawable);
    }

    public void setBtnBackgroundColor(int color) {
        btn.getBackground().setColorFilter(
                Color.parseColor("#"+Integer.toHexString(color)), PorterDuff.Mode.SRC_IN);
    }
    */

    @SuppressWarnings("deprecation")
    public void setBtnBackgroundGradientColor(int start, int end) {
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, new int[] {end, start});
        gd.setCornerRadius(getResources().getDimension(R.dimen.button_view_radius));
        btn.setBackgroundDrawable(gd);
    }

    /*
    public void setBtnBorderColor(int color) {
        btnBorder.setColorFilter(
                Color.parseColor("#"+Integer.toHexString(color)), PorterDuff.Mode.DST_ATOP);
    }

    public void setBtnIconColor(int color) {
        btnIcon.setColorFilter(
                Color.parseColor("#"+Integer.toHexString(color)), PorterDuff.Mode.DST_ATOP);
    }
    */

    /*
    public RelativeLayout getBtn() {
        return btn;
    }
    */

    @SuppressWarnings("deprecation")
    public void setButtonClickable(boolean isClickable) {
        clickable = isClickable;
        if (clickable) {
            setBtnBackgroundGradientColor(getResources().getColor(R.color.buttonBackgroundStart),
                    getResources().getColor(R.color.buttonBackgroundEnd));
        } else {
            setBtnBackgroundGradientColor(getResources().getColor(R.color.buttonNonClickableBackgroundStart),
                    getResources().getColor(R.color.buttonNonClickableBackgroundEnd));
        }
    }

    @SuppressWarnings("All")
    public interface ButtonClickListener {
        public void ButtonClickListener();
    }

    private ButtonClickListener buttonClickListener;

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

}
