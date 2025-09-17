package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Parent class of TileControllers.
 * @param <T> tile model type.
 */
public abstract class TileController<T extends Tile>
{
    protected TileController() {}

    @FXML
    public void initialize()
    {
        bind(model);
    }

    public TileController(T tileObject)
    {
        try
        {
            this.model = tileObject;
            String fxmlPath = tileObject.getFXMLPath();
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
            loader.setController(this);
            Node node = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tile controller: " + tileObject.getFXMLPath(), e);
        }
    }

    /**
     * root ui node.
     * @return root ui Node
     */
    public abstract Node getRoot();

    /**
     * Binds tile to the controller as its model.
     * @param tile tile to bind.
     */
    public abstract void bind(T tile);

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