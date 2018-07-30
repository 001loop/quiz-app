package com.loop.quizapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

public class CharsFragment extends Fragment{

    GameActivityInterface gameActivityInterface;
    CharsGridViewAdapter wordsGridViewAdapter;
    String[] chars;
    boolean[] isVisibleItem;
    int numOfItems;
    GridView gridView;
    int previousItemId = -2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        gameActivityInterface = (GameActivityInterface) getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chars, container, false);
        // get data
        Bundle bundle = getArguments();
        if (bundle != null) {
            chars = bundle.getStringArray("chars");
            if (chars != null) {
                numOfItems = chars.length;
            }
        }
        isVisibleItem = new boolean[numOfItems];
        boolean[] visibility = SaveManager.getVisibility(getActivity());
        for (int x = 0; x < numOfItems; x++) {
            if (visibility == null) {
                isVisibleItem[x] = true;
            } else {
                isVisibleItem[x] = visibility[x];
            }
        }
        if (chars != null) {
            wordsGridViewAdapter = new CharsGridViewAdapter(getActivity(), chars, isVisibleItem);
            gridView = view.findViewById(R.id.grid_view);
            gridView.setAdapter(wordsGridViewAdapter);
            adjustGridView();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gameActivityInterface.sendDataFromCharsFragment(new Symbol(false, chars[position].charAt(0), position));
                }
            });
            //noinspection AndroidLintClickableViewAccessibility
            gridView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE: {
                            float currentXPosition = event.getX();
                            float currentYPosition = event.getY();
                            int position = gridView.pointToPosition((int) currentXPosition, (int) currentYPosition);
                            if (previousItemId != position) {
                                View item = gridView.getChildAt(position);
                                if (item != null) {
                                    Animation animation = AnimationUtils.loadAnimation(getActivity(),
                                            R.anim.anim_touch_down_cell);
                                    item.startAnimation(animation);
                                    previousItemId = position;
                                }
                            }
                            break;
                        }
                        case MotionEvent.ACTION_DOWN: {
                            float currentXPosition = event.getX();
                            float currentYPosition = event.getY();
                            int position = gridView.pointToPosition((int) currentXPosition, (int) currentYPosition);
                            View item = gridView.getChildAt(position);
                            if (item != null) {
                                Animation animation = AnimationUtils.loadAnimation(getActivity(),
                                        R.anim.anim_touch_down_cell);
                                item.startAnimation(animation);
                            }
                            break;
                        }
                    }
                    return false;
                }
            });
        }
        return view;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void showSymbols(int[] positions) {
        if (gridView == null) {
            return;
        }
        for (int index = 0; index < positions.length; index++) {
            View itemView = gridView.getChildAt(positions[index]);
            if (itemView != null) {
                isVisibleItem[positions[index]] = true;
                itemView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void removeIncorrectSymbols(String[] answer, int[] specSymbolsIndexes) {
        ArrayList<String> answerChars = new ArrayList<>();
        ArrayList<String> answerWithoutSpecSymbols = new ArrayList<>();
        answerChars.addAll(Arrays.asList(answer));
        for (int index = 0; index < answerChars.size(); index++) {
            if (!contains(specSymbolsIndexes, index)) {
                answerWithoutSpecSymbols.add(answer[index]);
            }
        }
        for (int index = 0; index < numOfItems; index++) {
            if (!isVisibleItem[index]) {
                int deleteIndex = answerWithoutSpecSymbols.indexOf(String.valueOf(chars[index]));
                answerWithoutSpecSymbols.remove(deleteIndex);
            }
        }
        for (int index = 0; index < numOfItems; index++) {
            if (isVisibleItem[index]) {
                if (answerWithoutSpecSymbols.contains(String.valueOf(chars[index]))) {
                    int deleteIndex = answerWithoutSpecSymbols.indexOf(String.valueOf(chars[index]));
                    answerWithoutSpecSymbols.remove(deleteIndex);
                } else {
                    removeSymbolPermanent(index);
                }
            }
        }
        /*
        for (int index = 0; index < numOfItems; index++) {
            if (answerWithoutSpecSymbols.contains(chars[index])) {
                answerWithoutSpecSymbols.remove(String.valueOf(chars[index]));
            } else {
                removeSymbolPermanent(index);
            }
        }
        */
    }

    public void removeSymbol(int position) {
        if ((gridView == null) || (position == -1)) {
            return;
        }
        View itemView = gridView.getChildAt(position);
        if (itemView == null) {
            return;
        }
        itemView.setVisibility(View.INVISIBLE);
        isVisibleItem[position] = false;
    }

    public void removeSymbolPermanent(int position) {
        if ((gridView == null) || (position == -1)) {
            return;
        }
        View itemView = gridView.getChildAt(position);
        if (itemView == null) {
            return;
        }
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_remove_cell);
        itemView.startAnimation(animation);
        itemView.setVisibility(View.INVISIBLE);
        isVisibleItem[position] = false;
    }

    public void showSymbol(int position) {
        if (gridView == null) {
            return;
        }
        View itemView = gridView.getChildAt(position);
        if (itemView == null) {
            return;
        }
        isVisibleItem[position] = true;
        itemView.setVisibility(View.VISIBLE);
    }

    public Symbol getSymbolWithChar(String character) {
        for (int index = 0; index < numOfItems; index++) {
            if (chars[index].equals(character) && isVisibleItem[index]) {
                return new Symbol(false, chars[index].charAt(0), index);
            }
        }
        return new Symbol();
    }

    private void adjustGridView(){
        gridView.setNumColumns(8);
        gridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.grid_view_vertical_margin));
        gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_view_item_horizontal_margin));
    }

    public boolean[] getIsVisibleItem() {
        return isVisibleItem;
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