package com.loop.quizapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AnswerFragment extends Fragment implements AdapterView.OnItemClickListener{

    GameActivityInterface gameActivityInterface;
    RelativeLayout answerFragmentLayout;
    GridView[] gridViews;
    AnswerGridViewAdapter answerGridViewAdapter;

    private int[] linesIndex;
    private int[] specSymbolsIndexes;
    private String[] answer;
    private Symbol[] enteredChars;
    private int numOfItems = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        gameActivityInterface = (GameActivityInterface) getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        // get data
        Bundle bundle = getArguments();
        if (bundle != null) {
            answer = bundle.getStringArray("answer");
            if (answer != null) {
                numOfItems = answer.length;
            }
            linesIndex = bundle.getIntArray("lines_index");
            specSymbolsIndexes = bundle.getIntArray("spec_symbols_indexes");
        }
        Symbol[] symbols = SaveManager.getSymbols(getActivity());
        String[] currentAnswer = new String[numOfItems];
        for (int index = 0; index < numOfItems; index++) {
            currentAnswer[index] = " ";
        }
        if (symbols != null && symbols.length == numOfItems) {
            currentAnswer = getStringArrayFromSymbolArray(symbols);
        }
        enteredChars = new Symbol[numOfItems];
        for (int x = 0; x < numOfItems; x++) {
            if (symbols != null && symbols.length == numOfItems) {
                enteredChars[x] = symbols[x];
            } else {
                enteredChars[x] = new Symbol();
            }
            if (contains(specSymbolsIndexes, x)) {
                enteredChars[x].setIsLocked(true);
                enteredChars[x] = new Symbol(true, answer[x].charAt(0), -1);
            }
        }
        if (currentAnswer != null) {
            answerFragmentLayout = view.findViewById(R.id.answer_fragment_layout);
            int numOfGridViews = linesIndex.length;
            gridViews = new GridView[numOfGridViews];
            int startIndex = 0;
            LinearLayout line[];
            line = new LinearLayout[numOfGridViews];
            int maxNumOfCharsInOneLine = getMaxNumOfCharsInOneLine(linesIndex);
            for(int x = 0; x < numOfGridViews; x++) {
                answerGridViewAdapter = new AnswerGridViewAdapter(getActivity(),
                        Arrays.copyOfRange(answer, startIndex, linesIndex[x]),
                        Arrays.copyOfRange(currentAnswer, startIndex, linesIndex[x]),
                        getSpecSymbolsArrayForCurrentLine(specSymbolsIndexes, startIndex, linesIndex[x]),
                        maxNumOfCharsInOneLine,
                        Arrays.copyOfRange(getBooleanArrayFromSymbolArray(enteredChars), startIndex, linesIndex[x]),
                        x);
                gridViews[x] = new GridView(getActivity());
                gridViews[x].setAdapter(answerGridViewAdapter);
                int numOfItems = linesIndex[x] - startIndex;
                adjustGridView(gridViews[x], numOfItems);
                gridViews[x].setOnItemClickListener(this);
                line[x] = new LinearLayout(getActivity());
                line[x].setId(x + 1000);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                int verticalMargin = (int) getResources().getDimension(R.dimen.grid_view_vertical_margin);
                int horizontalMargin = (int) getResources().getDimension(R.dimen.grid_view_item_horizontal_margin);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int maxRowLength = getMaxRowLength(linesIndex);
                int denominator = (int) (maxRowLength * 1.33);
                if (maxRowLength < 8) {
                    denominator = 9;
                }
                int itemSize = screenWidth/denominator;
                params.height = itemSize + verticalMargin;
                params.width = itemSize * (numOfItems) + horizontalMargin * (numOfItems - 1);
                if (x == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                } else {
                    params.addRule(RelativeLayout.BELOW, line[x - 1].getId());
                }
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                line[x].setPadding(0, 0, 0, verticalMargin);
                line[x].setLayoutParams(params);
                line[x].setOrientation(LinearLayout.HORIZONTAL);
                line[x].addView(gridViews[x]);
                answerFragmentLayout.addView(line[x]);
                startIndex = linesIndex[x];
            }
        }
        return view;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private int getMaxNumOfCharsInOneLine(int[] linesIndex) {
        int startIndex = 0;
        int maxLength = 0;
        int currentLength;
        for (int i = 0; i < linesIndex.length; i++) {
            currentLength = linesIndex[i] - startIndex;
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
            startIndex = linesIndex[i];
        }
        return  maxLength;
    }

    private void adjustGridView(GridView gridView, int numOfColumns){
        gridView.setNumColumns(numOfColumns);
        gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_view_item_horizontal_margin));
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public int getIndexOfFreeCell() {
        for (int index = 0; index < numOfItems; index++) {
            if (cellIsVoid(index))
            {
                return index;
            }
        }
        return -1;
    }

    public int[] getIndexesOfUnlockedCells() {
        int numOfFreeCells = 0;
        for (int index = 0; index < numOfItems; index++) {
            if (!answer[index].equals(" ")) {
                if (!enteredChars[index].getIsLocked()) {
                    numOfFreeCells++;
                }
            }
        }
        int[] freeCells = new int[numOfFreeCells];
        int cellIndex = 0;
        for (int index = 0; index < numOfItems; index++) {
            if (!answer[index].equals(" ")) {
                if (!enteredChars[index].getIsLocked()) {
                    freeCells[cellIndex] = index;
                    cellIndex++;
                }
            }
        }
        return freeCells;
    }

    // is this visible unlocked cell without symbols?
    public boolean cellIsVoid(int index) {
        if (enteredChars[index].isEqual(new Symbol())) {
            if (!contains(specSymbolsIndexes, index)) {
                return true;
            }
        }
        return false;
    }

    public void setLockedSymbol(Symbol symbol, int index) {
        symbol.setIsLocked(true);
        enteredChars[index] = symbol;
        addCharToCell(index, symbol.getString());
        lockCell(index);
    }

    public Symbol[] getSymbolsByLock(boolean needLocked) {
        int numOfSymbols = 0;
        for (int index = 0; index < numOfItems; index++) {
            if (!(enteredChars[index].getIsLocked() && !needLocked) && !enteredChars[index].isEqual(new Symbol())) {
                numOfSymbols++;
            }
        }
        Symbol[] symbols = new Symbol[numOfSymbols];
        numOfSymbols = 0;
        for (int index = 0; index < numOfItems; index++) {
            if (!(enteredChars[index].getIsLocked() && !needLocked) && !enteredChars[index].isEqual(new Symbol())) {
                symbols[numOfSymbols] = enteredChars[index];
                if (!needLocked) {
                    removeSymbolFromAnswerField(index);
                }
                numOfSymbols++;
            }
        }
        return symbols;
    }

    private void swapSymbolsInsideAnswerFragment(int positionForRemove, int freeCellIndex) {
        gameActivityInterface.sendDataFromAnswerFragment(enteredChars[freeCellIndex]);
        enteredChars[freeCellIndex] = enteredChars[positionForRemove];
        setLockedSymbol(enteredChars[freeCellIndex], freeCellIndex);
        enteredChars[positionForRemove] = new Symbol();
        addCharToCell(positionForRemove, " ");
    }

    public void addSymbolHint() {
        int freeCellIndex = getIndexForLock();
        if (freeCellIndex == -1) {
            return;
        }
        // now we have index. try to put symbol to cell with this index
        Symbol symbol = enteredChars[freeCellIndex];
        if (symbol.getPosition() != -1 &&
                answer[freeCellIndex].equals(enteredChars[freeCellIndex].getString())) {
            // correct character already in this cell. just lock him
            enteredChars[freeCellIndex].setIsLocked(true);
            lockCell(freeCellIndex);
            checkAnswer();
            return;
        }
        Symbol wordsFragmentSymbol = gameActivityInterface.getSymbolFromCharsFragment(answer[freeCellIndex]);
        if (wordsFragmentSymbol.getPosition() != -1) {
            // words fragment have desired symbol
            gameActivityInterface.swapTwoSymbols(wordsFragmentSymbol, symbol, freeCellIndex);
            lockCell(freeCellIndex);
        } else {
            // words fragment haven`t desired symbol
            int unlockedSymbolPosition = getUnlockedSymbolPositionFromDisplay(answer[freeCellIndex], freeCellIndex);
            swapSymbolsInsideAnswerFragment(unlockedSymbolPosition, freeCellIndex);
        }
        checkAnswer();
    }

    // get index of unlocked cell
    private int getIndexForLock() {
        int[] unlockedIndexes = getIndexesOfUnlockedCells();
        Random random = new Random();
        int length = unlockedIndexes.length;
        if (length <= 0) {
            return -1;
        }
        int randomNum = random.nextInt(length);
        return unlockedIndexes[randomNum];
    }

    private int getUnlockedSymbolPositionFromDisplay(String character, int currentPosition) {
        for (int index = 0; index < numOfItems; index++) {
            if (index != currentPosition) {
                if (enteredChars[index].getString().equals(character) && !enteredChars[index].getIsLocked()) {
                    return index;
                }
            }
        }
        return -1;
    }

    public boolean addChar(Symbol symbol) {
        int index = this.getIndexOfFreeCell();
        if (index != -1) {
            enteredChars[index] = symbol;
            addCharToCell(index, enteredChars[index].getString());
            checkAnswer();
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean removeSymbolFromAnswerField(int position) {
        Symbol symbol = enteredChars[position];
        if (symbol.getIsLocked()) {
            startLockedCellAnimation(position);
        }
        if (!symbol.getIsLocked() && symbol.getPosition() != -1) {
            enteredChars[position] = new Symbol();
            addCharToCell(position, " ");
            gameActivityInterface.sendDataFromAnswerFragment(symbol);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private void addCharToCell(int index, String charSymbol) {
        int gridViewIndex = getGridViewIdByItemIndex(index);
        index = getGridViewIndex(index, gridViewIndex);
        View view = gridViews[gridViewIndex].getChildAt(index);
        if (view == null) {
            return;
        }
        TextView itemTextView = view.findViewById(R.id.item_text);
        itemTextView.setText(charSymbol);
        ImageView backgroundImageView = view.findViewById(R.id.item_background);
        Drawable background;
        background = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_char_box, null);
        int cellColor;
        if (charSymbol.equals(" ")) {
            cellColor = Color.parseColor("#"+Integer.toHexString(
                    getResources().getColor(R.color.cellWithoutCharBackground)));
        } else {
            cellColor = Color.parseColor("#"+Integer.toHexString(
                    getResources().getColor(R.color.cellWithCharBackground)));
        }
        backgroundImageView.setColorFilter(cellColor);
        backgroundImageView.setImageDrawable(background);
    }

    @SuppressWarnings("deprecation")
    private void lockCell(int index) {
        int gridViewIndex = getGridViewIdByItemIndex(index);
        index = getGridViewIndex(index, gridViewIndex);
        View view = gridViews[gridViewIndex].getChildAt(index);
        if (view == null) {
            return;
        }
        ImageView itemImageView = view.findViewById(R.id.item_stroke);
        int color = Color.parseColor("#"+Integer.toHexString(
                getResources().getColor(R.color.cellBorderLocked)));
        itemImageView.setColorFilter(color);
    }

    public void checkAnswer() {
        if (Arrays.equals(getStringArrayFromSymbolArray(enteredChars), answer)) {
            gameActivityInterface.sendCorrectAnswerMessage();
        } else if (isNoEmptyCells(enteredChars)) {
            gameActivityInterface.sendWrongAnswerMessage();
        }
    }

    private void startLockedCellAnimation(int index) {
        int gridViewIndex = getGridViewIdByItemIndex(index);
        index = getGridViewIndex(index, gridViewIndex);
        View view = gridViews[gridViewIndex].getChildAt(index);
        if (view == null) {
            return;
        }
        ImageView stroke = view.findViewById(R.id.item_stroke);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_click_on_locked_cell);
        stroke.startAnimation(animation);
    }

    private void startClickOnCellAnimation(int index) {
        int gridViewIndex = getGridViewIdByItemIndex(index);
        index = getGridViewIndex(index, gridViewIndex);
        View view = gridViews[gridViewIndex].getChildAt(index);
        if (view == null) {
            return;
        }
        RelativeLayout layout = view.findViewById(R.id.item_background_layout);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_click_on_non_locked_cell);
        layout.startAnimation(animation);
    }

    private boolean isNoEmptyCells(Symbol[] symbols) {
        int numOfSpaces = 0;
        for (int i=0; i < symbols.length; i++){
            if (answer[i].equals(" ")) {
                numOfSpaces++;
            }
        }
        if (numOfSpaces > 0) {
            ArrayList<Integer> spacesIndexes = new ArrayList<>();
            for (int i = 0; i < symbols.length; i++) {
                if (answer[i].equals(" ")) {
                    spacesIndexes.add(i);
                }
            }
            ArrayList<String> enteredCharsWithoutSpaces = new ArrayList<>();
            for (int i = 0; i < answer.length; i++) {
                if (!spacesIndexes.contains(i)) {
                    enteredCharsWithoutSpaces.add(enteredChars[i].getString());
                }
            }
            return !enteredCharsWithoutSpaces.contains(" ");
        } else {
            String string = getStringFromSymbolArray(symbols);
            return !string.contains(" ");
        }
    }


    private int getGridViewIdByItemIndex(int index) {
        for(int x = 0; x < linesIndex.length; x++) {
            if (index < linesIndex[x]) {
                return x;
            }
        }
        return 0;
    }

    private int getGridViewIndex(int index, int gridViewIndex) {
        if (gridViewIndex > 0) {
            index = index - linesIndex[gridViewIndex - 1];
        }
        return index;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private String getStringFromSymbolArray(Symbol[] symbolArray) {
        int length = symbolArray.length;
        StringBuilder string = new StringBuilder();
        for (int index = 0; index < length; index++) {
            string.append(symbolArray[index].getString());
        }
        return string.toString();
    }

    public Symbol[] getEnteredChars() {
        return enteredChars;
    }

    private String[] getStringArrayFromSymbolArray(Symbol[] symbols) {
        int length = symbols.length;
        String[] string = new String[length];
        for (int index = 0; index < length; index++) {
            string[index] = symbols[index].getString();
        }
        return string;
    }

    private boolean[] getBooleanArrayFromSymbolArray(Symbol[] symbols) {
        int length = symbols.length;
        boolean[] bool = new boolean[length];
        for (int index = 0; index < length; index++) {
            bool[index] = symbols[index].getIsLocked();
        }
        return bool;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private int[] getSpecSymbolsArrayForCurrentLine(
            int[] specSymbolsIndexes, int startIndex, int endIndex) {
        if (specSymbolsIndexes == null) {
            return null;
        }
        int numOfAllSpecSymbols = specSymbolsIndexes.length;
        int numOfCurrentLineSpecSymbols = 0;
        for (int x =0; x < numOfAllSpecSymbols; x++) {
            if (specSymbolsIndexes[x] >= startIndex && specSymbolsIndexes[x] <= endIndex) {
                numOfCurrentLineSpecSymbols++;
            }
        }
        if (numOfCurrentLineSpecSymbols == 0) {
            return null;
        }
        int[] currentLineSpecSymbols = new int[numOfCurrentLineSpecSymbols];
        int currentIndex = 0;
        for (int x = 0; x < numOfAllSpecSymbols; x++) {
            if (specSymbolsIndexes[x] >= startIndex && specSymbolsIndexes[x] <= endIndex) {
                currentLineSpecSymbols[currentIndex] = specSymbolsIndexes[x] - startIndex;
                currentIndex ++;
            }
        }
        return currentLineSpecSymbols;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int globalPosition = position;
        if (id > 0) {
            globalPosition = position + linesIndex[(int)(long)id - 1];
        }
        if (!enteredChars[globalPosition].getIsLocked() && !enteredChars[globalPosition].getString().equals(" ")) {
            startClickOnCellAnimation(globalPosition);
        }
        removeSymbolFromAnswerField(globalPosition);
    }

    private int getMaxRowLength(int[] linesIndex) {
        int maxLength = linesIndex[0];
        for (int x = 1; x < linesIndex.length; x++) {
            int currentLength = linesIndex[x] - linesIndex[x-1];
            if (maxLength < currentLength) {
                maxLength = currentLength;
            }
        }
        return maxLength;
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

