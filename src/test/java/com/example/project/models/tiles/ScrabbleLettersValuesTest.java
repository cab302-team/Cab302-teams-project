package com.example.project.models.tiles;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ScrabbleLettersValuesTest {

    // helper method to access the static baseLetterData
    private Map<Character, ScrabbleLettersValues.ScrabbleTileData> getBaseLetterData() {
        try {
            Field field = ScrabbleLettersValues.class.getDeclaredField("baseLetterData");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Character, ScrabbleLettersValues.ScrabbleTileData> data = (Map<Character, ScrabbleLettersValues.ScrabbleTileData>) field.get(null);
            return data;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not access ScrabbleLettersValues.baseLetterData", e);
        }
    }

    // Helper method to calculate total tiles dynamically
    private double getTotalTiles() {
        Map<Character, ScrabbleLettersValues.ScrabbleTileData> data = getBaseLetterData();
        return data.values().stream()
                .mapToInt(ScrabbleLettersValues.ScrabbleTileData::frequency)
                .sum();
    }

    @Test
    void getValueTest()
    {
        // Test existing letters
        assertEquals(1, ScrabbleLettersValues.getValue('a'));
        assertEquals(3, ScrabbleLettersValues.getValue('B'));

        // Test non-existing letters
        assertEquals(0, ScrabbleLettersValues.getValue(' '));
        assertEquals(0, ScrabbleLettersValues.getValue('?'));
    }
    @Test
    void drawRandomTileTest()
    {
        int sampleSize = 100000;
        double totalTiles = getTotalTiles();

        // values for calculating criticalValue
//        int degreesOfFreedom = 25; // 26 letters - 1
//        double confidenceLevel = 0.95; // 90-95% is standard

        // Generate Tally from sample size
        Map<Character, Integer> tally = new HashMap<>();
        for (int i = 0; i < sampleSize; i++) {
            char tile = ScrabbleLettersValues.drawRandomTile();
            tally.merge(tile, 1, Integer::sum);
        }

        // Chi-Squared statistic: this value is used to determine the relationship between
        // two categorical variables and if the observed distribution differs significantly
        // from the expected distribution
        double chiSquared = 0.0;

        Map<Character, ScrabbleLettersValues.ScrabbleTileData> baseLetterData = getBaseLetterData();

        for (Map.Entry<Character, ScrabbleLettersValues.ScrabbleTileData> entry : baseLetterData.entrySet()) {
            char letter = entry.getKey();
            int frequency = entry.getValue().frequency();

            double expectedCount = (frequency / totalTiles) * sampleSize;

            double observedCount = tally.getOrDefault(letter, 0);

            // Add to Chi-Squared sum, only if expected count is greater than 0 to avoid division by zero
            if (expectedCount > 0) {
                chiSquared += Math.pow(observedCount - expectedCount, 2) / expectedCount;
            }
        }

        // Calculating the critical value using a statistical library
        double criticalValue = 37.65;


        assertTrue(chiSquared < criticalValue,
                String.format("Chi-squared statistic (%.2f) is too high, indicating deviation from expected distribution. Critical value: %.2f",
                        chiSquared, criticalValue));
    }



}