package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTileModel;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.GameScene;
import com.example.project.services.Logger;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.sound.GameSoundPlayer;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

        var model = new LevelModel(mockSession, mockSceneManager);

        // call the function tested.
        model.onLostLevel();

        // assert resetPointsRedrawsPlays occurred.
        assertEquals(0, model.getPlayersCurrentPoints().get());

        // assert session called reset game.
        verify(mockSession).resetGame();
        verify(mockSession).resetPlaysRedraws();

        // assert scene manager called switch to main menu.
        verify(mockSceneManager).switchScene(GameScene.MAINMENU);
    }


    @Test
    void onWonLevelTest()
    {
        var mockSceneManager = mock(SceneManager.class);

        var mockSession = mock(Session.class);
        var playsLeft = 5;

        var mockMoney = new ReadOnlyDoubleWrapper(0);
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
        verify(mockSceneManager).switchScene(GameScene.SHOP);
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
        assertTrue(model.getIsRedrawActive().get());

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
        when(mockSession.getWordWindowSize()).thenReturn(9);

        var model = new LevelModel(mockSession, mock(SceneManager.class));
        model.setIsRedrawActive(false);

        var tile = model.getTileRackTilesProperty().getFirst();

        // move tile from tile rack to word.
        model.tryMoveTile(tile);

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

    @Test
    void onLostLevel()
    {
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);
        var model = new LevelModel(mockSession, mockSceneManager);
        model.onLostLevel();

        assertEquals(0, model.getPlayersCurrentPoints().get());
        verify(mockSession).resetPlaysRedraws();
        verify(mockSession).resetGame();
        verify(mockSceneManager).switchScene(GameScene.MAINMENU);
    }

    @Test
    void onWonLevel_remainingPlaysNot0()
    {
        var mockLogger = mock(Logger.class);
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);
        var model = new LevelModel(mockSession, mockLogger, mockSceneManager);
        var mockPlays = mock(ReadOnlyIntegerWrapper.class);

        var mockMoney = mock(ReadOnlyDoubleWrapper.class);
        when(mockMoney.get()).thenReturn(0.0);
        when(mockSession.getMoneyProperty()).thenReturn(mockMoney);

        when(mockPlays.get()).thenReturn(5);
        when(mockSession.getCurrentPlays()).thenReturn(mockPlays);
        model.onWonLevel();

        assertEquals(0, model.getPlayersCurrentPoints().get());
        verify(mockSession).resetPlaysRedraws();
        verify(mockSession).updateLevelInfo();
        verify(mockSceneManager).switchScene(GameScene.SHOP);
        verify(mockLogger).logMessage("You Won! Awarded $5 for 5 remaining plays");
    }

    @Test
    void onWonLevel_remainingPlays0(){
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);
        var model = new LevelModel(mockSession, mockSceneManager);
        var mockMoney = mock(ReadOnlyDoubleWrapper.class);
        when(mockMoney.get()).thenReturn(0.0);
        when(mockSession.getMoneyProperty()).thenReturn(mockMoney);
        var mockPlays = mock(ReadOnlyIntegerWrapper.class);
        when(mockPlays.get()).thenReturn(0);
        when(mockSession.getCurrentPlays()).thenReturn(mockPlays);

        model.onWonLevel();

        assertEquals(0, model.getPlayersCurrentPoints().get());
        verify(mockSession).resetPlaysRedraws();
        verify(mockSession).updateLevelInfo();
        verify(mockSceneManager).switchScene(GameScene.SHOP);
    }

    @Test
    void setWordPoints()
    {
        var model = new LevelModel(mock(Session.class), mock(SceneManager.class));
        model.setWordPoints(22);
        assertEquals(22, model.getWordPointsProperty().get());
    }

    @Test
    void setWordMulti()
    {
        var model = new LevelModel(mock(Session.class), mock(SceneManager.class));
        model.setWordMulti(22);
        assertEquals(22, model.getWordMultiProperty().get());
    }

    @Test
    void addToCombo()
    {
        var tile = mock(LetterTileModel.class);
        var tileNumber = 5;
        when(tile.getValue()).thenReturn(tileNumber);
        var model = new LevelModel(mock(Session.class), mock(SceneManager.class));

        model.addToCombo(tile);

        assertEquals(tileNumber, model.getWordPointsProperty().get());
        assertEquals(tileNumber, model.getWordPointsProperty().get());
    }

    @Test
    void calcTotalWordScore()
    {
        var mockSession = mock(Session.class);
        var mockUpgrades = mock(ReadOnlyListProperty.class);

        // create upgrades tiles to return
        var tile = new UpgradeTileModel.UpgradeBuilder()
                .name("Loaded Dice")
                .description("Value is doubled for a random letter in your word.")
                .imagePath("/com/example/project/upgradeTileImages/LoadedDice_small.png")
                .cost(2)
                .upgradeEffect(model -> model.setWordPoints(12))
                .build();

        ObservableList<UpgradeTileModel> upgradeTiles = FXCollections.observableArrayList(tile);
        when(mockUpgrades.get()).thenReturn(upgradeTiles);

        when(mockSession.getPlayersUpgradesProperty()).thenReturn(mockUpgrades);

        var model = new LevelModel(mockSession, mock(SceneManager.class));

        var score = model.calcTotalWordScore();

        assertEquals(12, score);
    }
}