package com.example.project;

import com.example.project.controllers.RootLayoutController;
import com.example.project.controllers.gameScreens.GameScreenFactory;
import com.example.project.services.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.project.services.FXMLPageLoader;

import java.io.IOException;

/**
 * The entry point of the application.
 */
public class Application extends javafx.application.Application
{
    /// Constants defining the window title and size
    public static final String TITLE = "WordPlay";

    /// defining the window width.
    public static final int WIDTH = 1300;

    /// Defining the window height.
    public static final int HEIGHT = 700;

    /**
     * dev buttons should be on or not.
     */
    public static boolean DEV_BUTTONS_ON = false;

    @Override
    public void start(Stage stage) throws IOException
    {
        String rootScenePath = "/com/example/project/rootLayout-view.fxml";
        FXMLLoader rootLoader = new FXMLLoader(this.getClass().getResource(rootScenePath));
        Parent root = rootLoader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(true);
        stage.setFullScreen(true);

        Session session = new Session();
        GameScreenFactory factory = new GameScreenFactory(session);
        RootLayoutController rootController = rootLoader.getController();
        factory.loadGameScreens(rootController, new FXMLPageLoader());
    }

    /**
     * @param args the command-line arguments passed to your program when it starts.
     */
    public static void main(String[] args) {
        launch();
    }
}