package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tiles.UpgradeTileController;
import com.example.project.models.gameScreens.ShopModel;
import com.example.project.models.tileGroups.UpgradeTileGroup;
import com.example.project.services.Logger;
import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(MockAudioSystemExtension.class)
class ShopControllerTests {

    private ShopController controller;
    private ShopModel mockShopModel;

    private final ByteArrayOutputStream stdOutputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errOutputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        mockShopModel = mock(ShopModel.class);
        var logger = new Logger(errOutputStream, stdOutputStream);
        controller = new ShopController(mockShopModel, mock(UpgradeTileGroup.class), mock(UpgradeTileGroup.class), logger);
    }

    @Test
    void onSceneChangedToThis_ShouldRegenerateShopItems()
    {
        var playersGroupMock =  mock(UpgradeTileGroup.class);
        var shopTileGroupMock =  mock(UpgradeTileGroup.class);
        var logger = new Logger(errOutputStream, stdOutputStream);
        var shopController = new ShopController(mockShopModel, playersGroupMock, shopTileGroupMock, logger);
        shopController.onSceneChangedToThis();
        verify(mockShopModel, times(1)).regenerateShopItems();
    }

    @Test
    void onNextLevelPressed_ShouldCallShopModel() {
        controller.onNextLevelPressed();
        verify(mockShopModel, times(1)).onNextLevelPressed();
    }

    @Test
    void onUpgradeClicked()
    {
        var mockUpgrade = mock(UpgradeTileController.class);
        controller.onUpgradeClicked(mockUpgrade);
        verify(mockShopModel, times(1)).tryPurchase(any());
        verify(mockUpgrade, times(1)).getModel();
    }
}