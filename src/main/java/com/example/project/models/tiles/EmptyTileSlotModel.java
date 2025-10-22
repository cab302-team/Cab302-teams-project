package com.example.project.models.tiles;


import javafx.beans.property.SimpleObjectProperty;

/**
 * An empty tile slot model. Empty tile slots store a letter tile.
 */
public class EmptyTileSlotModel extends TileModel
{
    private SimpleObjectProperty<LetterTileModel> currentTile = new SimpleObjectProperty<>(null);

    /**
     * get letter tile in slot.
     * @return letter tile.
     */
    public LetterTileModel getTile(){
        return this.currentTile.get();
    }

    /**
     * Set a new tile.
     * @param tile letter tile.
     */
    public void setTile(LetterTileModel tile)
    {
        this.currentTile.set(tile);
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/emptyTileSlot.fxml";
    }
}
