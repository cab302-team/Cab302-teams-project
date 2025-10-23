package com.example.project.models.gameScreens;

import com.example.project.services.GameScenes;
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
        var shopModel = new ShopModel(mockSession, logger);
        // to inject mock
        @SuppressWarnings("unused") var sceneManager = new SceneManager(mockSceneManager);

        shopModel.onNextLevelPressed();
        verify(mockSceneManager).switchScene(GameScenes.LEVEL);
    }

    @Test
    void regenerateShopItems()
    {
        var mockSession = mock(Session.class);
        var shop = new ShopModel(mockSession);

        try (MockedStatic<UpgradeTiles> mockedUpgradeTiles = Mockito.mockStatic(UpgradeTiles.class))
        {
            shop.regenerateShopItems();
            mockedUpgradeTiles.verify(UpgradeTiles::getRandomUpgradeTile, times(shop.numberOfShopItems));
        }
    }

    @Test
    void purchase_throws(){
        var mockSession = mock(Session.class);
        var shop = new ShopModel(mockSession);

        assertThrows(IllegalArgumentException.class, () -> shop.tryPurchase(null));
    }

    @Test
    void purchase_canAfford()
    {
        // TODO: is fixed in the save / load functionality branch.
    }

    @Test
    void purchase_cantAfford(){
        var mockSession = mock(Session.class);
        var money = new ReadOnlyDoubleWrapper(0);
        when(mockSession.getMoneyProperty()).thenReturn(money);
        var shop = new ShopModel(mockSession, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));
        shop.regenerateShopItems();

        var tileToBuy = shop.getCurrentShopItemsProperty().get().getFirst();

        shop.tryPurchase(tileToBuy);
        // assert tile not removed
        assertTrue(shop.getCurrentShopItemsProperty().get().contains(tileToBuy));
    }
}