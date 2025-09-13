package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.sqlite.dAOs.DictionaryDAO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

/**
 * Represents the level model.
 */
public class LevelModel extends GameScreenModel
{

//region private fields
    private final ObservableList<LetterTile> wordRowTiles = FXCollections.observableArrayList();
    private final ObservableList<LetterTile> tileRackRowTiles = FXCollections.observableArrayList();
    private ObservableList<LetterTile> redrawRowTiles = FXCollections.observableArrayList();
    private final ReadOnlyIntegerWrapper playersPoints = new ReadOnlyIntegerWrapper(0);
    private boolean isRedrawActive = false;
    private static final Random random = new Random();
    private final DictionaryDAO dictionary = new DictionaryDAO();
    private final int initialRedraws = 4;
    private final ReadOnlyIntegerWrapper currentRedraws = new ReadOnlyIntegerWrapper(initialRedraws);
    private final int initialPlays = 4;
    private final ReadOnlyIntegerWrapper currentPlays = new ReadOnlyIntegerWrapper(initialPlays);

//endregion

//region public getters setters
    /**
     * @return Read-only list of tiles currently in the word area
     */
    public ReadOnlyListProperty<LetterTile> getWordRowTilesProperty() {
        return new ReadOnlyListWrapper<>(wordRowTiles).getReadOnlyProperty();
    }
    /**
     * @return Read-only list of tiles currently in the rack
     */
    public ReadOnlyListProperty<LetterTile> getTileRackRowTilesProperty() {
        return new ReadOnlyListWrapper<>(tileRackRowTiles).getReadOnlyProperty();
    }
    /**
     * @return Read-only list of tiles currently in the redraw window.
     */
    public ReadOnlyListProperty<LetterTile> getRedrawRowTilesProperty() {
        return new ReadOnlyListWrapper<>(redrawRowTiles).getReadOnlyProperty();
    }
    public ReadOnlyIntegerProperty playersPointsProperty() {
        return playersPoints.getReadOnlyProperty();
    }
    /**
     * @return true if redraw is active, otherwise false
     */
    public boolean getIsRedrawActive() {
        return isRedrawActive;
    }

    public ReadOnlyIntegerWrapper currentRedrawsProperty(){
        return currentRedraws;
    }
    public ReadOnlyIntegerProperty currentPlaysProperty() {
        return currentPlays;
    }

    public ReadOnlyListProperty<UpgradeTile> upgradeTilesProprety(){
        return Session.upgradeTilesProperty();
    }

    /**
     * gets the max word size.
     * @return int.
     */
    public int getMaxWordSize() { return session.getWordSize(); }

    public int getHowManyPointsToBeatLevel()
    {
        return this.session.getPointsRequired();
    }

    /**
     * gets the hand size.
     * @return int.
     */
    public int getHandSize() { return session.getHandSize(); }

    /**
     * gets the redraw window size (number of slots in the window).
     * @return int.
     */
    public Integer getRedrawWindowSize() { return session.getRedrawWindowSize(); }

//endregion

//region public methods
    /**
     * @param session game session.
     */
    public LevelModel(Session session)
    {
        super(session);
        generateLetters();
    }

    public void onLostLevel()
    {
        this.resetPointsRedrawsPlays();
        this.session.resetGame();
        SceneManager.getInstance().switchScene(GameScenes.LOGIN);
    }

    public void onWonLevel()
    {
        this.resetPointsRedrawsPlays();
        SceneManager.getInstance().switchScene(GameScenes.SHOP);
    }
    private void resetPointsRedrawsPlays()
    {
        this.playersPoints.set(0);
        this.currentRedraws.set(initialRedraws);
        this.currentPlays.set(initialPlays);
    }
    public boolean hasWon()
    {
        return (this.getHowManyPointsToBeatLevel() <= this.playersPoints.get());
    }

    public boolean hasLost()
    {
        return !hasWon() && this.currentPlays.get() == 0;
    }

