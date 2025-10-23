package com.example.project.models.gameScreens;

import com.example.project.services.GameScene;
import com.example.project.services.Logger;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.shopItems.UpgradeTiles;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockAudioSystemExtension.class)
class ShopModelTests
{
    private final ByteArrayOutputStream ErrOutputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream StdOutputStream = new ByteArrayOutputStream();
    private final Logger logger = new Logger(ErrOutputStream, StdOutputStream);

    @Test
    void onNextLevelPressed_Test()
    {
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);
        var shopModel = new ShopModel(mockSession, mockSceneManager, logger);

        shopModel.onNextLevelPressed();
        verify(mockSceneManager).switchScene(GameScene.LEVEL);
    }

    @Test
    void regenerateShopItems()
    {
        var mockSession = mock(Session.class);
        var shop = new ShopModel(mockSession, mock(SceneManager.class));

        try (MockedStatic<UpgradeTiles> mockedUpgradeTiles = Mockito.mockStatic(UpgradeTiles.class))
        {
            shop.regenerateShopItems();
            mockedUpgradeTiles.verify(UpgradeTiles::getRandomUpgradeTile, times(shop.numberOfShopItems));
        }
    }

    @Test
    void reroll(){
        var mockSession = mock(Session.class);
        var shop = new ShopModel(mockSession, mock(SceneManager.class));

        assertEquals(3, shop.getRerollCostProperty().get());

        try (MockedStatic<UpgradeTiles> mockedUpgradeTiles = Mockito.mockStatic(UpgradeTiles.class))
        {
            shop.reroll();
            mockedUpgradeTiles.verify(UpgradeTiles::getRandomUpgradeTile, times(shop.numberOfShopItems));
        }

        assertEquals(6, shop.getRerollCostProperty().get());
    }

    @Test
    void purchase_throws(){
        var mockSession = mock(Session.class);
        var shop = new ShopModel(mockSession, mock(SceneManager.class));

        assertThrows(IllegalArgumentException.class, () -> shop.tryPurchase(null));
    }

    @Test
    void purchase_canAfford()
    {
        var mockSession = mock(Session.class);
        var money = new ReadOnlyDoubleWrapper(1000);
        when(mockSession.getMoneyProperty()).thenReturn(money);

        var capturedOutStream = new ByteArrayOutputStream();
        var shop = new ShopModel(mockSession, mock(SceneManager.class), new Logger(new ByteArrayOutputStream(), capturedOutStream));

        shop.regenerateShopItems();
        var tileToBuy = shop.getCurrentShopItemsProperty().get().getFirst();

        var count = shop.getCurrentShopItemsProperty().get().size();

        shop.tryPurchase(tileToBuy);
        verify(mockSession, times(1)).addUpgrade(tileToBuy);
        verify(mockSession, times(1)).modifyMoney(tileToBuy.getCost());
        assertEquals((String.format("Purchased %s for $%.2f%n", tileToBuy.getName(), tileToBuy.getCost())), capturedOutStream.toString());
        assertEquals(count - 1, shop.getCurrentShopItemsProperty().get().size());
    }

    @Test
    void purchase_cantAfford(){
        var mockSession = mock(Session.class);
        var money = new ReadOnlyDoubleWrapper(0);
        when(mockSession.getMoneyProperty()).thenReturn(money);
        var shop = new ShopModel(mockSession, mock(SceneManager.class), new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));
        shop.regenerateShopItems();

        var tileToBuy = shop.getCurrentShopItemsProperty().get().getFirst();

        shop.tryPurchase(tileToBuy);
        // assert tile not removed
        assertTrue(shop.getCurrentShopItemsProperty().get().contains(tileToBuy));
    }
}