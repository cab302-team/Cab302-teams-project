package com.example.project.services;

import com.example.project.controllers.RootLayoutController;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.example.project.controllers.gameScreens.GameScreenController;

/**
 * Represents the scene manager class.
 */
public class SceneManager
{
    private RootLayoutController rootController;
    private Map<GameScenes, Parent> pages = new HashMap<>();
    private Map<GameScenes, GameScreenController> controllers = new HashMap<>();

    /**
     * Constructor with injection for tests
     * @param newRootController root controller
     * @param controllersToUse controllers
     * @param pagesToUse pages
     */
    public SceneManager(RootLayoutController newRootController, Map<GameScenes,
            GameScreenController> controllersToUse, Map<GameScenes, Parent> pagesToUse)
    {
        rootController = newRootController;
        controllers = controllersToUse;
        pages = pagesToUse;
    }

    /**
     * Switch between scenes specify scene type of type from {@link GameScenes}.
     * @param type scene.
     */
    public void switchScene(GameScenes type)
    {
        Parent page = pages.get(type);
        rootController.setContent(page);
        GameScreenController controller = controllers.get(type);
        controller.onSceneChangedToThis();
    }
}
