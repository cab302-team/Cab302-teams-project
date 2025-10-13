package com.example.project.controllers.gameScreens;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Minimal test for abstract {@link GameScreenController}.
 */
class GameScreenControllerTests {

    // Concrete stub to test instantiation
    static class StubController extends GameScreenController {
        @Override
        public void onSceneChangedToThis() {
            // No-op for testing
        }
    }

    @Test
    void constructor_ShouldNotThrow() {
        assertDoesNotThrow(() -> new StubController());
    }
}