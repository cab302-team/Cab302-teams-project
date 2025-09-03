package com.example.project.Tiles;

import java.util.Map;

/**
 * Static class to store the scrabble base letter value.
 */
public class ScrabbleLettersValues
{
    private static final Map<Character, Integer> baseLetterScores = Map.<Character, Integer>ofEntries(
            Map.entry('a', 1),
            Map.entry('b', 3),
            Map.entry('c', 3),
            Map.entry('d', 2),
            Map.entry('e', 1),
            Map.entry('f', 4),
            Map.entry('g', 2),
            Map.entry('h', 4),
            Map.entry('i', 1),
            Map.entry('j', 8),
            Map.entry('k', 5),
            Map.entry('l', 1),
            Map.entry('m', 3),
            Map.entry('n', 1),
            Map.entry('o', 1),
            Map.entry('p', 3),
            Map.entry('q', 10),
            Map.entry('r', 1),
            Map.entry('s', 1),
            Map.entry('t', 1),
            Map.entry('u', 1),
            Map.entry('v', 4),
            Map.entry('w', 4),
            Map.entry('x', 8),
            Map.entry('y', 4),
            Map.entry('z', 10)
    );

    /**
     * Returns the int value of the letter according to the scrabble rules.
     * @param letter letter to get value of.
     * @return returns integer value of letter.
     */
    public static int getValue(Character letter)
    {
        return baseLetterScores.get(Character.toLowerCase(letter));
    }
}
