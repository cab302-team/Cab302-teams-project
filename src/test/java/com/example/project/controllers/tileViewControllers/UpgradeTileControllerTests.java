package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.UpgradeTile;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
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

    private ImageView mockImageView;
    private StackPane mockRoot;

    @BeforeEach
    void setUp() {
        controller = new UpgradeTileController();

        // Inject mock fields without reflection
        mockImageView = mock(ImageView.class);
        mockRoot = mock(StackPane.class);

        controller.imageView = mockImageView;
        controller.root = mockRoot;
    }

    @Test
    void formatUpgradeTooltipText_ShouldIncludeNameDescriptionAndCost() throws Exception {
        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Test Upgrade")
                .description("Increases power.")
                .cost(5.0)
                .imagePath("/some/path.png")
                .upgradeEffect(() -> {})
                .build();

        controller.model = tile;

        var method = UpgradeTileController.class.getDeclaredMethod("formatUpgradeTooltipText");
        method.setAccessible(true);
        String result = (String) method.invoke(controller);

        assertTrue(result.contains("Test Upgrade"));
        assertTrue(result.contains("Increases power."));
        assertTrue(result.contains("5.00")); // cost formatting
    }

    @Test
    void bind_ShouldThrowRuntimeException_WhenPathIsInvalid() {
        UpgradeTile tile = new UpgradeTile.UpgradeBuilder()
                .name("Broken")
                .description("Missing image")
                .imagePath("/path/does/not/exist.png")
                .upgradeEffect(() -> {})
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.bind(tile));
        assertEquals("path null", ex.getMessage());
    }

    @Test
    void bind_ShouldCallGetAbilityImagePath() {
        UpgradeTile tile = mock(UpgradeTile.class);
        when(tile.getAbilityImagePath()).thenReturn("/fake/path.png");
        when(tile.getName()).thenReturn("X");
        when(tile.getDescription()).thenReturn("Y");
        when(tile.getCost()).thenReturn(2.0);

        assertThrows(RuntimeException.class, () -> controller.bind(tile));

        verify(tile, atLeastOnce()).getAbilityImagePath();
    }
    
}