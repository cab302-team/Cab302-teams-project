package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.ShopModel;
import com.example.project.models.tiles.UpgradeTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class ShopControllerTests {

    private ShopController controller;
    private ShopModel mockShopModel;

    @BeforeEach
    void setUp() {
        mockShopModel = mock(ShopModel.class);
        controller = new ShopController(mockShopModel); // Use injected constructor
    }

    @Test
    void onSceneChangedToThis_ShouldRegenerateShopItems() {
        controller.onSceneChangedToThis();
        verify(mockShopModel, times(1)).regenerateShopItems();
    }

    @Test
    void onNextLevelPressed_ShouldCallShopModel() {
        controller.onNextLevelPressed();
        verify(mockShopModel, times(1)).onNextLevelPressed();
    }

    @Test
    void onUpgradeClicked_ShouldPurchase_WhenCanPurchaseTrue() {
        var mockUpgrade = mock(UpgradeTile.class);
        when(mockShopModel.canPurchase(mockUpgrade)).thenReturn(true);

        try {
            var method = ShopController.class.getDeclaredMethod("onUpgradeClicked", UpgradeTile.class);
            method.setAccessible(true);
            method.invoke(controller, mockUpgrade);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Unexpected exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        verify(mockShopModel, times(1)).purchase(mockUpgrade);
    }

    @Test
    void onUpgradeClicked_ShouldNotPurchase_WhenCanPurchaseFalse() {
        var mockUpgrade = mock(UpgradeTile.class);
        when(mockShopModel.canPurchase(mockUpgrade)).thenReturn(false);

        try {
            var method = ShopController.class.getDeclaredMethod("onUpgradeClicked", UpgradeTile.class);
            method.setAccessible(true);
            method.invoke(controller, mockUpgrade);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Unexpected exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        verify(mockShopModel, never()).purchase(any());
    }
}