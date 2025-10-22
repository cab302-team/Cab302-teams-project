package com.example.project.controllers.gameScreens;

import com.example.project.models.tileGroups.UpgradeTileGroup;
import com.example.project.controllers.tiles.UpgradeTileController;
import com.example.project.models.gameScreens.ShopModel;
import com.example.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Shop view controller.
 */
public class ShopController extends GameScreenController
{
    @FXML private Pane root;
    @FXML private HBox shopItemsContainer;
    @FXML private HBox playersUpgradesContainer;
    @FXML private StackPane sidebar;

    private ShopModel shopModel;
    private UpgradeTileGroup playersUpgrades;
    private UpgradeTileGroup shopItemsGroup;

    /**
     * no arg constructor.
     */
    public ShopController() { }

    /**
     * protected constructor for unit testing with mock model injection.
     */
    protected ShopController(ShopModel model, UpgradeTileGroup players, UpgradeTileGroup shopItems, Logger logger) {
        super();
        this.logger = logger;
        this.shopModel = model;
        this.playersUpgrades = players;
        this.shopItemsGroup = shopItems;
    }

    @Override
    public void setup(Session session, SceneManager sceneManager)
    {
        this.shopModel = new ShopModel(session, sceneManager);

        playersUpgrades = new UpgradeTileGroup(playersUpgradesContainer, shopModel.playersUpgradesProperty());
        shopItemsGroup = new UpgradeTileGroup(shopItemsContainer, shopModel.currentShopItemsProperty(),
                this::onUpgradeClicked);

        var loadedSidebar = this.loadSidebar();
        var sidebarNode = ((StackPane) loadedSidebar.node());
        this.sidebar.getChildren().add(sidebarNode);
        SidebarController sidebarController = loadedSidebar.controller();
        sidebarController.bindPersistentInfo(session);
        sidebarController.hideLevelInfo();
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
        shopModel.regenerateShopItems();
        shopModel.getSession().resetPlaysRedraws();
        playersUpgrades.syncTiles();
    }

    /**
     *
     * @param controller upgrade tile ui element clicked on.
     */
    protected void onUpgradeClicked(UpgradeTileController controller)
    {
        var model = controller.getModel();
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

    @FXML
    protected void save()
    {
        this.shopModel.getSession().Save();
    }
}
