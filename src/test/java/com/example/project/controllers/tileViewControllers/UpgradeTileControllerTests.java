package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.UpgradeTileModel;
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

    @BeforeEach
    void setUp() {
        controller = new UpgradeTileController();

        // Inject mock fields without reflection
        ImageView mockImageView = mock(ImageView.class);
        StackPane mockRoot = mock(StackPane.class);

        controller.imageView = mockImageView;
        controller.root = mockRoot;
    }

    @Test
    void bind_ShouldCallGetAbilityImagePath() {
        UpgradeTileModel tile = mock(UpgradeTileModel.class);
        when(tile.getAbilityImagePath()).thenReturn("/fake/path.png");
        when(tile.getName()).thenReturn("X");
        when(tile.getDescription()).thenReturn("Y");
        when(tile.getCost()).thenReturn(2.0);

        assertThrows(RuntimeException.class, () -> controller.bind(tile));

        verify(tile, atLeastOnce()).getAbilityImagePath();
    }
    
}