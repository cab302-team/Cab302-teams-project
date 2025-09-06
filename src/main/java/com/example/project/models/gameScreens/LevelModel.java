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
    /**
     * The hand size to start with at the start of the round in the player's tile rack.
     */
    private Integer handSize = 9;

    /**
     * maximum word length in word view.
     */
    private Integer maxWordSize = 8;

    private List<UpgradeTile> upgrades = new ArrayList<>();

    // Track tiles in their current positions
    private List<LetterTile> wordRowTiles = new ArrayList<>();
    private List<LetterTile> tileRackRowTiles = new ArrayList<>();

    private static final Random random = new Random();

    private final DictionaryDAO dictionary = new DictionaryDAO();

    /**
     * @param session game session.
     * @param observer controller that observes this model.
     */
    public LevelModel(Session session, ModelObserver observer)
    {
        super(session, observer);
        handSize = session.getHandSize();
        maxWordSize = session.getWordSize();
        this.upgrades = session.getUpgrades();

        generateLetters();

        this.observer = observer;
    }

    public Integer getMaxWordSize() { return maxWordSize; }
    public Integer getHandSize() { return handSize; }

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
    public List<UpgradeTile> getUpgrades() {
        return List.copyOf(upgrades);
    }

    private void generateLetters() {
        for (int i = 0; i < handSize; i++) {
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
        if (tileRackRowTiles.contains(tile) && wordRowTiles.size() < maxWordSize) {
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
        var tilesToReplace = wordRowTiles.size();
        for (int i = 0; i < tilesToReplace; i++){
            tileRackRowTiles.add(new LetterTile(getRandomLetter()));
        }

        wordRowTiles.clear();
        notifyObservers();
    }
}