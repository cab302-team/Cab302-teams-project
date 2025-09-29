package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.ShopModel;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Shop view controller.
 */
public class ShopController extends GameScreenController
{
    private final ShopModel shopModel;

    @FXML
    private HBox shopItemsContainer;

    @FXML
    private Pane root;

    @FXML
    private HBox playersUpgradesContainer;

    // The label for displaying the player's current amount of money
    @FXML
    private Label moneyLabel;

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
     * FXML initialise function called once when the .fxml is loaded on application launch.
     */
    @FXML
    public void initialize()
    {
        //binds the money display to automatically update when the players money changes
        moneyLabel.textProperty().bind(
                Session.getInstance().getMoneyProperty().asString("Money: $%d")
        );

        playersUpgrades = new UpgradeTileGroup(playersUpgradesContainer, shopModel.playersUpgradesProperty());
        shopItemsGroup = new UpgradeTileGroup(shopItemsContainer, shopModel.currentShopItemsProperty(),
                this::onUpgradeClicked);
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
        shopModel.regenerateShopItems();
    }

    /**
     *
     * @param model the upgrade tile model that was clicked by the player
     */
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
