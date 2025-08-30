package com.example.project;

import com.example.project.Controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager
{
    private static Stage applicationRootStage = null;
    private static RootLayoutController rootController;
    private static final Map<SceneTypes, Parent> pages = new HashMap<>();
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
            loadPage(SceneTypes.LOGIN, "login-view.fxml");
            loadPage(SceneTypes.LEVEL, "level-view.fxml");
        }
        catch (IOException e)
        {
            logger.logError("Couldn't initialise fxml.");
        }
    }

    private static void loadPage(SceneTypes type, String fxmlPath) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent page = loader.load();
        pages.put(type, page);
    }

    public static void switchScene(SceneTypes type)
    {
        Parent page = pages.get(type);
        if (page == null)
        {
            throw new IllegalArgumentException("Page not loaded: " + type);
        }

        rootController.setContent(page);
    }
}
