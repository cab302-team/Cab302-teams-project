package com.example.project.services;

import javafx.scene.Parent;
import java.io.IOException;

/**
 * Interface for page loading.
 */
public interface PageLoader
{
    /**
     * Load method to load fxml page.
     * @param fxmlPath path to .fxml mfile.
     * @return returns the fxml file root node.
     * @throws IOException exception on failing.
     */
    Parent load(String fxmlPath) throws IOException;

    /**
     * Get the .fxml pages controller
     * @param <T> controller type.
     * @return page's controller.
     */
    <T> T getController();
}
