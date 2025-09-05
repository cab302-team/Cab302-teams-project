package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * Controlls the upgrade tile view fxml file.
 */
public class UpgradeTileViewController extends TileController<UpgradeTile>
{
    @FXML
    Rectangle theBackground = new Rectangle(Tile.TILE_SIZE, Tile.TILE_SIZE);

    @FXML
    private Image abilityImage;

    @Override
    public void setUIValues(UpgradeTile tile)
    {
        this.abilityImage = tile.getAbilityImage();
    }
}
