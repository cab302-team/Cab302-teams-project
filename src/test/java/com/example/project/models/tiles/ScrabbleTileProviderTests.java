package com.example.project.models.tiles;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ScrabbleTileProviderTests
{
    @BeforeAll
    static void beforeAll()
    {
        createTestCdfMap();
    }

    @Test
    void getValueTest()
    {
        // Test existing letters
        assertEquals(1, ScrabbleTileProvider.getValue('a'));
        assertEquals(3, ScrabbleTileProvider.getValue('B'));

        // Test non-existing letters
        assertEquals(0, ScrabbleTileProvider.getValue(' '));
        assertEquals(0, ScrabbleTileProvider.getValue('?'));
    }

    @Test
    void testDrawRandomTile_ProducesValidCharacters() {
        for (int i = 0; i < 1000; i++) {
            char tile = ScrabbleTileProvider.drawRandomTile();
            assertTrue(Character.isLetter(tile), "Tile should always be a letter");
            assertTrue(ScrabbleTileProvider.getValue(tile) > 0, "Tile should exist in base data");
        }
    }

    @Test
    void testDrawRandomTile_Deterministic()
    {
        var firstSeed = 42;
        var secondSeed = 50;
        var thirdSeed = 120;

        Random randomGen = new Random();
        randomGen.setSeed(firstSeed);
        var firstExpected = testCdfMap.tailMap(randomGen.nextDouble(), true).firstEntry().getValue();

        randomGen.setSeed(secondSeed);
        var secondExpected = testCdfMap.tailMap(randomGen.nextDouble(), true).firstEntry().getValue();

        randomGen.setSeed(thirdSeed);
        var thirdExpected = testCdfMap.tailMap(randomGen.nextDouble(), true).firstEntry().getValue();

        // seed the scrabble letters class
        // With fixed seed, first few draws should be consistent across runs
        ScrabbleTileProvider.seedRandomNumberGenerator(firstSeed);
        char first = ScrabbleTileProvider.drawRandomTile();

        ScrabbleTileProvider.seedRandomNumberGenerator(secondSeed);
        char second = ScrabbleTileProvider.drawRandomTile();

        ScrabbleTileProvider.seedRandomNumberGenerator(thirdSeed);
        char third = ScrabbleTileProvider.drawRandomTile();

        assertEquals(firstExpected, first, "Draw should match seeded");
        assertEquals(secondExpected, second, "Draw should match seeded");
        assertEquals(thirdExpected, third, "Draw should match seeded");
    }

    private static final TreeMap<Double, Character> testCdfMap = new TreeMap<>();

    private static void createTestCdfMap()
    {
        // Calculate total number of tiles by summing frequencies
        double totalTiles = baseLetterData.values().stream()
                .mapToInt(ScrabbleTileProvider.ScrabbleTileData::frequency)
                .sum();

        double cumulativeFrequency = 0.0;

        // Populate the cdfMap
        for (Map.Entry<Character, ScrabbleTileProvider.ScrabbleTileData> entry : baseLetterData.entrySet()) {
            cumulativeFrequency += entry.getValue().frequency();
            testCdfMap.put(cumulativeFrequency / totalTiles, entry.getKey());
        }
    }

    private static final Map<Character, ScrabbleTileProvider.ScrabbleTileData> baseLetterData = Map.ofEntries(
            Map.entry('a', new ScrabbleTileProvider.ScrabbleTileData(1, 9)),
            Map.entry('b', new ScrabbleTileProvider.ScrabbleTileData(3, 2)),
            Map.entry('c', new ScrabbleTileProvider.ScrabbleTileData(3, 2)),
            Map.entry('d', new ScrabbleTileProvider.ScrabbleTileData(2, 4)),
            Map.entry('e', new ScrabbleTileProvider.ScrabbleTileData(1, 12)),
            Map.entry('f', new ScrabbleTileProvider.ScrabbleTileData(4, 2)),
            Map.entry('g', new ScrabbleTileProvider.ScrabbleTileData(2, 3)),
            Map.entry('h', new ScrabbleTileProvider.ScrabbleTileData(4, 2)),
            Map.entry('i', new ScrabbleTileProvider.ScrabbleTileData(1, 9)),
            Map.entry('j', new ScrabbleTileProvider.ScrabbleTileData(8, 1)),
            Map.entry('k', new ScrabbleTileProvider.ScrabbleTileData(5, 1)),
            Map.entry('l', new ScrabbleTileProvider.ScrabbleTileData(1, 4)),
            Map.entry('m', new ScrabbleTileProvider.ScrabbleTileData(3, 2)),
            Map.entry('n', new ScrabbleTileProvider.ScrabbleTileData(1, 6)),
            Map.entry('o', new ScrabbleTileProvider.ScrabbleTileData(1, 8)),
            Map.entry('p', new ScrabbleTileProvider.ScrabbleTileData(3, 2)),
            Map.entry('q', new ScrabbleTileProvider.ScrabbleTileData(10, 1)),
            Map.entry('r', new ScrabbleTileProvider.ScrabbleTileData(1, 6)),
            Map.entry('s', new ScrabbleTileProvider.ScrabbleTileData(1, 4)),
            Map.entry('t', new ScrabbleTileProvider.ScrabbleTileData(1, 6)),
            Map.entry('u', new ScrabbleTileProvider.ScrabbleTileData(1, 4)),
            Map.entry('v', new ScrabbleTileProvider.ScrabbleTileData(4, 2)),
            Map.entry('w', new ScrabbleTileProvider.ScrabbleTileData(4, 2)),
            Map.entry('x', new ScrabbleTileProvider.ScrabbleTileData(8, 1)),
            Map.entry('y', new ScrabbleTileProvider.ScrabbleTileData(4, 2)),
            Map.entry('z', new ScrabbleTileProvider.ScrabbleTileData(10, 1))
    );
}