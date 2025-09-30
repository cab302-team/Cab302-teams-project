package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.ShopModel;
import com.example.project.models.tiles.UpgradeTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class ShopControllerTests {

    private ShopController controller;
    private ShopModel mockShopModel;

    @BeforeEach
    void setUp() throws Exception {
        controller = new ShopController();

        // mocking the ShopModel
        mockShopModel = mock(ShopModel.class);
        var shopModelField = ShopController.class.getDeclaredField("shopModel");
        shopModelField.setAccessible(true);
        shopModelField.set(controller, mockShopModel);
    }

    @Test
    void onSceneChangedToThis_ShouldRegenerateShopItems() {
        controller.onSceneChangedToThis();
        verify(mockShopModel, times(1)).regenerateShopItems();
    }

    @Test
    void onNextLevelPressed_ShouldCallShopModel() throws Exception {
        var method = ShopController.class.getDeclaredMethod("onNextLevelPressed");
        method.setAccessible(true);
        method.invoke(controller);

        verify(mockShopModel, times(1)).onNextLevelPressed();
    }

    @Test
    void onUpgradeClicked_ShouldPurchase_WhenCanPurchaseTrue() throws Exception {
        var mockUpgrade = mock(UpgradeTile.class);
        when(mockShopModel.canPurchase(mockUpgrade)).thenReturn(true);

        var method = ShopController.class.getDeclaredMethod("onUpgradeClicked", UpgradeTile.class);
        method.setAccessible(true);
        method.invoke(controller, mockUpgrade);

        verify(mockShopModel, times(1)).purchase(mockUpgrade);
    }

    @Test
    void onUpgradeClicked_ShouldNotPurchase_WhenCanPurchaseFalse() throws Exception {
        var mockUpgrade = mock(UpgradeTile.class);
        when(mockShopModel.canPurchase(mockUpgrade)).thenReturn(false);

        var method = ShopController.class.getDeclaredMethod("onUpgradeClicked", UpgradeTile.class);
        method.setAccessible(true);
        method.invoke(controller, mockUpgrade);

        verify(mockShopModel, never()).purchase(any());
    }

}