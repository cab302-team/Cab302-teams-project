package com.example.project.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * FXML Loader wrapper class for the project to load the game screen FXML pages.
 */
public class FXMLPageLoader implements PageLoader
{
    private FXMLLoader loader;

    /**
     * constructor.
     */
    public FXMLPageLoader() {
        this.loader = new FXMLLoader();
    }

    @Override
    public Parent load(String fxmlPath) throws IOException
    {
        this.loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
        return loader.load();
    }

    @Override
    public <T> T getController() {
        return loader.getController();
    }
}
