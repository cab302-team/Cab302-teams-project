package com.example.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Default Controller for the main view.
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}