package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.TileModel;
import javafx.scene.Node;

/**
 * Parent class of TileControllers.
 * @param <T> tile model type.
 */
public abstract class TileController<T extends TileModel>
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

    /**
     * Set the model this controller will observe.
     * @param tile tile model class.
     */
    public void setModel(T tile){
        model = tile;
    }

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