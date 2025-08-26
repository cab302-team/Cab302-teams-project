package com.example.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The entry point of the application.
 */
public class HelloApplication extends Application
{
    /// Constants defining the window title and size
    public static final String TITLE = "The Application Name!";

    /// defining the window width.
    public static final int WIDTH = 640;

    /// Defining the window height.
    public static final int HEIGHT = 360;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command-line arguments passed to your program when it starts.
     */
    public static void main(String[] args) {
        launch();
    }
}