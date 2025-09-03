package com.example.project;

import com.example.project.Controllers.RootLayoutController;
import com.example.project.Controllers.gameScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        try
        {
            preloadPage(SceneTypes.LOGIN, "login-view.fxml");
            preloadPage(SceneTypes.LEVEL, "level-view.fxml");
            // TODO add more scenes here.
        }
        catch (IOException e)
        {
            logger.logError("Couldn't initialise fxml.");
        }
    }

    private static void preloadPage(SceneTypes type, String fxmlPath) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent page = loader.load();

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
        if (page == null)
        {
            throw new IllegalArgumentException("Page not loaded: " + type);
        }

        gameScreenController controller = controllers.get(type);
        rootController.setContent(page);
        controller.onSceneChangedToThis();
    }
}
