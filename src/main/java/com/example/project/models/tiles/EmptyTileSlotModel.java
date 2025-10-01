package com.example.project.models.tiles;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


/**
 * An empty tile slot model. Empty tile slots store a letter tile.
 */
public class EmptyTileSlotModel extends Tile
{
    private final ObjectProperty<LetterTile> tile = new SimpleObjectProperty<>(null);

    /**
     * The observable property representing the tile in this slot.
     * @return the tile property
     */
    public ObjectProperty<LetterTile> tileProperty() {
        return tile;
    }

    /**
     * Gets the current tile in this slot.
     * @return the LetterTile currently in this slot, or null if none
     */
    public LetterTile getTile() {
        return tile.get();
    }

    /**
     * Sets a new tile in this slot.
     * @param tile the tile to set, or null to clear the slot
     */
    public void setTile(LetterTile tile) {
        this.tile.set(tile);
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/emptyTileSlot.fxml";
    }
}
