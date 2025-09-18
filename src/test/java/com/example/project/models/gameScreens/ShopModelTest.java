package com.example.project.models.gameScreens;

import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ShopModelTest {

    // TODO: add tests after shop model complete

    @Test
    void onNextLevelPressed_Test() {
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);
        var shopModel = new ShopModel(mockSession);
        var sceneManager = new SceneManager(mockSceneManager);

        shopModel.onNextLevelPressed();
        verify(mockSession).updateLevelInfo();
        verify(mockSceneManager).switchScene(GameScenes.LEVEL);
    }

    @Test
    void regenerateShopItems() {
    }

    @Test
    void playersUpgradesProperty() {
    }

    @Test
    void purchase() {

    }

    @Test
    void canPurchase() {
    }

}