package com.example.project;

/**
 * Class for Scrabble inspired Tiles with Methods for getting Letters and Values
 */
public class LetterTile {

    private final Character letter;

    private final int value;


    public LetterTile(Character letter, int value) {

        this.letter = letter;

        this.value = value;

    }


    public Character getLetter(){

        return letter;

    }

    public int getValue(){

        return value;

    }

}
