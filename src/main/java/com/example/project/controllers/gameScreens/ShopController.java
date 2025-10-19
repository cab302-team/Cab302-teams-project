package com.example.project.controllers.gameScreens;

import com.example.project.controllers.TileGroups.UpgradeTileGroupController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
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

    private final ShopModel shopModel;
    private UpgradeTileGroupController playersUpgradesTiles;
    private UpgradeTileGroupController shopItemsGroupTiles;
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
    protected ShopController(ShopModel model, UpgradeTileGroupController players, UpgradeTileGroupController shopItems) {
        super();
        this.shopModel = model;
        this.playersUpgradesTiles = players;
        this.shopItemsGroupTiles = shopItems;
    }

    /**
     * FXML initialise function called once when the .fxml is loaded on application launch.
     */
    @FXML
    public void initialize()
    {
        playersUpgradesTiles = new UpgradeTileGroupController(playersUpgradesContainer, shopModel.playersUpgradesProperty());
        shopItemsGroupTiles = new UpgradeTileGroupController(shopItemsContainer, shopModel.currentShopItemsProperty(),
                this::onUpgradeClicked);

        var loadedSidebar = this.loadSidebar();
        var sidebarNode = ((StackPane) loadedSidebar.node());
        this.sidebar.getChildren().add(sidebarNode);
        this.sidebarController = loadedSidebar.controller();
        sidebarController.bindPersistentInfo();
        sidebarController.onlyShopShopInfo();
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
        shopModel.regenerateShopItems();
        playersUpgradesTiles.syncTiles();
        // TODO: if merge breaks need to add syncTiles for both here.
        Session.getInstance().resetPlaysRedraws();
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
}
