package com.example.project.services;

import com.example.project.controllers.RootLayoutController;
import com.example.project.services.sound.GameSoundPlayer;
import javafx.scene.Parent;

import java.util.HashMap;
import java.util.Map;
import com.example.project.controllers.gameScreens.GameScreenController;

/**
 * Represents the scene manager class.
 */
public class SceneManager
{
    private final RootLayoutController rootController;
    private Map<GameScene, Parent> pages = new HashMap<>();
    private Map<GameScene, GameScreenController> controllers = new HashMap<>();
    private GameScene currentScene;

    /**
     * Constructor with injection for tests
     * @param newRootController root controller
     * @param controllersToUse controllers
     * @param pagesToUse pages
     */
    public SceneManager(RootLayoutController newRootController, Map<GameScene,
            GameScreenController> controllersToUse, Map<GameScene, Parent> pagesToUse)
    {
        rootController = newRootController;
        controllers = controllersToUse;
        pages = pagesToUse;
    }

    /**
     * Switch between scenes specify scene type of type from {@link GameScene}.
     * @param type scene.
     */
    public void switchScene(GameScene type)
    {
        currentScene = type;
        Parent page = pages.get(type);
        rootController.setContent(page);
        GameScreenController controller = controllers.get(type);
        controller.onSceneChangedToThis();
    }

    /**
     * get current scene.
     * @return scene.
     */
    public GameScene getCurrentScene()
    {
        return currentScene;
    }
}
