package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;


/**
 * Tests for level controller.
 */
@ExtendWith(MockAudioSystemExtension.class)
public class LevelControllerTests
{

    @Test
    void testOnSkipButton_switchesToShop()
    {
        var sceneManagerMock = mock(SceneManager.class);
        var sceneManager = new SceneManager(sceneManagerMock);

        var modelMock = mock(LevelModel.class);
        var levelController = new LevelController(modelMock);

        levelController.onSkipButton();

        verify(sceneManagerMock).switchScene(GameScenes.SHOP);
    }

    @Test
    void testOnRedrawButton_togglesRedrawActive()
    {
        var sceneManagerMock = mock(SceneManager.class);
        var sceneManager = new SceneManager(sceneManagerMock);

        var modelMock = mock(LevelModel.class);
        var levelController = new LevelController(modelMock);

        when(modelMock.getIsRedrawActive()).thenReturn(new javafx.beans.property.SimpleBooleanProperty(false));

        levelController.onRedrawButton();

        verify(modelMock).setIsRedrawActive(true);
        verify(modelMock).returnRedrawTilesToTheRack();
    }

    @Test
    void testOnConfirmRedrawButton_redrawsTiles()
    {
        var sceneManagerMock = mock(SceneManager.class);
        var sceneManager = new SceneManager(sceneManagerMock);

        var modelMock = mock(LevelModel.class);
        var levelController = new LevelController(modelMock);

        when(modelMock.getIsRedrawActive()).thenReturn(new javafx.beans.property.SimpleBooleanProperty(false));

        levelController.onConfirmRedrawButton();

        verify(modelMock).setIsRedrawActive(true);
        verify(modelMock).redrawTiles();
    }
}