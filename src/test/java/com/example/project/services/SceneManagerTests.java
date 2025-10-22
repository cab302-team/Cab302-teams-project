package com.example.project.services;

import com.example.project.controllers.RootLayoutController;
import com.example.project.controllers.gameScreens.GameScreenController;
import com.example.project.controllers.gameScreens.LevelController;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

/**
 * Scene Manager Tests. Will test preload page so will fail is FXML paths are not correct.
 */
@ExtendWith(MockAudioSystemExtension.class)
public class SceneManagerTests
{


    @Test
    void switchScenes_SwitchToLevel()
    {
        RootLayoutController mockRootLayoutController = mock(RootLayoutController.class);

        // Mock controller.setContent response do nothing
        doNothing().when(mockRootLayoutController).setContent(any(Parent.class));

        // initialise with mock controller
        LevelController mockLevelController = mock(LevelController.class);
        doNothing().when(mockLevelController).onSceneChangedToThis();

        Map<GameScene, GameScreenController> mockControllers = new HashMap<>();
        mockControllers.put(GameScene.LEVEL, mockLevelController);

        Map<GameScene, Parent> mockPages = new HashMap<>();
        Parent page = new Pane();
        mockPages.put(GameScene.LEVEL, page);

        var sceneManager = new SceneManager(mockRootLayoutController, mockControllers, mockPages);

        // test method
        sceneManager.switchScene(GameScene.LEVEL);

        // assert
        verify(mockLevelController).onSceneChangedToThis();
    }
}
