package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTileModel;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.sound.GameSoundPlayer;
import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        var sceneManager = new SceneManager(mockSceneManager);

        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);

        // call the function tested.
        model.onLostLevel();

        // assert resetPointsRedrawsPlays occurred.
        assertEquals(0, model.getPlayersCurrentPoints().get());
        assertEquals(4, model.getCurrentPlays().get());
        assertEquals(4, model.getCurrentRedraws().get());

        // assert session called reset game.
        verify(mockSession).resetGame();

        // assert scene manager called switch to log in.
        verify(mockSceneManager).switchScene(GameScenes.LOGIN);
    }


    @Test
    void onWonLevelTest()
    {
        var mockSceneManager = mock(SceneManager.class);
        var sceneManager = new SceneManager(mockSceneManager);

        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession);

        // call the function tested.
        model.onWonLevel();

        // assert level model results.
        assertEquals(0, model.getPlayersCurrentPoints().get());
        assertEquals(4, model.getCurrentRedraws().get());
        assertEquals(4, model.getCurrentRedraws().get());
        verify(mockSceneManager).switchScene(GameScenes.SHOP);
    }


    @Test
    void hasWonTest_False()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);
        when(mockSession.getLevelRequirement()).thenReturn(10);

        var actual = model.hasWon();
        assertFalse(actual);
    }

    @Test
    void hasWonTest_True()
    {
        var mockSession = mock(Session.class);

        var model = new LevelModel(mockSession);

        when(mockSession.getLevelRequirement()).thenReturn(10);
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
        var model = new LevelModel(mockSession);

        var mockTile = createMockLetterTile();
        model.addTileToWordRow(mockTile);
        var wasMoved = model.tryMoveTile(mockTile);
        assertTrue(wasMoved);

        // assert tile is in the tile rack
        assertTrue(model.getTileRackTiles().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawClosed_MovedToWordRow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getWordSize()).thenReturn(9);
        var model = new LevelModel(mockSession);

        var mockTile = createMockLetterTile();
        model.addTileToRack(mockTile);

        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        // assert tile is in the tile rack
        assertTrue(model.getWordRowTilesProperty().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToRedrawRowFromTileRack()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getRedrawWindowSize()).thenReturn(9);
        var model = new LevelModel(mockSession);
        var mockTile = createMockLetterTile();
        model.addTileToRack(mockTile);
        model.setIsRedrawActive(true);

        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        assertTrue(model.getRedrawRowTilesProperty().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToRedrawRowFromWordRow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getRedrawWindowSize()).thenReturn(9);
        var model = new LevelModel(mockSession);
        model.setIsRedrawActive(true);
        var mockTile = createMockLetterTile();
        model.addTileToWordRow(mockTile);

        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        // assert tile is in the tile rack
        assertTrue(model.getRedrawRowTilesProperty().contains(mockTile));
    }

    @Test
    void tryMoveTile_RedrawActive_MovedToTileRackFromRedrawRow()
    {
        var mockSession = mock(Session.class);
        when(mockSession.getWordSize()).thenReturn(9);
        var model = new LevelModel(mockSession);
        model.setIsRedrawActive(true);
        var mockTile = createMockLetterTile();
        model.addTileToRedrawRack(mockTile);

        // test function
        var wasMoved = model.tryMoveTile(mockTile);

        assertTrue(wasMoved);
        // assert tile is in the tile rack
        assertTrue(model.getTileRackTiles().contains(mockTile));
    }


    @Test
    void isCurrentWordValid_True()
    {
        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession);

        var expected = "word";
        for(char let : expected.toCharArray()){
            model.addTileToWordRow(new LetterTileModel(let));
        }

        assertTrue(model.isCurrentWordValid());
    }

    @Test
    void isCurrentWordValid_False()
    {
        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession);


        var word = "word-not-in-dictionary";
        for(char let : word.toCharArray()){
            model.addTileToWordRow(new LetterTileModel(let));
        }

        assertFalse(model.isCurrentWordValid());
    }

    @Test
    void redrawTilesTest(){
        var mockSession = mock(Session.class);

        var handSize = 9;
        when(mockSession.getHandSize()).thenReturn(handSize);

        var model = new LevelModel(mockSession);

        model.redrawTiles();

        assertEquals(handSize, model.getTileRackTiles().size());
        assertEquals(3, model.getCurrentRedraws().get());
        assertTrue(model.getRedrawRowTilesProperty().isEmpty());
    }

    @Test
    void playTilesTest()
    {
        var mockSession = mock(Session.class);
        var handSize = 9;
        when(mockSession.getHandSize()).thenReturn(handSize);

        var model = new LevelModel(mockSession);

        var currentPlays = model.getCurrentPlays().get();

        model.addTileToWordRow(createMockLetterTile());

        model.playTiles();

        assertTrue(model.getWordRowTilesProperty().isEmpty());
        assertEquals(handSize, model.getTileRackTiles().size());
        assertEquals((currentPlays - 1), model.getCurrentPlays().get());
    }

    @Test
    void returnRedrawTilesToTheRackTest()
    {
        // setup
        var mockSession = mock(Session.class);
        var model = new LevelModel(mockSession);
        var tileA = new LetterTileModel('a');
        var tileB = new LetterTileModel('b');
        model.addTileToRedrawRack(tileA);
        model.addTileToRedrawRack(tileB);

        // call test method
        model.returnRedrawTilesToTheRack();

        // assert
        assertTrue(model.getTileRackTiles().contains(tileA));
        assertTrue(model.getTileRackTiles().contains(tileB));
    }

    @Test
    void setupNewLevelTest(){
        var mockSession = mock(Session.class);
        var handSize = 9;
        when(mockSession.getHandSize()).thenReturn(handSize);

        var model = new LevelModel(mockSession);

        // call function
        model.setupNewLevel();

        // assert
        assertTrue(model.getWordRowTilesProperty().isEmpty());
        assertTrue(model.getRedrawRowTilesProperty().isEmpty());
        assertEquals(handSize, model.getTileRackTiles().size());
        assertEquals(4, model.getCurrentRedraws().get());
        assertEquals(4, model.getCurrentPlays().get());
    }
}