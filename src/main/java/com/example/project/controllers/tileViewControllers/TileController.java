package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.scene.Node;

public abstract class TileController<T extends Tile>
{
    public abstract Node getRoot();

    public abstract void bind(T tile);

    protected T model;

    public Tile getModel()
    {
        return this.model;
    }
}