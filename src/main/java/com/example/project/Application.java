package com.example.project;

import com.example.project.services.GameScenes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.project.services.FXMLPageLoader;
import com.example.project.services.SceneManager;

import java.io.IOException;

/**
 * The Application class.
 */
public class Application extends javafx.application.Application
{
    /**
     * Constants defining the window title and size
     */
    public static final String TITLE = "Wordplay";

    @Override
    public void start(Stage stage) throws IOException
    {
        String rootScenePath = "/com/example/project/rootLayout-view.fxml";
        FXMLLoader rootLoader = new FXMLLoader(this.getClass().getResource(rootScenePath));
        Parent root = rootLoader.load();
        Scene scene = new Scene(root, 640, 400);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);

        var sceneManager = SceneManager.getInstance();
        sceneManager.initialise(rootLoader.getController(), new FXMLPageLoader());
        sceneManager.switchScene(GameScenes.LOGIN);
    }

    /**
     * Entry point of the application.
     * @param args the command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        launch();
    }
}