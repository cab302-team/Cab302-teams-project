package com.example.project;

import com.example.project.controllers.RootLayoutController;
import com.example.project.controllers.gameScreens.GameScreenController;
import com.example.project.controllers.gameScreens.LevelController;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Scene Manager Tests. Will test preload page so will fail is FXML paths are not correct.
 */
public class SceneManagerTests
{
    @BeforeEach
    void beforeEach()
    {
        SceneManager.injectForTests(null, null, new HashMap<GameScenes, GameScreenController>(), new HashMap<GameScenes, Parent>());
    }

    @BeforeAll
    static void beforeAll()
    {
        initJavaToolkit.initJavaFX();
    }

    @Test
    void intitialise_FirstTimeSuccess()
    {
        RootLayoutController controller = new RootLayoutController();
        var sceneManager = SceneManager.getInstance();
        sceneManager.initialise(controller);

        // Assert pages loaded


    }

    @Test
    void initialise_ThrowsIfCalledTwice()
    {
        RootLayoutController controller = new RootLayoutController();

        var sceneManager = SceneManager.getInstance();
        sceneManager.initialise(controller);

        assertThrows(RuntimeException.class,
                () -> sceneManager.initialise(controller));
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

        SceneManager.injectForTests(null, mockRootLayoutController, mockControllers, mockPages);

        // test method
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);

        // assert
        verify(mockLevelController).onSceneChangedToThis();
    }
}
