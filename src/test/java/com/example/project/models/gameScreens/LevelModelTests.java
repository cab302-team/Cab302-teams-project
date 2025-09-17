package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * unit tests for the level model class.
 */
public class LevelModelTests
{
    @Test
    void onLostLevelTest()
    {
        var mockSceneManager = mock(SceneManager.class);
        SceneManager.injectForTests(mockSceneManager);

        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);

        // call the function tested.
        model.onLostLevel();

        // assert resetPointsRedrawsPlays occured.
        assertEquals(0, model.getPlayersCurrentPoints().get());
        assertEquals(4, model.getCurrentRedrawsProperty().get());
        assertEquals(4, model.getCurrentPlaysProperty().get());

        // assert session called reset game.
        verify(mockSession).resetGame();

        // assert scene manager called switch to login.
        verify(mockSceneManager).switchScene(GameScenes.LOGIN);
    }


    @Test
    void onWonLevel(){
        var mockSceneManager = mock(SceneManager.class);
        SceneManager.injectForTests(mockSceneManager);

        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession);

        // call the function tested.
        model.onLostLevel();

        // assert level model results.
        assertEquals(0, model.getPlayersCurrentPoints().get());
        assertEquals(4, model.getCurrentRedrawsProperty().get());
        assertEquals(4, model.getCurrentPlaysProperty().get());

        verify(mockSceneManager).switchScene(GameScenes.LOGIN);
    }


    @Test
    void hasWonTest_False()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);
        when(mockSession.getPointsRequired()).thenReturn(10);

        var actual = model.hasWon();
        assertFalse(actual);
    }

    @Test
    void hasWonTest_True()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);

        when(mockSession.getPointsRequired()).thenReturn(10);
        model.setPlayersScore(200);

        var actual = model.hasWon();
        assertTrue(actual);
    }

    @Test
    void hasLost_True()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);
        model.setCurrentPlays(0);

        var actual = model.hasLost();
        assertTrue(actual);
    }

    @Test
    void hasLost_False()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);
        model.setCurrentPlays(1);

        var actual = model.hasLost();
        assertFalse(actual);
    }

    @Test
    void tryMoveTile_NotMoved()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);

        var wasMoved = model.tryMoveTile(new LetterTile('a'));
        assertFalse(wasMoved);
    }

    @Test
    void tryMoveTile_RedrawClosed_MovedToTileRack()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);
        var tile = new LetterTile('a');

        model.addTileToWordRow(tile);

        var wasMoved = model.tryMoveTile(tile);
        assertTrue(wasMoved);

        // assert tile is in the tile rack
        assertTrue(model.getTileRackRowTilesProperty().contains(tile));
    }

    @Test
    void tryMoveTile_RedrawClosed_MovedToWordRow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getWordSize()).thenReturn(9);
        var model = new LevelModel(mockSession);
        var tile = new LetterTile('a');

        model.addTileToRack(tile);

        var wasMoved = model.tryMoveTile(tile);
        assertTrue(wasMoved);

        // assert tile is in the tile rack
        assertTrue(model.getWordRowTilesProperty().contains(tile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToRedrawRowFromTileRack()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getRedrawWindowSize()).thenReturn(9);

        var model = new LevelModel(mockSession);
        var tile = new LetterTile('a');
        model.setIsRedrawActive(true);

        // test method
        model.addTileToRack(tile);

        // assertions
        var wasMoved = model.tryMoveTile(tile);
        assertTrue(wasMoved);

        // assert tile is in the tile rack
        assertTrue(model.getRedrawRowTilesProperty().contains(tile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToRedrawRowFromWordRow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getRedrawWindowSize()).thenReturn(9);

        var model = new LevelModel(mockSession);
        var tile = new LetterTile('a');
        model.setIsRedrawActive(true);

        // test method
        model.addTileToWordRow(tile);

        // assertions
        var wasMoved = model.tryMoveTile(tile);
        assertTrue(wasMoved);

        // assert tile is in the tile rack
        assertTrue(model.getRedrawRowTilesProperty().contains(tile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToTileRackFromRedrawRow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getWordSize()).thenReturn(9);

        var model = new LevelModel(mockSession);
        var tile = new LetterTile('a');
        model.setIsRedrawActive(true);

        // test method
        model.addTileToRedrawRack(tile);

        // assertions
        var wasMoved = model.tryMoveTile(tile);
        assertTrue(wasMoved);

        // assert tile is in the tile rack
        assertTrue(model.getTileRackRowTilesProperty().contains(tile));
    }
}
