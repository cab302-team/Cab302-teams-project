package com.example.project;

import com.example.project.Controllers.RootLayoutController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.example.project.initJavaToolkit;

/**
 * Scene Manager Tests. Will test preload page so will fail is FXML paths are not correct.
 */
public class SceneManagerTests
{
    @BeforeEach
    void beforeEach()
    {
        SceneManager.resetForTests(null);
    }

    @BeforeAll
    static void beforeAll()
    {
        initJavaToolkit.initJavaFX();
    }

    @Test
    void intitialiseTest()
    {
        RootLayoutController controller = new RootLayoutController();
        var sceneManager = SceneManager.getInstance();
        sceneManager.initialise(controller);
    }

    @Test
    void inisialiseThrowsIfCalledTwice()
    {
        RootLayoutController controller = new RootLayoutController();

        var sceneManager = SceneManager.getInstance();
        sceneManager.initialise(controller);

        assertThrows(RuntimeException.class,
                () -> sceneManager.initialise(controller));
    }
}
