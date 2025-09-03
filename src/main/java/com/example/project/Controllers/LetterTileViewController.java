package com.example.project.Controllers;

import com.example.project.Tiles.LetterTile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Controls the layout of the letterUIModel. Which is a letter tile.
 */
public class LetterTileViewController
{
    @FXML
    Rectangle theBackground = new Rectangle(50, 50, Color.GREEN);

    @FXML
    Label letterLabel;

    @FXML
    Label valueLabel;

    public void setUIValues(LetterTile tile)
    {
        letterLabel.setText(tile.getLetter().toString());
        valueLabel.setText(tile.getValue().toString());
    }
}
