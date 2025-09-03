//package com.example.project;
//
//public class ScoreWord {
//    public int calculateScore(String word) {
//        if (word == null || word.trim().isEmpty()) {
//            System.err.println("Error: Word cannot be null or empty.");
//            return 0;
//        }
//
//        int sumOfValues = 0;
//        String upperCaseWord = word.toUpperCase(); // Ensure case-insensitive scoring
//
//        // Iterate through each character of the word
//        for (char letter : upperCaseWord.toCharArray()) {
//            // Get the value from the map. Use getOrDefault for letters not in the map (e.g., spaces).
//            sumOfValues += letterValues.getOrDefault(letter, 0);
//        }
//
//        // Multiply the sum of values by the length of the word
//        return sumOfValues * word.length();
//    }
//    public static void main(String[] args) {
//        ScoreWord calculator = new ScoreWord();
//
//        String testWord1 = "EXAMPLE";
//        int score1 = calculator.calculateScore(testWord1);
//        System.out.println("The score for '" + testWord1 + "' is: " + score1);
//
//        String testWord2 = "JAVA";
//        int score2 = calculator.calculateScore(testWord2);
//        System.out.println("The score for '" + testWord2 + "' is: " + score2);
//
//        String testWord3 = "QUIZ";
//        int score3 = calculator.calculateScore(testWord3);
//        System.out.println("The score for '" + testWord3 + "' is: " + score3);
//    }
//}
