package com.example.project.services;

import com.example.project.controllers.tileViewControllers.EmptyTileController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
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
        UpgradeTile upgradeTileModel = new UpgradeTile("name", "description", "path");

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
        var tileSLot = new EmptyTileSlot();

        EmptyTileController controller = factory.createEmptyTileController(tileSLot);

        // assert
        assertEquals(tileSLot, controller.getModel());
    }

}