    /**
     * Attempts to move a tile from rack to word area
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    public boolean tryMoveTileToWordArea(LetterTile tile) {
        if (tileRackRowTiles.contains(tile) && wordRowTiles.size() < session.getWordSize()) {
            tileRackRowTiles.remove(tile);
            wordRowTiles.add(tile);
            return true;
        }
        return false;
    }

    /**
     * Attempts to move a tile from rack to redraw area
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    public boolean tryMoveTileToRedrawArea(LetterTile tile) {
        if (tileRackRowTiles.contains(tile) && redrawRowTiles.size() < session.getRedrawWindowSize()) {
            tileRackRowTiles.remove(tile);
            redrawRowTiles.add(tile);
            return true;
        }
        return false;
    }

    /**
     * Attempts to move a tile from word area or redraw area to rack
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    public boolean tryMoveTileToRack(LetterTile tile) {
        if (wordRowTiles.contains(tile)) {
            wordRowTiles.remove(tile);
            tileRackRowTiles.add(tile);
            return true;
        } else if (redrawRowTiles.contains(tile)) {
            redrawRowTiles.remove(tile);
            tileRackRowTiles.add(tile);
            return true;
        }
        return false;
    }

    /**
     * determines where tile should go and moves it
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    public boolean tryMoveTile(LetterTile tile) {
        boolean moved = false;

        // When no redraw window is open.
        if (!isRedrawActive) {
            if (tileRackRowTiles.contains((tile))) {
                moved = tryMoveTileToWordArea(tile);
            }
            else if (wordRowTiles.contains(tile)) {
                moved = tryMoveTileToRack(tile);
            }
        }
        // when redraw window is open.
        else {
            if (!redrawRowTiles.contains((tile))) {
                moved = tryMoveTileToRedrawArea(tile);
            }
            else {
                moved = tryMoveTileToRack(tile);
            }
        }

        return moved;
    }

    /**
     * Gets the current word formed by tiles in the word area
     * @return returns current word string.
     */
    public String getCurrentWord() {
        StringBuilder word = new StringBuilder();
        for (LetterTile tile : wordRowTiles) {
            word.append(tile.getLetter());
        }
        return word.toString();
    }

    public boolean isWordValid(){
        return dictionary.isWordInDictionary(this.getCurrentWord());
    }

    /**
     * redraws tiles into the tile rack and removes from redraw window.
     */
    public void redrawTiles()
    {
        this.currentRedraws.set(this.currentRedraws.get() - 1);
        replaceTiles();
    }

    public void addTileToScore(LetterTile tile)
    {
        this.playersPoints.set(this.playersPoints.get() + tile.getValue());
    }

    public void playTiles()
    {
        this.replaceTiles();
    }

    public void decreasePlays(){
        this.currentPlays.set(this.currentPlays.get() - 1);
        redrawRowTiles.clear();
    }

    /**
     * sends the selected redraw tiles back to the rack
     */
    public void clearRedrawTiles() {
        for (int i = 0; i < redrawRowTiles.size();){
            tryMoveTile(redrawRowTiles.get(i));
        }
    }

    /**
     * changes active redraw status
     */
    public void toggleRedrawState() {
        isRedrawActive = !isRedrawActive;
    }
//endregion

//region private methods
    private void generateLetters() {
        for (int i = 0; i < session.getHandSize(); i++) {
            var newLetter = new LetterTile(getRandomLetter());
            this.tileRackRowTiles.add(newLetter); // Start all tiles in rack
        }
    }

    private Character getRandomLetter() {
        return (char) ('A' + random.nextInt(26));
    }

    /**
     * Clear tiles from word row and refills tile rack.
     */
    private void replaceTiles(){
        var tilesToReplace = redrawRowTiles.size();
        for (int i = 0; i < tilesToReplace; i++){
            tileRackRowTiles.add(new LetterTile(getRandomLetter()));
        }

        redrawRowTiles.clear();
    }


//endregion
}