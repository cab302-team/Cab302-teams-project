package com.example.project.models.tiles;

/**
 * Parent tile class for objects in the scene that are tiles. Upgrades and tiles.
 */
public abstract class Tile
{
    public abstract String getFXMLPath();

    /**
     * Tile size.
     */
    public static final int TILE_SIZE = 50;

    public static final int CORNER_RADIUS = 20;
}
