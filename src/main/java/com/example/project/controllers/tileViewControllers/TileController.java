package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Parent class of TileControllers.
 * @param <T> tile model type.
 */
public abstract class TileController<T extends Tile>
{
    @FXML
    public void initialise()
    {
        bind();
    }

    protected TileController() {}

    public TileController(T tileObject)
    {
        this.model = tileObject;

        try
        {
            String fxmlPath = tileObject.getFXMLPath();
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
            StackPane root = loader.load();
            this.setRoot(root);

            var pane = this.getRoot();

            pane.setOnMouseEntered(e -> {
                pane.setScaleX(1.1);
                pane.setScaleY(1.1);
                this.getModel().getHoverSoundPlayer().play();
            });

            pane.setOnMouseExited(e -> {
                pane.setScaleX(1.0);
                pane.setScaleY(1.0);
            });

        } catch (Exception e) {
            throw new RuntimeException("Failed to create tile controller: " + tileObject.getFXMLPath(), e);
        }
    }

    public abstract void setRoot(StackPane root);

    /**
     * root ui node.
     * @return root ui Node
     */
    public abstract Node getRoot();

    /**
     * Binds tile to the controller as its model.
     */
    public abstract void bind();

    protected T model;

    /**
     * gets the tile model.
     * @return model.
     */
    public T getModel()
    {
        return this.model;
    }
}