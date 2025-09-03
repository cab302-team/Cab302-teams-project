package com.example.project;

import com.example.project.Controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.example.project.Controllers.gameScreens.gameScreenController;

public class SceneManager
{
    private static Stage applicationRootStage = null;
    private static RootLayoutController rootController;
    private static final Map<SceneTypes, Parent> pages = new HashMap<>();
    private static final Map<SceneTypes, gameScreenController> controllers = new HashMap<>();

    private static final Logger logger = new Logger();

    public static void initialise(Stage stage, RootLayoutController theRootController)
    {
        if(applicationRootStage != null){
            throw new RuntimeException("Scene Manager already initialised in Application.start().");
        }

        applicationRootStage = stage;
        rootController = theRootController;
        preloadPage(SceneTypes.LOGIN, "login-view.fxml");
        preloadPage(SceneTypes.LEVEL, "level-view.fxml");
        preloadPage(SceneTypes.SHOP, "shop-view.fxml");
    }

    private static void preloadPage(SceneTypes type, String fxmlPath)
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

    public static void switchScene(SceneTypes type)
    {
        Parent page = pages.get(type);
        gameScreenController controller = controllers.get(type);
        rootController.setContent(page);
        controller.onSceneChangedToThis();
    }
}
