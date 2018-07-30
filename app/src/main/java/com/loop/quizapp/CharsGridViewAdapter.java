package com.loop.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CharsGridViewAdapter extends BaseAdapter{

    private String[] symbols;
    private boolean[] visibility;
    private LayoutInflater layoutInflater;
    Context context;

    CharsGridViewAdapter(Context _context, String[] _symbols, boolean[] _visibility) {
        context = _context;
        symbols = _symbols;
        visibility = _visibility;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return symbols.length;
    }

    public Character getItem(int position) {
        return symbols[position].charAt(0);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_view_item_layout, parent, false);
        }
        if (!visibility[position]) {
            view.setVisibility(View.INVISIBLE);
        }
        ImageView backgroundImageView = view.findViewById(R.id.item_background);
        Drawable background;
        background = ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_char_box, null);
        int cellColor = Color.parseColor("#"+Integer.toHexString(
                context.getResources().getColor(R.color.cellWithCharBackground)));
        backgroundImageView.setColorFilter(cellColor);
        backgroundImageView.setImageDrawable(background);
        TextView itemTextView = view.findViewById(R.id.item_text);
        ImageView itemImageView = view.findViewById(R.id.item_stroke);
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),
                R.drawable.icon_char_box_stroke, null);
        int borderColor = Color.parseColor("#"+Integer.toHexString(
                context.getResources().getColor(R.color.cellBorder)));
        itemImageView.setColorFilter(borderColor);
        itemImageView.setImageDrawable(drawable);
        String sym = symbols[position];
        itemTextView.setText(sym);
        return view;
    }

}