package com.example.project.models.tiles;

import java.util.Map;
import java.util.TreeMap;
import java.util.Random;

/**
 * Static class to store the scrabble base letter value.
 */
public class ScrabbleLettersValues
{
    /**
     * @param value value of scrabble letter
     * @param frequency population of tile
     */
    public record ScrabbleTileData(int value, int frequency) {}
    private static final TreeMap<Double, Character> cdfMap = new TreeMap<>();
    private static final Random random = new Random();

    private static final Map<Character, ScrabbleTileData> baseLetterData = Map.ofEntries(
            Map.entry('a', new ScrabbleTileData(1, 9)),
            Map.entry('b', new ScrabbleTileData(3, 2)),
            Map.entry('c', new ScrabbleTileData(3, 2)),
            Map.entry('d', new ScrabbleTileData(2, 4)),
            Map.entry('e', new ScrabbleTileData(1, 12)),
            Map.entry('f', new ScrabbleTileData(4, 2)),
            Map.entry('g', new ScrabbleTileData(2, 3)),
            Map.entry('h', new ScrabbleTileData(4, 2)),
            Map.entry('i', new ScrabbleTileData(1, 9)),
            Map.entry('j', new ScrabbleTileData(8, 1)),
            Map.entry('k', new ScrabbleTileData(5, 1)),
            Map.entry('l', new ScrabbleTileData(1, 4)),
            Map.entry('m', new ScrabbleTileData(3, 2)),
            Map.entry('n', new ScrabbleTileData(1, 6)),
            Map.entry('o', new ScrabbleTileData(1, 8)),
            Map.entry('p', new ScrabbleTileData(3, 2)),
            Map.entry('q', new ScrabbleTileData(10, 1)),
            Map.entry('r', new ScrabbleTileData(1, 6)),
            Map.entry('s', new ScrabbleTileData(1, 4)),
            Map.entry('t', new ScrabbleTileData(1, 6)),
            Map.entry('u', new ScrabbleTileData(1, 4)),
            Map.entry('v', new ScrabbleTileData(4, 2)),
            Map.entry('w', new ScrabbleTileData(4, 2)),
            Map.entry('x', new ScrabbleTileData(8, 1)),
            Map.entry('y', new ScrabbleTileData(4, 2)),
            Map.entry('z', new ScrabbleTileData(10, 1))
    );

    // Static initializer for building a CDF map
    static {

        // Calculate total number of tiles by summing frequencies
        double totalTiles = baseLetterData.values().stream()
                .mapToInt(ScrabbleTileData::frequency)
                .sum();

        double cumulativeFrequency = 0.0;

        // Populate the cdfMap
        for (Map.Entry<Character, ScrabbleTileData> entry : baseLetterData.entrySet()) {
            cumulativeFrequency += entry.getValue().frequency();
            cdfMap.put(cumulativeFrequency / totalTiles, entry.getKey());
        }
    }

    /**
     * Returns Integer of the letter according to scrabble
     * @param letter letter to get value of
     * @return Integer value of the letter, or 0 if not found
     */
    public static int getValue(Character letter) {
        ScrabbleTileData data = baseLetterData.get(Character.toLowerCase(letter));
        return (data != null) ? data.value() : 0;
    }

    /**
     * @return randomly selected tile based on CDF
     */
    public static Character drawRandomTile() {
        // Generate a random float between 0.0 (inclusive) and 1.0 (exclusive)
        double randomFloat = random.nextDouble();

        // Find the first entry in the CDF map whose key is greater than or equal to the random float.
        return cdfMap.tailMap(randomFloat, true).firstEntry().getValue();
    }

}
