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
public class ShopController extends GameScreenController
{
    private final ShopModel shopModel;

    @FXML
    private HBox shopItemsRow;

    /**
     * no arg constructor.
     */
    public ShopController()
    {
        this.shopModel = new ShopModel(Session.getInstance());
    }

    /**
     * Constructor with injection for tests.
     * @param logger logger to use.
     */
    public ShopController(Logger logger)
    {
        super(logger);
        this.shopModel = new ShopModel(Session.getInstance());
    }

    /**
     * This runs after the constructor and after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    public void initialize()
    {
        // hook into observable properties
        shopModel.currentShopItemsProperty().addListener((obs, oldVal, newVal) -> syncShopItems());
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");

        shopModel.regenerateShopItems();
        syncShopItems();
    }

    /**
     * Sync shop items in UI to models shop items.
     */
    public void syncShopItems()
    {
        shopItemsRow.getChildren().clear();
        for (UpgradeTile model : shopModel.getUpgradeCards()){
            UpgradeTileViewController controller = TileLoader.createUpgradeTile(model);
            controller.getRoot().setOnMouseClicked(e -> onUpgradeClicked(model));
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
    private void onNextLevelPressed()
    {
        this.shopModel.onNextLevelPressed();
    }
}
