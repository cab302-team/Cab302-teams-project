package com.example.project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The entry point of the application.
 */
public class Application extends javafx.application.Application
{
    /// Constants defining the window title and size
    public static final String TITLE = "The Application Name!";

    /// defining the window width.
    public static final int WIDTH = 640;

    /// Defining the window height.
    public static final int HEIGHT = 360;

    @Override
    public void start(Stage stage) throws IOException
    {
        String rootScenePath = "/com/example/project/rootLayout-view.fxml";
        FXMLLoader rootLoader = new FXMLLoader(SceneManager.class.getResource(rootScenePath));
        Parent root = rootLoader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);

        SceneManager.initialise(stage, rootLoader.getController());
        SceneManager.switchScene(SceneTypes.LOGIN);
    }

    /**
     * @param args the command-line arguments passed to your program when it starts.
     */
    public static void main(String[] args) {
        launch();
    }
}