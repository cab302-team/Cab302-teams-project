package com.example.project.models.tiles;

import javafx.scene.image.Image;

/**
 * Represents the upgrade tiles sold at the shop.
 */
public class UpgradeTile extends Tile
{
    private final String description;
    private final String name;
    private final String imagePath;

    public UpgradeTile(String name, String description, String imagePath)
    {
        this.description = description;
        this.name = name;
        this.imagePath = imagePath;
    }

    /**
     * @return returns loaded Image for this tile.
     */
    public String getAbilityImagePath()
    {
        return imagePath; // "/com/example/project/upgradeTileImages/Monk_29.png";
    }
}
