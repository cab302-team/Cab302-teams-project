package com.example.project.services;

import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeEffects;
import com.example.project.models.tiles.UpgradeTile;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TileControllerFactoryTest
{
    @Test
    void createUpgradeTileTest()
    {
        // Arrange
        UpgradeTile upgradeTileModel = new UpgradeTile.UpgradeBuilder()
                .name("Grandma's Glasses")
                .description("Add +2 to the score multiplier for every identical pair of letters next to each other.")
                .imagePath("/com/example/project/upgradeTileImages/GrandmasGlasses_small.png")
                .cost(2)
                .upgradeEffect(UpgradeEffects::glassesEffect)
                .build();

        Pane pane = new Pane();

        UpgradeTileController fakeController = mock(UpgradeTileController.class);
        when(fakeController.getRoot()).thenReturn(pane);
        when(fakeController.getModel()).thenReturn(upgradeTileModel);

        var mockLoader = mock(FXMLPageLoader.class);
        try {
            when(mockLoader.load(anyString())).thenReturn(pane);
        }
        catch (IOException e){
            fail();
        }

        when(mockLoader.getController()).thenReturn(fakeController);

        TileControllerFactory factory = new TileControllerFactory(mockLoader);

        // Act
        UpgradeTileController result = factory.createUpgradeTileController(upgradeTileModel);

        // Assert
        assertSame(fakeController, result);

        // check bind was called. but cant actually call it because it sets up the ui elements.
        verify(fakeController).bind(upgradeTileModel);

        // Hover handlers attached
        assertNotNull(pane.getOnMouseEntered());
        assertNotNull(pane.getOnMouseExited());

        assertEquals(upgradeTileModel, result.getModel());
    }

    @Test
    void createLetterTileTest(){
        // Arrange
        LetterTile letterTileModel = new LetterTile('a');

        Pane pane = new Pane();

        LetterTileController fakeController = mock(LetterTileController.class);
        when(fakeController.getRoot()).thenReturn(pane);
        when(fakeController.getModel()).thenReturn(letterTileModel);

        var mockLoader = mock(FXMLPageLoader.class);
        try {
            when(mockLoader.load(anyString())).thenReturn(pane);
        }
        catch (IOException e){
            fail();
        }

        when(mockLoader.getController()).thenReturn(fakeController);

        TileControllerFactory factory = new TileControllerFactory(mockLoader);

        // Act
        LetterTileController result = factory.createLetterTileController(letterTileModel);

        // Assert
//        assertSame(fakeController, result);

        // check bind was called. but cant actually call it because it sets up the ui elements.
        verify(fakeController).bind(letterTileModel);

        // Hover handlers attached
        assertNotNull(pane.getOnMouseEntered());
        assertNotNull(pane.getOnMouseExited());

        assertEquals(letterTileModel, result.getModel());
    }

    @Test
    void createEmptyTileSlotTest()
    {
        var factory = new TileControllerFactory();
        var tileSLot = new EmptyTileSlotModel();

        EmptyTileSlotController controller = factory.createEmptyTileController(tileSLot);

        // assert
        assertEquals(tileSLot, controller.getModel());
    }

}