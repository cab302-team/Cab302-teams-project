package com.example.project;

import com.example.project.Controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.example.project.Controllers.gameScreens.gameScreenController;

/**
 * Represents the scene manager class.
 */
public class SceneManager
{
    private static RootLayoutController rootController;
    private static final Map<GameScenes, Parent> pages = new HashMap<>();
    private static final Map<GameScenes, gameScreenController> controllers = new HashMap<>();
    private static final Logger logger = new Logger();
    protected static SceneManager instance;

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

    private SceneManager() {};

    /**
     * Helper method for tests.
     */
    public static void resetForTests()
    {
        if (!Boolean.getBoolean("testenv")) {
            throw new IllegalStateException("resetForTests() must not be used in production!");
        }
        instance = null;
        rootController = null;
    }

    /**
     * loads the fxml of all the scenes on launch to switch to via scene manaager.
     * @param theRootController the root Controller for the rootLayout scene.
     */
    public void initialise(RootLayoutController theRootController)
    {
        if (rootController != null)
        {
            throw new RuntimeException("Scene Manager already initialised in Application.start().");
        }

        rootController = theRootController;

        preloadPage(GameScenes.LOGIN, "/com/example/project/GameScreens/login-view.fxml");
        preloadPage(GameScenes.LEVEL, "/com/example/project/GameScreens/level-view.fxml");
        preloadPage(GameScenes.SHOP, "/com/example/project/GameScreens/shop-view.fxml");
    }

    private void preloadPage(GameScenes type, String fxmlPath)
    {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));

        Parent page = null;
        try{
            page = loader.load();
        }
        catch (IOException e){
            throw new RuntimeException(String.format("Exception when loading page: %s. Message: %s", fxmlPath, e.getMessage()));
        }

        if (page == null)
        {
            throw new IllegalArgumentException("Page not loaded: " + type);
        }

        gameScreenController controller = loader.getController();
        if (controller == null){
            throw new RuntimeException("must have a controller on the game screen that implements gameScreenController");
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
        gameScreenController controller = controllers.get(type);
        rootController.setContent(page);
        controller.onSceneChangedToThis();
    }
}
