package com.example.project.models.tiles;


/**
 * An empty tile slot model. Empty tile slots store a letter tile.
 */
public class EmptyTileSlotModel extends Tile
{
    private LetterTile tile;

    /**
     * Set a new tile.
     * @param tile letter tile.
     */
    public void setTile(LetterTile tile) {
        this.tile = tile;
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/emptyTileSlot.fxml";
    }
}
