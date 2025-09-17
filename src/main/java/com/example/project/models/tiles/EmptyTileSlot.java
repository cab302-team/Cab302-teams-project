package com.example.project.models.tiles;


/**
 * An empty tile slot model. Empty tile slots store a letter tile.
 */
public class EmptyTileSlot extends Tile
{
    private LetterTile tile;

    /**
     * Set a new tile.
     * @param tile letter tile.
     */
    public void setTile(LetterTile tile) {
        this.tile = tile;
    }

    /**
     * Gets the current tile in this slot.
     *
     * @return the LetterTile currently in this slot, or null if empty.
     */
    public LetterTile getTile() {
        return this.tile;
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/emptyTileSlot.fxml";
    }
}
