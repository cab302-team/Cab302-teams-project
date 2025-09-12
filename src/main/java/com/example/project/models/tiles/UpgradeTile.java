package com.example.project.models.tiles;

/**
 * Represents the upgrade tiles sold at the shop.
 */
public class UpgradeTile extends Tile
{
    private final String description;
    private final String name;
    private final String imagePath;
    private final double cost;

    public UpgradeTile(String name, String description, String imagePath)
    {
        this.description = description;
        this.name = name;
        this.imagePath = imagePath;
        this.cost = 2;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public double getCost()
    {
        return this.cost;
    }

    /**
     * @return returns loaded Image for this tile.
     */
    public String getAbilityImagePath()
    {
        return imagePath; // "/com/example/project/upgradeTileImages/Monk_29.png";
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/upgradeTileView.fxml";
    }
}
