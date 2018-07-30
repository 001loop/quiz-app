package com.loop.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AnswerGridViewAdapter extends BaseAdapter{

    int MAX_STANDARD_LENGTH = 10;
    String SYMBOL_LINE_BREAK = "→";
    private String[] answer;
    private String[] currentAnswer;
    int[] specSymbolsPositions;
    private int maxNumOfCharsInOneLine;
    private boolean[] isLocked;
    private int id;
    private LayoutInflater layoutInflater;
    Context context;

    AnswerGridViewAdapter(Context _context, String[] _answer, String[] _currentAnswer,
                          int[] _specSymbolPositions, int _maxNumOfCharsInOneLine ,
                          boolean[] _isLocked, int _id) {
        answer = _answer;
        currentAnswer = _currentAnswer;
        specSymbolsPositions = _specSymbolPositions;
        isLocked = _isLocked;
        maxNumOfCharsInOneLine = _maxNumOfCharsInOneLine;
        id = _id;
        context = _context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return answer.length;
    }

    public Character getItem(int position) {
        return answer[position].charAt(0);
    }

    public long getItemId(int position) {
        return id;
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
        int cellColor;
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_view_item_layout, parent, false);
        }
        TextView itemTextView = view.findViewById(R.id.item_text);
        if (maxNumOfCharsInOneLine > MAX_STANDARD_LENGTH) {
            itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.grid_view_item_text_size_small));
        }
        ImageView backgroundImageView = view.findViewById(R.id.item_background);
        ImageView strokeImageView = view.findViewById(R.id.item_stroke);
        if (contains(specSymbolsPositions, position)) {                 // special symbol
            if (answer[position].contains(SYMBOL_LINE_BREAK)) {         // line break item
                Drawable lineBreakDrawable = context.getResources().getDrawable(R.drawable.icon_line_break);
                lineBreakDrawable.setColorFilter(context.getResources().getColor(R.color.icon), PorterDuff.Mode.SRC_ATOP);
                backgroundImageView.setImageDrawable(lineBreakDrawable);
            } else {
                itemTextView.setText(answer[position]);
                backgroundImageView.setVisibility(View.INVISIBLE);
            }
            strokeImageView.setVisibility(View.INVISIBLE);
            view.setClickable(false);
            view.setEnabled(false);
        } else {                                                        // simple symbol
            if (answer[position].contains(" ")) {
                itemTextView.setText(" ");
                view.setVisibility(View.INVISIBLE);
            } else  {
                itemTextView.setText(String.valueOf(currentAnswer[position]));
            }
            if (currentAnswer[position].equals(" ")) {
                cellColor = Color.parseColor("#"+Integer.toHexString(
                        context.getResources().getColor(R.color.cellWithoutCharBackground)));
            } else {
                cellColor = Color.parseColor("#"+Integer.toHexString(
                        context.getResources().getColor(R.color.cellWithCharBackground)));
            }
            Drawable background;
            background = ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.icon_char_box, null);
            backgroundImageView.setColorFilter(cellColor);
            backgroundImageView.setImageDrawable(background);
            Drawable stroke;
            stroke = ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.icon_char_box_stroke, null);
            int borderColor;
            if (isLocked[position]) {
                borderColor = Color.parseColor("#"+Integer.toHexString(
                        context.getResources().getColor(R.color.cellBorderLocked)));
            } else {
                borderColor = Color.parseColor("#"+Integer.toHexString(
                        context.getResources().getColor(R.color.cellBorder)));
            }
            strokeImageView.setColorFilter(borderColor);
            strokeImageView.setImageDrawable(stroke);
        }
        return view;
    }

    private static boolean contains(final int[] array, final int value) {
        if (array == null) {
            return false;
        }
        boolean result = false;
        for(int i : array){
            if(i == value){
                result = true;
                break;
            }
        }
        return result;
    }

}
