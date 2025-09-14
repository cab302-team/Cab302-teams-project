package com.example.project.controllers.gameScreens;

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
    private HBox shopItemsContainer;

    @FXML
    private HBox playersUpgradesContainer;

    private UpgradeTileGroup playersUpgrades;
    private UpgradeTileGroup shopItemsGroup;

    /**
     * no arg constructor.
     */
    public ShopController()
    {
        super();
        this.shopModel = new ShopModel(Session.getInstance());
    }

    /**
     * This runs after the constructor and after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    public void initialize()
    {
        playersUpgrades = new UpgradeTileGroup(playersUpgradesContainer, shopModel.playersUpgradesProperty());
        shopItemsGroup = new UpgradeTileGroup(shopItemsContainer, shopModel.currentShopItemsProperty(), this::onUpgradeClicked);
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
        shopModel.regenerateShopItems();
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
