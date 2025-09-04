package UnitTests;

import com.example.project.Controllers.RootLayoutController;
import com.example.project.SceneManager;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Scene Manager Tests.
 */
public class SceneManagerTests
{
    @BeforeEach
    void beforeEach()
    {
        SceneManager.resetForTests();
    }

    @BeforeAll
    static void beforeAll(){
        Platform.startup(() -> {}); // start JavaFX runtime once
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
