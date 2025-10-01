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

    /**
     * public constructor.
     * @param name name.
     * @param description description
     * @param imagePath image path.
     */
    public UpgradeTile(String name, String description, String imagePath)
    {
        this.description = description;
        this.name = name;
        this.imagePath = imagePath;
        this.cost = 2;
    }

    /**
     * gets the name.
     * @return return name.
     */
    public String getName(){
        return this.name;
    }

    /**
     * gets the description.
     * @return description.
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * gets the cost
     * @return return cost.
     */
    public double getCost()
    {
        return this.cost;
    }

    /**
     * gets ability image path.
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
