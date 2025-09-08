package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.gameScreens.ShopModel;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

/**
 * Shop view controller.
 */
public class ShopViewController extends GameScreenController implements ModelObserver
{
    private final ShopModel shopModel;

    @FXML
    private HBox shopItemsRow;

    /**
     * no arg constructor.
     */
    public ShopViewController()
    {
        this.shopModel = new ShopModel(Session.getInstance(), this);
    }

    /**
     * Constructor with injection for tests.
     * @param logger logger to use.
     */
    public ShopViewController(Logger logger)
    {
        super(logger);
        this.shopModel = new ShopModel(Session.getInstance(), this);
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");

        shopModel.regenerateShopItems();
        onModelChanged(); // initial setup.
    }

    @Override
    public void onModelChanged() {
        logger.logMessage("TODO shop view changes things.");
        shopItemsRow.getChildren().clear();

        for (UpgradeTile model : shopModel.getUpgradeCards()){
            UpgradeTileViewController controller = TileLoader.createUpgradeTile(model);
            controller.getRoot().setOnMouseClicked(e -> {
                onUpgradeClicked(model);
            });
            shopItemsRow.getChildren().add((controller.getRoot()));
        }
    }

    private void onUpgradeClicked(UpgradeTile model)
    {
        if (shopModel.canPurchase(model))
        {
            shopModel.purchase(model);
        }
    }

    @FXML
    private void onNextLevelPressed(){
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }
}
