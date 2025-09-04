package UnitTests;

import com.example.project.Controllers.RootLayoutController;
import com.example.project.ISceneManager;
import com.example.project.SceneManager;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Scene Manager Tests.
 */
public class SceneManagerTests
{
    @BeforeAll
    static void beforeAll(){
        Platform.startup(() -> {}); // start JavaFX runtime once
    }

    @Test
    void intitialiseTest()
    {
        Stage stage = mock(Stage.class);
        RootLayoutController controller = mock(RootLayoutController.class);

        var sceneManager = SceneManager.getInstance();
        sceneManager.initialise(stage, controller);
    }

    @Test
    void inisialiseThrowsIfCalledTwice()
    {
        Stage stage = mock(Stage.class);
        RootLayoutController controller = mock(RootLayoutController.class);

        var sceneManager = com.example.project.SceneManager.getInstance();
        sceneManager.initialise(stage, controller);

        assertThrows(RuntimeException.class,
                () -> sceneManager.initialise(stage, controller));
    }

}
