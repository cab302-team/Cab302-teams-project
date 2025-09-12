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
    private List<LetterTile> redrawRowTiles = new ArrayList<>();

    private boolean isRedrawActive = false;

    private static final Random random = new Random();

    private final DictionaryDAO dictionary = new DictionaryDAO();

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

    public Integer getMaxWordSize() { return session.getWordSize(); }
    public Integer getHandSize() { return session.getHandSize(); }
    public Integer getRedrawWindowSize() { return session.getRedrawWindowSize(); }

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
     * @return All letter tiles for loading tiles and controllers and ui in levelController
     */
    public List<LetterTile> getLetterTiles() {
        List<LetterTile> allTiles = new ArrayList<>();
        allTiles.addAll(wordRowTiles);
        allTiles.addAll(tileRackRowTiles);
        allTiles.addAll(redrawRowTiles);
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

        if (moved) { notifyObservers(); }
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
     * @return Read-only list of tiles currently in the redraw window.
     */
    public List<LetterTile> getRedrawRowTiles() {
        return List.copyOf(redrawRowTiles);
    }

    /**
     * redraws tiles into the tile rack and removes from redraw window.
     */
    public void redrawTiles()
    {
        var tilesToReplace = redrawRowTiles.size();
        for (int i = 0; i < tilesToReplace; i++){
            tileRackRowTiles.add(new LetterTile(getRandomLetter()));
        }

        redrawRowTiles.clear();
        notifyObservers();
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

    /**
     * @return true if redraw is active, otherwise false
     */
    public boolean isRedrawActive() {
        return isRedrawActive;
    }

}