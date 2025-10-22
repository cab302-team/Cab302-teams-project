package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTileModel;
import com.example.project.services.GameScenes;
import com.example.project.services.Logger;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.sound.GameSoundPlayer;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for the level model class.
 */
@ExtendWith(MockAudioSystemExtension.class)
public class LevelModelTests
{
    @Test
    void onLostLevelTest()
    {
        var mockSceneManager = mock(SceneManager.class);
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession, mock(SceneManager.class));

        // call the function tested.
        model.onLostLevel();

        // assert resetPointsRedrawsPlays occurred.
        assertEquals(0, model.getPlayersCurrentPoints().get());

        // assert session called reset game.
        verify(mockSession).resetGame();
        verify(mockSession).resetPlaysRedraws();

        // assert scene manager called switch to main menu.
        verify(mockSceneManager).switchScene(GameScenes.MAINMENU);
    }


    @Test
    void onWonLevelTest()
    {
        var mockSceneManager = mock(SceneManager.class);

        var mockSession = mock(Session.class);
        var playsLeft = 5;

        IntegerProperty mockMoney = new ReadOnlyIntegerWrapper(0);
        when(mockSession.getCurrentPlays()).thenReturn(new ReadOnlyIntegerWrapper(playsLeft));
        when(mockSession.getMoneyProperty()).thenReturn(mockMoney);

        var model = new LevelModel(mockSession, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()), mockSceneManager);

        var initialMoney = mockMoney.get();

        // call the function tested.
        model.onWonLevel();

        // assert level model results.
        assertEquals(0, model.getPlayersCurrentPoints().get());

        assertEquals(initialMoney + playsLeft, mockMoney.get());
        verify(mockSession).resetPlaysRedraws();
        verify(mockSceneManager).switchScene(GameScenes.SHOP);
    }


    @Test
    void hasWonTest_False()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession, mock(SceneManager.class));
        when(mockSession.getLevelRequirement()).thenReturn(new ReadOnlyIntegerWrapper(10));

        var actual = model.hasWon();
        assertFalse(actual);
    }

    @Test
    void hasWonTest_True()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession, mock(SceneManager.class));

        when(mockSession.getLevelRequirement()).thenReturn(new ReadOnlyIntegerWrapper(10));
        model.setTotalScore(200);

        var actual = model.hasWon();
        assertTrue(actual);
    }

    @Test
    void hasLost_True()
    {
        var mockSession = mock(Session.class);

        var mockPlays = mock(ReadOnlyIntegerWrapper.class);
        when(mockPlays.get()).thenReturn(0);
        when(mockSession.getCurrentPlays()).thenReturn(mockPlays);

        var model = new LevelModel(mockSession, mock(SceneManager.class));
        var actual = model.hasLost();
        assertTrue(actual);
    }

    @Test
    void hasLost_False()
    {
        var mockSession = mock(Session.class);
        var mockPlays = mock(ReadOnlyIntegerWrapper.class);
        when(mockPlays.get()).thenReturn(1);
        when(mockSession.getCurrentPlays()).thenReturn(mockPlays);
        mockSession.getCurrentPlays().set(1);
        var model = new LevelModel(mockSession, mock(SceneManager.class));
        var actual = model.hasLost();
        assertFalse(actual);
    }

    @Test
    void tryMoveTile_NotMoved()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession, mock(SceneManager.class));

        var wasMoved = model.tryMoveTile(new LetterTileModel('a'));
        assertFalse(wasMoved);
    }

    private LetterTileModel createMockLetterTile(){
        var tile = mock(LetterTileModel.class);
        var mockSoundPlayer = mock(GameSoundPlayer.class);
        when(tile.getClackSoundPlayer()).thenReturn(mockSoundPlayer);
        return tile;
    }

    @Test
    void tryMoveTile_RedrawClosed_MovedToTileRack()
    {
        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession, mock(SceneManager.class));

        var mockTile = createMockLetterTile();
        model.addTileToWordWindow(mockTile);
        var wasMoved = model.tryMoveTile(mockTile);
        assertTrue(wasMoved);

        // assert tile is in the tile rack
        assertTrue(model.getTileRackTilesProperty().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawClosed_MovedToWordWindow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getWordWindowSize()).thenReturn(9);
        var model = new LevelModel(mockSession, mock(SceneManager.class));

        var mockTile = createMockLetterTile();
        model.addTileToRack(mockTile);

        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        // assert tile is in the tile rack
        assertTrue(model.getWordWindowTilesProperty().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToRedrawWindowFromTileRack()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getRedrawWindowSize()).thenReturn(9);
        var model = new LevelModel(mockSession, mock(SceneManager.class));
        var mockTile = createMockLetterTile();
        model.addTileToRack(mockTile);
        model.setIsRedrawActive(true);

        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        assertTrue(model.getRedrawWindowTilesProperty().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToRedrawRowFromWordWindow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getRedrawWindowSize()).thenReturn(9);
        var model = new LevelModel(mockSession, mock(SceneManager.class));
        model.setIsRedrawActive(true);
        var mockTile = createMockLetterTile();
        model.addTileToWordWindow(mockTile);

        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        // assert tile is in the tile rack
        assertTrue(model.getRedrawWindowTilesProperty().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToTileRackFromRedrawWindow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getWordWindowSize()).thenReturn(9);
        var model = new LevelModel(mockSession, mock(SceneManager.class));
        model.setIsRedrawActive(true);
        var mockTile = createMockLetterTile();
        model.addTileToRedrawWindow(mockTile);

        // test function
        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        // assert tile is in the tile rack
        assertTrue(model.getTileRackTilesProperty().contains(mockTile));
    }


    @Test
    void isCurrentWordValid_True()
    {
        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession, mock(SceneManager.class));

        var expected = "word";
        for(char let : expected.toCharArray()){
            model.addTileToWordWindow(new LetterTileModel(let));
        }

        assertTrue(model.isCurrentWordValid());
    }

    @Test
    void isCurrentWordValid_False()
    {
        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession, mock(SceneManager.class));


        var word = "word-not-in-dictionary";
        for(char let : word.toCharArray()){
            model.addTileToWordWindow(new LetterTileModel(let));
        }

        assertFalse(model.isCurrentWordValid());
    }

    @Test
    void redrawTilesTest(){
        var mockSession = mock(Session.class);

        var redraws = 5;
        var mockRedraws = mock(ReadOnlyIntegerWrapper.class);
        when( mockRedraws.get()).thenReturn(redraws);
        when(mockSession.getCurrentRedraws()).thenReturn(mockRedraws);

        var handSize = 9;
        when(mockSession.getHandSize()).thenReturn(handSize);

        var model = new LevelModel(mockSession, mock(SceneManager.class));

        model.redrawTiles();

        assertEquals(handSize, model.getTileRackTilesProperty().size());
        assertTrue(model.getRedrawWindowTilesProperty().isEmpty());
        verify(mockSession, times(2)).getCurrentRedraws();
    }

    @Test
    void playTilesTest()
    {
        var mockSession = mock(Session.class);
        var handSize = 9;
        when(mockSession.getHandSize()).thenReturn(handSize);

        var mockPlays = mock(ReadOnlyIntegerWrapper.class);
        when(mockSession.getCurrentPlays()).thenReturn(mockPlays);

        var model = new LevelModel(mockSession, mock(SceneManager.class));

        model.addTileToWordWindow(createMockLetterTile());

        model.playTiles();

        verify(mockPlays).set((mockPlays.get()) - 1);
        assertTrue(model.getWordWindowTilesProperty().isEmpty());
        assertEquals(handSize, model.getTileRackTilesProperty().size());
    }

    @Test
    void returnRedrawTilesToTheRackTest()
    {
        // setup
        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession, mock(SceneManager.class));
        var tileA = new LetterTileModel('a');
        var tileB = new LetterTileModel('b');
        model.addTileToRedrawWindow(tileA);
        model.addTileToRedrawWindow(tileB);

        // call test method
        model.returnRedrawTilesToTheRack();

        // assert
        assertTrue(model.getTileRackTilesProperty().contains(tileA));
        assertTrue(model.getTileRackTilesProperty().contains(tileB));
    }

    @Test
    void setupNewLevelTest()
    {
        var mockSession = mock(Session.class);
        var handSize = 9;
        when(mockSession.getHandSize()).thenReturn(handSize);

        var model = new LevelModel(mockSession, mock(SceneManager.class));

        // call function
        model.setupNewLevel();

        // assert
        assertTrue(model.getWordWindowTilesProperty().isEmpty());
        assertTrue(model.getRedrawWindowTilesProperty().isEmpty());
        assertEquals(handSize, model.getTileRackTilesProperty().size());
        verify(mockSession).resetPlaysRedraws();
    }
}