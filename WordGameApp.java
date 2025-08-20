package com.example.bruh;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class WordGameApp extends Application {


    // This is the word the user needs to guess
    private final String secretWord = "code";
    private final Label messageLabel = new Label("Guess the 4-letter word!");
    private final TextField guessInput = new TextField();
    private final Button guessButton = new Button("Guess");


    @Override
    public void start(Stage stage) {
        stage.setTitle("Simple Word Guessing Game");


// Set up the guess input
        guessInput.setPromptText("Enter a 4-letter word");
        guessInput.setMaxWidth(150);


// Button click checks the guess
        guessButton.setOnAction(e -> checkGuess());


// Layout setup
        VBox root = new VBox(10, messageLabel, guessInput, guessButton);
        root.setPadding(new Insets(20));


        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.show();
    }


    private void checkGuess() {
        String guess = guessInput.getText().trim().toLowerCase();


// Check if it's 4 letters
        if (guess.length() != 4) {
            messageLabel.setText("Please enter a 4-letter word.");
            return;
        }


// Check if it's correct
        if (guess.equals(secretWord)) {
            messageLabel.setText("Correct! The word was '" + secretWord + "'.");
        } else {
            messageLabel.setText("Wrong! Try again.");
        }


        guessInput.clear();
    }


    public static void main(String[] args) {
        launch();
    }
}