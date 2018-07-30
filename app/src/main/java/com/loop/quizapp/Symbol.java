package com.loop.quizapp;

public class Symbol {

    private boolean isLocked;
    private char character;
    private int position;
    @SuppressWarnings("FieldCanBeLocal")
    private final char VOID_CHAR = ' ';

    public Symbol(boolean _isLocked, char _character, int _position) {
        isLocked = _isLocked;
        character = _character;
        position = _position;
    }

    public Symbol(char _character) {
        isLocked = false;
        character = _character;
        position = -1;
    }

    public Symbol() {
        isLocked = false;
        character = VOID_CHAR;
        position = -1;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public char getCharacter() {
        return character;
    }

    public int getPosition() {
        return position;
    }

    public void setIsLocked(boolean _isLocked) {
        isLocked = _isLocked;
    }

    static Symbol[] arrayFromString(String string) {
        int numOfChars = string.length();
        Symbol[] array = new Symbol[numOfChars];
        for (int x = 0; x < numOfChars; x++) {
            array[x] = new Symbol(string.charAt(x));
        }
        return  array;
    }

    public boolean isEqual(Symbol symbol) {
        return symbol.character == character;
    }

    public String getString() {
        return String.valueOf(character);
    }

}