package com.example.project.models.tiles;


public class EmptyTileSlot extends Tile
{
    private LetterTile tile;

    public boolean isEmpty() {
        return tile == null;
    }

    public void setTile(LetterTile tile) {
        this.tile = tile;
    }

    public LetterTile getTile() {
        return tile;
    }

    public void clear() {
        this.tile = null;
    }
}
