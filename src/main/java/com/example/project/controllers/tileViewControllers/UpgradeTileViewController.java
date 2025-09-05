package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Controlls the upgrade tile view fxml file.
 */
public class UpgradeTileViewController extends TileController<UpgradeTile>
{
    @FXML
    private StackPane root;

    @FXML
    Rectangle theBackground = new Rectangle(Tile.TILE_SIZE, Tile.TILE_SIZE);

    @FXML
    private Image abilityImage;

    private UpgradeTile model;

    public Node getRoot()
    {
        return root;
    }

    public void bind(UpgradeTile tileModel)
    {
        abilityImage = new Image(getClass().getResource(tileModel.getAbilityImagePath()).toExternalForm());
        model = tileModel;
    }
}
