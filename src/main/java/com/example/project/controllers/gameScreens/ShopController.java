package com.example.project.controllers.gameScreens;

import com.example.project.models.tileGroups.UpgradeTileGroup;
import com.example.project.controllers.tiles.UpgradeTileController;
import com.example.project.models.gameScreens.ShopModel;
import com.example.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Shop view controller.
 */
public class ShopController extends GameScreenController
{
    @FXML private Button rerollButton;
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

        playersUpgrades = new UpgradeTileGroup(playersUpgradesContainer, shopModel.getSession().getPlayersUpgradesProperty());
        shopItemsGroup = new UpgradeTileGroup(shopItemsContainer, shopModel.getCurrentShopItemsProperty(),
                this::onUpgradeClicked);

        var loadedSidebar = this.loadSidebar();
        var sidebarNode = ((StackPane) loadedSidebar.node());
        this.sidebar.getChildren().add(sidebarNode);
        SidebarController sidebarController = loadedSidebar.controller();
        sidebarController.bindPersistentInfo(session);
        sidebarController.hideLevelInfo();

        this.shopModel.getRerollCostProperty().addListener((obs, oldStr, newStr) -> syncReroll());
        this.shopModel.getSession().getMoneyProperty().addListener((obs, oldStr, newStr) -> syncReroll());
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Scene changed to shop");
        shopModel.regenerateShopItems();
        shopModel.getSession().resetPlaysRedraws();
        playersUpgrades.syncTiles();
        shopItemsGroup.syncTiles();
        syncReroll();
    }

    private void syncReroll()
    {
        var rerollCost = this.shopModel.getRerollCostProperty().get();
        rerollButton.setDisable(rerollCost > this.shopModel.getSession().getMoneyProperty().get());
        rerollButton.setText(String.format("Reroll ($%d)", rerollCost));
    }

    /**
     *
     * @param controller upgrade tile ui element clicked on.
     */
    protected void onUpgradeClicked(UpgradeTileController controller)
    {
        var model = controller.getModel();
        shopModel.tryPurchase(model);
    }

    @FXML
    protected void onNextLevelPressed()
    {
        this.shopModel.onNextLevelPressed();
    }

    @FXML
    protected void onRerollPressed()
    {
        this.shopModel.reroll();
    }

    @FXML
    protected void save()
    {
        this.shopModel.getSession().Save();
    }
}
