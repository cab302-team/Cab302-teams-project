package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.Tile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Controls the layout of the letterUIModel. Which is a letter tile.
 */
public class LetterTileViewController extends TileController<LetterTile>
{
    @FXML
    private StackPane root;

    @FXML
    Rectangle theBackground = new Rectangle(Tile.TILE_SIZE, Tile.TILE_SIZE);

    @FXML
    Label letterLabel;

    @FXML
    Label valueLabel;

    private LetterTile model;

    public Node getRoot()
    {
        return root;
    }

    public LetterTile getModel(){
        return model;
    }

    @Override
    public void bind(LetterTile model)
    {
        this.model = model;
        letterLabel.setText(String.valueOf(model.getLetter()));
        valueLabel.setText(String.valueOf(model.getValue()));
    }
}
