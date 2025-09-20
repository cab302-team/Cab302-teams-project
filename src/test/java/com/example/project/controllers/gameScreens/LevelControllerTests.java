package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Tests for level controller.
 */
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

    @Mock
    private Button playButton;

    @InjectMocks
    private LevelController controller;

    /**
     * asf
     */
    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this); // manual init
    }

    // onPLay button tests.
    // won after, lost after, neither won or lost after.
    @Test
    void testOnPlayButton_ThenWon()
    {
        var sceneManagerMock = mock(SceneManager.class);
        var sceneManager = new SceneManager(sceneManagerMock);

        var modelMock = mock(LevelModel.class);
//        controller = new LevelController(modelMock);

        controller.onPlayButton();
    }
}