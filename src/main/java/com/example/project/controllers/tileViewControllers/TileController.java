package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;

/**
 * @param <T> Tile Controller parent abstract class.
 */
public abstract class TileController<T extends Tile> {

    /**
     * Abstract method that takes a tile. and sets the UI elements.
     * @param tile the tile.
     */
    public abstract void setUIValues(T tile);
}