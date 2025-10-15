package com.example.project.services;

import com.example.project.controllers.RootLayoutController;
import com.example.project.controllers.gameScreens.GameScreenController;
import com.example.project.controllers.gameScreens.LevelController;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Scene Manager Tests. Will test preload page so will fail is FXML paths are not correct.
 */
@ExtendWith(MockAudioSystemExtension.class)
public class SceneManagerTests
{
    @BeforeEach
    void beforeEach()
    {
        var sceneManager = new SceneManager(null, null, new HashMap<>(), new HashMap<>());
    }

    @Test
    void intitialise_FirstTimeSuccess()
    {
        RootLayoutController controller = new RootLayoutController();
        var sceneManager = SceneManager.getInstance();
        Map<GameScenes, GameScreenController> controllerMap = new HashMap<>();
        Map<GameScenes, Parent> pagesMap = new HashMap<>();

        sceneManager = new SceneManager(null, null, controllerMap, pagesMap);

        var mockLoader = mock(PageLoader.class);

        try{
            when(mockLoader.load(anyString())).thenReturn(new StackPane());
        }
        catch (IOException e){
            Assertions.fail();
        }

        when(mockLoader.getController()).thenReturn(mock(GameScreenController.class));
        sceneManager.initialise(controller, mockLoader);

        // assert pages has 3 entries, assert controllers have 3 entries.
        assertEquals(4, controllerMap.size());
        assertEquals(4, pagesMap.size());
    }

    @Test
    void initialise_ThrowsIfCalledTwice()
    {
        RootLayoutController rootController = new RootLayoutController();
        Map<GameScenes, GameScreenController> controllerMap = new HashMap<>();
        Map<GameScenes, Parent> pagesMap = new HashMap<>();
        var sceneManager = new SceneManager(null, null, controllerMap, pagesMap);
        var mockLoader = mock(PageLoader.class);

        try{
            when(mockLoader.load(anyString())).thenReturn(new StackPane());
        }
        catch (IOException e){
            Assertions.fail();
        }

        when(mockLoader.getController()).thenReturn(mock(GameScreenController.class));
        sceneManager.initialise(rootController, mockLoader);

        // Correct - passes executable that assertThrows will call
        assertThrows(RuntimeException.class, () ->
                sceneManager.initialise(rootController, new FXMLPageLoader())
        );
    }

    @Test
    void switchScenes_SwitchToLevel()
    {
        RootLayoutController mockRootLayoutController = mock(RootLayoutController.class);

        // Mock controllere.setContent response do nothing
        doNothing().when(mockRootLayoutController).setContent(any(Parent.class));

        // initialise with mock controller
        LevelController mockLevelController = mock(LevelController.class);
        doNothing().when(mockLevelController).onSceneChangedToThis();

        Map<GameScenes, GameScreenController> mockControllers = new HashMap<>();
        mockControllers.put(GameScenes.LEVEL, mockLevelController);

        Map<GameScenes, Parent> mockPages = new HashMap<>();
        Parent page = new Pane();
        mockPages.put(GameScenes.LEVEL, page);

        var sceneManager = new SceneManager(null, mockRootLayoutController, mockControllers, mockPages);

        // test method
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);

        // assert
        verify(mockLevelController).onSceneChangedToThis();
    }
}
