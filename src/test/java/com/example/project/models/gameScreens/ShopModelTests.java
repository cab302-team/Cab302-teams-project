package com.example.project.models.gameScreens;

import com.example.project.services.GameScenes;
import com.example.project.services.Logger;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ShopModelTests
{
    private final ByteArrayOutputStream ErrOutputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream StdOutputStream = new ByteArrayOutputStream();
    private final Logger logger = new Logger(ErrOutputStream, StdOutputStream);

    // TODO: add tests after shop model complete
    @Test
    void onNextLevelPressed_Test()
    {
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);
        var shopModel = new ShopModel(mockSession, logger);
        var sceneManager = new SceneManager(mockSceneManager);

        shopModel.onNextLevelPressed();
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