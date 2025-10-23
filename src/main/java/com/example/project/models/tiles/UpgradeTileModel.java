package com.example.project.models.tiles;

import com.example.project.models.gameScreens.LevelModel;

import java.util.function.Consumer;

/**
 * Represents the upgrade tiles sold at the shop.
 */
public class UpgradeTileModel extends TileModel
{
    private final String description;
    private final String name;
    private final String imagePath;
    private final double cost;
    private final Consumer<LevelModel> upgradeEffect;

    /**
     * public upgrade constructor.
     * @param builder upgrade builder
     */
    public UpgradeTileModel(UpgradeBuilder builder)
    {
        this.description = builder.description;
        this.name = builder.name;
        this.imagePath = builder.imagePath;
        this.cost = builder.cost;
        this.upgradeEffect = builder.upgradeEffect;
    }

    /**
     * builder class for the upgrades
     */
    public static class UpgradeBuilder {
        private String description;
        private String name;
        private String imagePath;
        private double cost;
        private Consumer<LevelModel> upgradeEffect;

        /**
         * upgrade description builder
         * @param description new upgrade description
         * @return description
         */
        public UpgradeBuilder description(String description) {this.description = description; return this; }

        /**
         * upgrade name builder
         * @param name new upgrade name
         * @return name
         */
        public UpgradeBuilder name(String name) {this.name = name; return this; }

        /**
         * upgrade image path builder
         * @param imagePath new upgrade image path
         * @return image path
         */
        public UpgradeBuilder imagePath(String imagePath) {this.imagePath = imagePath; return this; }

        /**
         * upgrade cost builder
         * @param cost new upgrade cost
         * @return cost
         */
        public UpgradeBuilder cost(double cost) {this.cost = cost; return this; }

        /**
         * upgrade effect builder
         * @param upgradeEffect new upgrade effect
         * @return upgrade effect
         */
        public UpgradeBuilder upgradeEffect(Consumer<LevelModel> upgradeEffect) { this.upgradeEffect = upgradeEffect; return this; }

        /**
         * build upgrade
         * @return new upgrade
         */
        public UpgradeTileModel build() { return new UpgradeTileModel(this); }
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
     * gets upgrade tile image path.
     * @return returns loaded Image for this tile.
     */
    public String getAbilityImagePath() { return imagePath; }

    /**
     * gets the effect for this tile.
     * @param model level model.
     */
    public void runUpgradeEffect(LevelModel model)
    {
        upgradeEffect.accept(model);
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/upgradeTileView.fxml";
    }
}
