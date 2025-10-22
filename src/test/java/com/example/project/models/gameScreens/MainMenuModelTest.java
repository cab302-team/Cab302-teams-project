package com.example.project.models.gameScreens;

import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MainMenuModelTest {

    @Test
    void onStartClicked() {
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);

        var mainMenuModel = new MainMenuModel(mockSession, mockSceneManager);
        mainMenuModel.onStartClicked();

        verify(mockSceneManager).switchScene(GameScenes.LEVEL);
    }

    @Test
    void onLogoutClicked() {
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);

        var mainMenuModel = new MainMenuModel(mockSession, mockSceneManager);

        mainMenuModel.onLogoutClicked();

        verify(mockSceneManager).switchScene(GameScenes.LOGIN);
    }
}