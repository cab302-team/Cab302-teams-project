package com.example.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * Root layout that remains the root scene of the application to switch scenes within.
 */
public class RootLayoutController
{
    @FXML
    private StackPane contentPane;

    /**
     * @param page Set page content to a game scene.
     */
    public void setContent(Parent page) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(page);
    }
}
