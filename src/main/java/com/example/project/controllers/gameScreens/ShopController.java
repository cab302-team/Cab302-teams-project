package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.ShopModel;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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

    @FXML private StackPane sidebar;

    private UpgradeTileGroup playersUpgrades;
    private UpgradeTileGroup shopItemsGroup;
    private SidebarController sidebarController;


    /**
     * no arg constructor.
     */
    public ShopController()
    {
        super();
        this.shopModel = new ShopModel(Session.getInstance());
    }

    /**
     * protected constructor for unit testing with mock model injection.
     */
    protected ShopController(ShopModel model) {
        super();
        this.shopModel = model;
    }

    /**
     * FXML initialise function called once when the .fxml is loaded on application launch.
     */
    @FXML
    public void initialize()
    {
        playersUpgrades = new UpgradeTileGroup(playersUpgradesContainer, shopModel.playersUpgradesProperty());
        shopItemsGroup = new UpgradeTileGroup(shopItemsContainer, shopModel.currentShopItemsProperty(),
                this::onUpgradeClicked);

        // TODO sidebar?
        var loadedSidebar = this.loadSidebar();
        var sidebarNode = ((StackPane) loadedSidebar.node());
        this.sidebar.getChildren().add(sidebarNode);
        this.sidebarController = loadedSidebar.controller();

        // Shop binding. TODO bind draws / plays.
        sidebarController.bindMoney();
        // TODO: bind plays and redraws tho to session to be bound in sidebar.
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
        shopModel.regenerateShopItems();

//        this.sidebarController.sync();
//        sidebarController.setupProperties(levelModel);
        // TODO: sidebar for shop model instead diff function to hide level stuff.
    }

    /**
     *
     * @param model the upgrade tile model that was clicked by the player
     */
    protected void onUpgradeClicked(UpgradeTile model)
    {
        if (shopModel.canPurchase(model))
        {
            shopModel.purchase(model);
        }
    }

    @FXML
    protected void onNextLevelPressed()
    {
        this.shopModel.onNextLevelPressed();
    }
}
