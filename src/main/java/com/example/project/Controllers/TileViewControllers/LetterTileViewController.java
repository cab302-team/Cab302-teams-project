package com.example.project.Controllers.TileViewControllers;

import com.example.project.Models.Tiles.LetterTile;
import com.example.project.Models.Tiles.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

/**
 * Controls the layout of the letterUIModel. Which is a letter tile.
 */
public class LetterTileViewController extends TileController<LetterTile>
{
    @FXML
    Rectangle theBackground = new Rectangle(Tile.TILE_SIZE, Tile.TILE_SIZE);

    @FXML
    Label letterLabel;

    @FXML
    Label valueLabel;

    @Override
    public void setUIValues(LetterTile tile)
    {
        letterLabel.setText(tile.getLetter().toString());
        valueLabel.setText(tile.getValue().toString());
    }
}
