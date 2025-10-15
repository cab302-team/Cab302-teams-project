package com.example.project.controllers.gameScreens;

import com.example.project.controllers.TileGroups.UpgradeTileGroupController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.gameScreens.ShopModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class ShopControllerTests {

    private ShopController controller;
    private ShopModel mockShopModel;

    @BeforeEach
    void setUp() {
        mockShopModel = mock(ShopModel.class);
        controller = new ShopController(mockShopModel, mock(UpgradeTileGroupController.class), mock(UpgradeTileGroupController.class)); // Use injected constructor
    }

    @Test
    void onSceneChangedToThis_ShouldRegenerateShopItems()
    {
        var playersGroupMock =  mock(UpgradeTileGroupController.class);
        var shopTileGroupMock =  mock(UpgradeTileGroupController.class);
        var shopController = new ShopController(mockShopModel, playersGroupMock, shopTileGroupMock);
        shopController.onSceneChangedToThis();
        verify(mockShopModel, times(1)).regenerateShopItems();
        verify(playersGroupMock, times(1)).syncTiles();
        verify(shopTileGroupMock, times(1)).syncTiles();
    }

    @Test
    void onNextLevelPressed_ShouldCallShopModel() {
        controller.onNextLevelPressed();
        verify(mockShopModel, times(1)).onNextLevelPressed();
    }

    @Test
    void onUpgradeClicked_ShouldPurchase_WhenCanPurchaseTrue() {
        var mockUpgrade = mock(UpgradeTileController.class);
        when(mockShopModel.canPurchase(mockUpgrade.getModel())).thenReturn(true);

        controller.onUpgradeClicked(mockUpgrade);

        verify(mockShopModel, times(1)).purchase(mockUpgrade.getModel());
    }

    @Test
    void onUpgradeClicked_ShouldNotPurchase_WhenCanPurchaseFalse() {
        var mockUpgrade = mock(UpgradeTileController.class);
        when(mockShopModel.canPurchase(mockUpgrade.getModel())).thenReturn(false);

        controller.onUpgradeClicked(mockUpgrade);

        verify(mockShopModel, never()).purchase(any());
    }

}