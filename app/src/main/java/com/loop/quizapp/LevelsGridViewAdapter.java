package com.loop.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelsGridViewAdapter extends BaseAdapter{

    private int[] _ids;
    private int[] _numbers;
    private Drawable[] _drawables;
    private LayoutInflater layoutInflater;
    Context _context;

    LevelsGridViewAdapter(Context context, int[] ids, int[] numbers, Drawable[] drawables) {
        _context = context;
        _ids = ids;
        _numbers = numbers;
        _drawables = drawables;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return _ids.length;
    }

    public String getItem(int position) {
        return String.valueOf(_ids[position]);
    }

    public long getItemId(int position) {
        return _ids[position];
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_view_levels_item_layout, parent, false);
        }
        ImageView backgroundImageView = view.findViewById(R.id.item_image);
        if (_drawables[position] != null) {
            backgroundImageView.setImageDrawable(_drawables[position]);
        }
        int cellColor = Color.parseColor("#"+Integer.toHexString(
                _context.getResources().getColor(R.color.encyclopediaLevelCellForeground)));
        backgroundImageView.setColorFilter(cellColor);
        TextView itemTextView = view.findViewById(R.id.item_text);
        itemTextView.setText(LocaleTextHelper.getLocaleNumber(_numbers[position]));
        return view;
    }

}
