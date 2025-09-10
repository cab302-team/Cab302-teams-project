package com.example.project.models.gameScreens;

import com.example.project.controllers.gameScreens.ModelObserver;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.Session;
import com.example.project.services.sqlite.dAOs.DictionaryDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelModel extends GameScreenModel
{
    // Track tiles in their current positions
    private List<LetterTile> wordRowTiles = new ArrayList<>();
    private List<LetterTile> tileRackRowTiles = new ArrayList<>();
    private int playersPoints = 0;

    private static final Random random = new Random();

    private final DictionaryDAO dictionary = new DictionaryDAO();

    private int initialRedraws = 4;
    private int currentRedraws = initialRedraws;

    private int initialPlays = 4;
    private int currentPlays = initialPlays;

    /**
     * @param session game session.
     * @param observer controller that observes this model.
     */
    public LevelModel(Session session, ModelObserver observer)
    {
        super(session, observer);
        generateLetters();

        this.observer = observer;
    }

    public void lostLevel()
    {
        this.reset();
        this.session.resetGame();
    }

    public void reset()
    {
        this.playersPoints = 0;
        this.currentRedraws = initialRedraws;
        this.currentPlays = initialPlays;
    }

    public int getPlayersPoints() { return playersPoints;}

    public boolean hasWon()
    {
        return (this.getHowManyPointsToBeatLevel() <= this.playersPoints);
    }

    public int getRedrawsLeft()
    {
        return currentRedraws;
    }

    public int getPlaysLeft(){
        return currentPlays;
    }

    public boolean hasLost()
    {
        return !hasWon() && this.getPlaysLeft() == 0;
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
    public List<LetterTile> getWordRowTiles() {
        return List.copyOf(wordRowTiles);
    }

    /**
     * @return Read-only list of tiles currently in the rack
     */
    public List<LetterTile> getTileRackRowTiles() {
        return List.copyOf(tileRackRowTiles);
    }

    /**
     * @return All letter tiles (for initial setup compatibility)
     */
    public List<LetterTile> getLetterTiles() {
        List<LetterTile> allTiles = new ArrayList<>();
        allTiles.addAll(wordRowTiles);
        allTiles.addAll(tileRackRowTiles);
        return List.copyOf(allTiles);
    }

    /**
     * @return Read-only list of upgrades
     */
    public List<UpgradeTile> getUpgrades()
    {
        return List.copyOf(session.getUpgrades());
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
            notifyObservers();
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
            notifyObservers();
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
        if (this.currentRedraws == 0) { return; }
        this.currentRedraws--;
        replaceTiles();
        notifyObservers();
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
        notifyObservers();
    }

    public void decreasePlays(){
        this.currentPlays--;
    }

    public void addTileToScore(LetterTile tile)
    {
        this.playersPoints += tile.getValue();
        this.notifyObservers();
    }

    public void playTiles(){
        this.replaceTiles();
    }
}