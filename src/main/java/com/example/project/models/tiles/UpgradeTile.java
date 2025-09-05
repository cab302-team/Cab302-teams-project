package com.example.project.models.tiles;

import javafx.scene.image.Image;

/**
 * Represents the upgrade tiles sold at the shop.
 */
public class UpgradeTile extends Tile
{
    // TODO: the upgrade tile stuff like description its FXML (see Letter Tile).

    @Override
    public String getFXMLFile()
    {
        return "/com/example/project/SingleTiles/upgradeTileView.fxml";
    }

    /**
     * @return returns loaded Image for this tile.
     */
    public Image getAbilityImage()
    {
        return null;
    }
}
