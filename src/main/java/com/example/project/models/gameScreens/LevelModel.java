package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.sqlite.dAOs.DictionaryDAO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelModel extends GameScreenModel
{
    // Track tiles in their current positions
    private ListProperty<LetterTile> wordRowTiles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<LetterTile> tileRackRowTiles = new SimpleListProperty<>(FXCollections.observableArrayList());

    private IntegerProperty playersPoints = new SimpleIntegerProperty(0);

    public IntegerProperty playersPointsProperty() {
        return playersPoints;
    }

    private static final Random random = new Random();

    private final DictionaryDAO dictionary = new DictionaryDAO();

    private int initialRedraws = 4;
    private ReadOnlyIntegerWrapper currentRedraws = new ReadOnlyIntegerWrapper(initialRedraws);

    public ReadOnlyIntegerWrapper currentRedrawsProperty(){
        return currentRedraws;
    }

    private int initialPlays = 4;
    private ReadOnlyIntegerWrapper currentPlays = new ReadOnlyIntegerWrapper(initialPlays);

    public ReadOnlyIntegerProperty currentPlaysProperty() {
        return currentPlays;
    }

    public ListProperty<UpgradeTile> upgradeTilesProprety(){
        return Session.upgradeTilesProperty();
    }

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

    public int getHowManyPointsToBeatLevel()
    {
        return this.session.getPointsRequired();
    }

    public Integer getMaxWordSize() { return session.getWordSize(); }
    public Integer getHandSize() { return session.getHandSize(); }

    /**
     * @return Read-only list of tiles currently in the word area
     */
    public ListProperty<LetterTile> getWordviewRowTilesProperty() {
        return wordRowTiles;
    }

    /**
     * @return Read-only list of tiles currently in the rack
     */
    public ListProperty<LetterTile> getTileRackRowTilesProperty() {
        return tileRackRowTiles;
    }

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
     * Attempts to move a tile from word area to rack
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    public boolean tryMoveTileToRack(LetterTile tile) {
        if (wordRowTiles.contains(tile)) {
            wordRowTiles.remove(tile);
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
        if (tileRackRowTiles.contains(tile)) {
            return tryMoveTileToWordArea(tile);
        } else if (wordRowTiles.contains(tile)) {
            return tryMoveTileToRack(tile);
        }
        return false;
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
     * redraws tiles into the tile rack and removes from word window.
     */
    public void redrawTiles()
    {
        this.currentRedraws.set(this.currentRedraws.get() - 1);
        replaceTiles();
    }


    /**
     * Clear tiles from word row and refills tile rack.
     */
    private void replaceTiles(){
        var tilesToReplace = wordRowTiles.size();
        for (int i = 0; i < tilesToReplace; i++){
            tileRackRowTiles.add(new LetterTile(getRandomLetter()));
        }

        wordRowTiles.clear();
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
    }
}