package com.example.project.services;

import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.*;
import com.example.project.services.sound.GameSoundPlayer;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockAudioSystemExtension.class)
class TileControllerFactoryTests
{
    @Test
    void createTileController_UpgradeTileTest()
    {
        // Arrange
        UpgradeTileModel upgradeTileModel = mock(UpgradeTileModel.class);

        var mockSoundPlayer = mock(GameSoundPlayer.class);
        when(upgradeTileModel.getHoverSoundPlayer()).thenReturn(mockSoundPlayer);
        Pane spyPane = Mockito.spy(new Pane());

        UpgradeTileController fakeController = mock(UpgradeTileController.class);
        when(fakeController.getRoot()).thenReturn(spyPane);
        when(fakeController.getModel()).thenReturn(upgradeTileModel);

        var mockLoader = mock(FXMLPageLoader.class);
        try {
            when(mockLoader.load(anyString())).thenReturn(spyPane);
        }
        catch (IOException e){
            fail();
        }

        when(mockLoader.getController()).thenReturn(fakeController);

        TileControllerFactory factory = new TileControllerFactory(mockLoader);

        // Act
        UpgradeTileController result = factory.createTileController(upgradeTileModel, UpgradeTileController.class);

        // Assert
        assertSame(fakeController, result);

        // check bind was called.
        verify(fakeController).bind(upgradeTileModel);

        // Hover handlers attached
        verify(spyPane).setOnMouseEntered(any());
        verify(spyPane).setOnMouseExited(any());

        // hover action
        spyPane.getOnMouseEntered().handle(null);
        verify(spyPane).setScaleX(anyDouble());
        verify(spyPane).setScaleY(anyDouble());

        verify(upgradeTileModel).getHoverSoundPlayer();
        verify(mockSoundPlayer).replay();

        spyPane.getOnMouseExited().handle(null);
        verify(spyPane, times(2)).setScaleX(anyDouble());
        verify(spyPane, times(2)).setScaleY(anyDouble());

        assertEquals(upgradeTileModel, result.getModel());
    }

    @Test
    void createLetterTileTest(){
        // Arrange
        LetterTileModel letterTileModel = new LetterTileModel('a');

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
        LetterTileController result = factory.createTileController(letterTileModel, LetterTileController.class);

        // Assert
        assertSame(fakeController, result);

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
        var tileModel = new EmptyTileSlotModel();

        EmptyTileSlotController controller = factory.createTileController(tileModel, EmptyTileSlotController.class);

        // assert
        assertEquals(tileModel, controller.getModel());
    }

    @Test
    void createUpgradeTileControllerThrowsRuntimeException()
    {
        // createGenericTileController throws
        var mockLoader = mock(FXMLPageLoader.class);
        var fxmlPath = "path";
        var mockLetterModel = mock(LetterTileModel.class);
        when(mockLetterModel.getFXMLPath()).thenReturn(fxmlPath);

        var mockController = mock(LetterTileController.class);
        when(mockLoader.getController()).thenReturn(mockController);

        try {
            doThrow(IOException.class).when(mockLoader).load(fxmlPath);
        } catch (IOException e) {
            // This will never happen at runtime because it’s a mock setup
            throw new RuntimeException("Test: createUpgradeTileControllerThrowsRuntimeException failed.");
        }

        var factory = new TileControllerFactory(mockLoader);

        var expected = String.format("Failed to create tile controller: " + fxmlPath);
        assertThrows(RuntimeException.class, () -> factory.createLetterTileController(mockLetterModel), expected);
    }


    private class UnsupportedTileModel extends TileModel
    {

        @Override
        public String getFXMLPath() {
            return "";
        }
    }

    private class BadController extends TileController<UnsupportedTileModel> {

        @Override
        public Node getRoot() {
            return null;
        }

        @Override
        public void bind(UnsupportedTileModel tile) {

        }
    }

    @Test
    void createTileController_ThrowIllegalArgument_TileModel()
    {
        UnsupportedTileModel test = new UnsupportedTileModel();
        var factory = new TileControllerFactory();
        var expected = String.format("Unsupported tile type: " + test.getClass());
        assertThrows(IllegalArgumentException.class, () -> factory.createTileController(test, BadController.class), expected);
    }
}