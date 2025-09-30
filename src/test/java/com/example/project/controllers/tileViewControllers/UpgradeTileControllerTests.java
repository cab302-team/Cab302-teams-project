package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.UpgradeTile;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UpgradeTileController}.
 */
class UpgradeTileControllerTests {

    private UpgradeTileController controller;

    @BeforeEach
    void setUp() {
        controller = new UpgradeTileController();
    }

    @Test
    void getRoot_ShouldReturnInjectedRoot() throws Exception {
        StackPane rootPane = new StackPane();

        var rootField = UpgradeTileController.class.getDeclaredField("root");
        rootField.setAccessible(true);
        rootField.set(controller, rootPane);

        Node result = controller.getRoot();

        assertSame(rootPane, result, "getRoot() should return the injected root node.");
    }

    @Test
    void formatUpgradeTooltipText_ShouldFormatCorrectly() throws Exception {
        var tile = new UpgradeTile("Test Name", "Test Description", "/some/path.png");

        // Inject model field in superclass
        var modelField = UpgradeTileController.class.getSuperclass().getDeclaredField("model");
        modelField.setAccessible(true);
        modelField.set(controller, tile);

        var method = UpgradeTileController.class.getDeclaredMethod("formatUpgradeTooltipText");
        method.setAccessible(true);
        String result = (String) method.invoke(controller);

        assertTrue(result.contains("Test Name"));
        assertTrue(result.contains("Test Description"));
        assertTrue(result.contains("2.00")); // default cost
    }

    @Test
    void bind_ShouldThrowRuntimeException_WhenImagePathNotFound() throws Exception {
        var mockTile = mock(UpgradeTile.class);
        when(mockTile.getAbilityImagePath()).thenReturn("/this/path/does/not/exist.png");

        // Inject required fields with mocks to avoid JavaFX instantiation
        var imageViewField = UpgradeTileController.class.getDeclaredField("imageView");
        imageViewField.setAccessible(true);
        imageViewField.set(controller, mock(javafx.scene.image.ImageView.class));

        var rootField = UpgradeTileController.class.getDeclaredField("root");
        rootField.setAccessible(true);
        rootField.set(controller, mock(StackPane.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.bind(mockTile));
        assertEquals("path null", ex.getMessage());
    }

    @Test
    void bind_ShouldCallGetAbilityImagePathOnTileModel() throws Exception {
        // Arrange
        var mockTile = mock(UpgradeTile.class);
        when(mockTile.getAbilityImagePath()).thenReturn("/path/to/image.png");

        // Inject required fields with mocks
        var imageViewField = UpgradeTileController.class.getDeclaredField("imageView");
        imageViewField.setAccessible(true);
        imageViewField.set(controller, mock(javafx.scene.image.ImageView.class));

        var rootField = UpgradeTileController.class.getDeclaredField("root");
        rootField.setAccessible(true);
        rootField.set(controller, mock(StackPane.class));

        // Act
        assertThrows(RuntimeException.class, () -> controller.bind(mockTile));

        // Assert
        verify(mockTile, times(1)).getAbilityImagePath();
    }


}