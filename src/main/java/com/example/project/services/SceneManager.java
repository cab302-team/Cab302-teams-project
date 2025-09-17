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
    private static RootLayoutController rootController;
    private static Map<GameScenes, Parent> pages = new HashMap<>();
    private static Map<GameScenes, GameScreenController> controllers = new HashMap<>();
    private static SceneManager instance;

    /**
     * @return Gets the programs Scene Manager instance.
     */
    public static SceneManager getInstance()
    {
        if (instance == null){
            instance = new SceneManager();
        }
        return instance;
    }

    private SceneManager() {}

    /**
     * Helper method for tests.
     * @param newInstance new Instance to inject.
     * @param newRootController root layout controller.
     * @param controllersToUse controllers to use.
     * @param pagesToUse pages to use.
     */
    public static void injectForTests(SceneManager newInstance, RootLayoutController newRootController, Map<GameScenes,
            GameScreenController> controllersToUse, Map<GameScenes, Parent> pagesToUse)
    {
        if (!Boolean.getBoolean("testenv")) {
            throw new IllegalStateException("resetForTests() must not be used in production!");
        }

        instance = newInstance;
        rootController = newRootController;
        controllers = controllersToUse;
        pages = pagesToUse;
    }

    /**
     * Helper method for tests.
     * @param newInstance new Instance to inject.
     */
    public static void injectForTests(SceneManager newInstance)
    {
        if (!Boolean.getBoolean("testenv")) {
            throw new IllegalStateException("resetForTests() must not be used in production!");
        }

        instance = newInstance;
    }

    /**
     * loads the fxml of all the scenes on launch to switch to via scene manaager.
     * @param theRootController the root Controller for the rootLayout scene.
     * @param loader FXMLLoader
     */
    public void initialise(RootLayoutController theRootController, PageLoader loader)
    {
        if (rootController != null)
        {
            throw new RuntimeException("Scene Manager already initialised in Application.start().");
        }

        rootController = theRootController;
        preloadPage(GameScenes.LOGIN, "/com/example/project/gameScreens/login-view.fxml", loader);
        preloadPage(GameScenes.LEVEL, "/com/example/project/gameScreens/level-view.fxml", loader);
        preloadPage(GameScenes.SHOP, "/com/example/project/gameScreens/shop-view.fxml", loader);
    }

    private void preloadPage(GameScenes type, String fxmlPath, PageLoader loader)
    {
        Parent page;
        try{
            page = loader.load(fxmlPath);
        }
        catch (IOException e){
            throw new RuntimeException(String.format("Exception when loading page: %s. Message: %s, cause: %s", fxmlPath, e.getMessage(), e.getCause()));
        }
        catch (IllegalStateException e){
            throw new RuntimeException(String.format("game scene fxml path for %s not correct: %s, cause: %s", fxmlPath, e.getMessage(), e.getCause()));
        }

        if (page == null)
        {
            throw new IllegalArgumentException("Page not loaded: " + type);
        }

        GameScreenController controller = loader.getController();
        if (controller == null){
            throw new RuntimeException("must have a controller on the game screen that is a gameScreenController");
        }

        controllers.put(type, controller);
        pages.put(type, page);
    }

    /**
     * Switch between scenes specifiy scene type of type from {@link GameScenes}.
     * @param type scene.
     */
    public void switchScene(GameScenes type)
    {
        Parent page = pages.get(type);
        GameScreenController controller = controllers.get(type);
        rootController.setContent(page);
        controller.onSceneChangedToThis();
    }
}
