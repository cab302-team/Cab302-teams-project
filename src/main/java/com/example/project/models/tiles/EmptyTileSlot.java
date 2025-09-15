package com.example.project.models.tiles;


public class EmptyTileSlot extends Tile
{
    private LetterTile tile;

    public void setTile(LetterTile tile) {
        this.tile = tile;
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/emptyTileSlot.fxml";
    }
}
