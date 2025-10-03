package com.example.project.controllers.gameScreens;

import com.example.project.services.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GameScreenController}.
 */
class GameScreenControllerTests {

    static class TestGameScreenController extends GameScreenController {
        @Override
        public void onSceneChangedToThis() {
            logger.logMessage("Test scene changed");
        }
    }

    @Test
    void constructor_ShouldCreateLogger() {
        GameScreenController controller = new TestGameScreenController();

        assertNotNull(controller.logger, "Logger should be initialized");
    }

    @Test
    void onSceneChangedToThis_ShouldNotThrow() {
        GameScreenController controller = new TestGameScreenController();

        assertDoesNotThrow(controller::onSceneChangedToThis);
    }
}