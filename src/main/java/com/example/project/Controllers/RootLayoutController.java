package com.example.project.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class RootLayoutController
{
    @FXML
    private StackPane contentPane;

    public void setContent(Parent page) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(page);
    }
}
