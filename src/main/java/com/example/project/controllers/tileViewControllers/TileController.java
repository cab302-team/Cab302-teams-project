package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import javafx.scene.Node;

/**
 * Parent class of TileControllers.
 * @param <T> tile model type.
 */
public abstract class TileController<T extends Tile>
{

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

    public T getModel()
    {
        return this.model;
    }
}