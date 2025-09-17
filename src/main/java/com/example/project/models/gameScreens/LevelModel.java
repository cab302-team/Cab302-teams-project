package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.models.tiles.ScrabbleLettersValues;
import com.example.project.services.sqlite.dAOs.DictionaryDAO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Represents the level model.
 */
public class LevelModel extends GameScreenModel
{

//region private fields

    private final ReadOnlyListWrapper<LetterTile> wordRowTiles = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    private final ReadOnlyListWrapper<LetterTile> tileRackRowTiles = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    private final ObservableList<LetterTile> redrawRowTiles = FXCollections.observableArrayList();
    private final ReadOnlyIntegerWrapper playersCurrentPoints = new ReadOnlyIntegerWrapper(0);
    private boolean isRedrawActive = false;
    private final DictionaryDAO dictionary = new DictionaryDAO();
    private final int initialRedraws = 4;
    private final ReadOnlyIntegerWrapper currentRedraws = new ReadOnlyIntegerWrapper(initialRedraws);
    private final int initialPlays = 4;
    private final ReadOnlyIntegerWrapper currentPlays = new ReadOnlyIntegerWrapper(initialPlays);
    private final wordTileScoreChimeAscending tileScoreSoundPlayer = new wordTileScoreChimeAscending();

//endregion

    /**
     * Gets the tile score sound effect player.
     * @return LevelTileScoreSoundPlayer.
     */
//region public getters setters
    public wordTileScoreChimeAscending getTileScoreSoundPlayer() { return this.tileScoreSoundPlayer; }

    /**
     * @return Read-only list of tiles currently in the word area
     */
    public ObservableList<LetterTile> getWordRowTilesProperty() {
        return FXCollections.unmodifiableObservableList(wordRowTiles);
    }

    /**
     * @return Read-only list of tiles currently in the rack
     */
    public ObservableList<LetterTile> getTileRackRowTilesProperty() {
        return FXCollections.unmodifiableObservableList(tileRackRowTiles);
    }

    /**
     * @return Read-only list of tiles currently in the redraw window.
     */
    public ReadOnlyListProperty<LetterTile> getRedrawRowTilesProperty() {
        return new ReadOnlyListWrapper<>(redrawRowTiles).getReadOnlyProperty();
    }

    /**
     * @return the players current level points property to observe.
     */
    public ReadOnlyIntegerProperty getPlayersCurrentPoints() {
        return playersCurrentPoints.getReadOnlyProperty();
    }

    /**
     * gets value indicating if redraw window is open.
     * @return true if redraw is active, otherwise false
     */
    public boolean getIsRedrawActive() {
        return isRedrawActive;
    }

    public void setIsRedrawActive(boolean newValue)
    {
        this.isRedrawActive = newValue;
    }

    /**
     * gets the redraws property.
     * @return the current redraws.
     */
    public ReadOnlyIntegerWrapper getCurrentRedrawsProperty(){
        return currentRedraws;
    }

    /**
     * gets the current plays.
     * @return current plays remaining.
     */
    public ReadOnlyIntegerProperty getCurrentPlaysProperty() {
        return currentPlays;
    }

    /**
     * gets the upgrades tiles observable property.
     * @return the user's session upgrade tiles.
     */
    public ReadOnlyListProperty<UpgradeTile> getUpgradeTilesProprety(){
        return Session.getUpgradeTilesProperty();
    }

    /**
     * gets the max word size.
     * @return int.
     */
    public int getMaxWordSize() { return session.getWordSize(); }

    /**
     * gets points need to win the current level.
     * @return points need to win the current level.
     */
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

    protected void setPlayersScore(int newValue)
    {
        this.playersCurrentPoints.set(newValue);
    }

    protected void addTileToWordRow(LetterTile tile)
    {
        this.wordRowTiles.add(tile);
    }

    protected void addTileToRack(LetterTile tile)
    {
        this.tileRackRowTiles.add(tile);
    }

    protected void addTileToRedrawRack(LetterTile tile)
    {
        this.redrawRowTiles.add(tile);
    }

    /**
     * Called when the level has been lost.
     * resets the players session info and logs back out to the login screen.
     */
    public void onLostLevel()
    {
        this.resetPointsRedrawsPlays();
        this.session.resetGame();
        SceneManager.getInstance().switchScene(GameScenes.LOGIN);
    }

    /**
     * Called when level has been won.
     * reset the per level info: redraws plays. Goes to shop window.
     */
    public void onWonLevel()
    {
        this.resetPointsRedrawsPlays();
        SceneManager.getInstance().switchScene(GameScenes.SHOP);
    }

    /**
     * returns true if player has won.
     * @return value indicating if player has won.
     */
    public boolean hasWon()
    {
        return (this.getHowManyPointsToBeatLevel() <= this.playersCurrentPoints.get());
    }

    /**
     * true if player has lost
     * @return value indicating if player has lost.
     */
    public boolean hasLost()
    {
        return this.currentPlays.get() == 0;
    }

    /**
     * Attempts to move a tile from rack to word area
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    private boolean tryMoveTileToWordArea(LetterTile tile) {
        if (tileRackRowTiles.contains(tile) && wordRowTiles.size() < session.getWordSize()) {
            tileRackRowTiles.remove(tile);
            wordRowTiles.add(tile);
        }
        return wordRowTiles.contains(tile);
    }

    /**
     * Attempts to move a tile from rack to redraw area
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    private boolean tryMoveTileToRedrawArea(LetterTile tile)
    {
        if (redrawRowTiles.size() < session.getRedrawWindowSize() && !redrawRowTiles.contains(tile))
        {
            var rowItsIn = tileRackRowTiles.contains(tile) ? tileRackRowTiles : wordRowTiles;
            rowItsIn.remove(tile);
            redrawRowTiles.add(tile);
        }

        return redrawRowTiles.contains(tile);
    }

    /**
     * Attempts to move a tile from word area or redraw area to rack
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    private boolean tryMoveToTileRack(LetterTile tile) {
        if (wordRowTiles.contains(tile)) {
            wordRowTiles.remove(tile);
            tileRackRowTiles.add(tile);
        } else if (redrawRowTiles.contains(tile)) {
            redrawRowTiles.remove(tile);
            tileRackRowTiles.add(tile);
        }

        return tileRackRowTiles.contains(tile);
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
                moved = tryMoveToTileRack(tile);
            }
        }
        // when redraw window is open.
        else
        {
            if (!redrawRowTiles.contains((tile))) {
                moved = tryMoveTileToRedrawArea(tile);
            }
            else {
                moved = tryMoveToTileRack(tile);
            }
        }

        if (moved) { tile.getClackSoundPlayer().play(); }
        return moved;
    }

    /**
     * Gets the current word formed by tiles in the word area
     * @return returns current word string.
     */
    private String getCurrentWord() {
        StringBuilder word = new StringBuilder();
        for (LetterTile tile : wordRowTiles) {
            word.append(tile.getLetter());
        }
        return word.toString();
    }

    /**
     * returns true if word is in dictionary.
     * @return value indicating if word is valid.
     */
    public boolean isCurrentWordValid(){
        return dictionary.isWordInDictionary(this.getCurrentWord());
    }

    /**
     * redraws tiles into the tile rack and removes from redraw window.
     */
    public void redrawTiles()
    {
        this.currentRedraws.set(this.currentRedraws.get() - 1);
        refillTileTack();
        this.redrawRowTiles.clear();
    }

    /**
     * add tile value to the player's level score
     * TODO: this will probably be changed to be adding a value to a word score that then gets added to the level
     * score after multipliers from upgrade tiles have been added..
     * @param tile tile.
     */
    public void addTileValueToScore(LetterTile tile)
    {
        this.playersCurrentPoints.set(this.playersCurrentPoints.get() + tile.getValue());
    }

    /**
     * clears the word row tiles. and refills the tile rack. and decreses the plays left.
     */
    public void playTiles()
    {
        this.wordRowTiles.clear();
        this.refillTileTack();
        this.currentPlays.set(this.currentPlays.get() - 1);
    }

    protected void setCurrentPlays(int newValue){
        this.currentPlays.set(newValue);
    }

    /**
     * sends the selected redraw tiles back to the rack
     */
    public void returnRedrawTilesToTheRack() {
        for (int i = 0; i < redrawRowTiles.size();) {
            tryMoveToTileRack(redrawRowTiles.get(i));
        }
    }

    /**
     * Initialise new level. Clears word row, redraw rack. draws new tiles for the player's tile rack.
     */
    public void setupNewLevel(){
        this.wordRowTiles.clear();
        this.redrawRowTiles.clear();
        tileRackRowTiles.clear();
        refillTileTack();
        this.currentRedraws.set(initialRedraws);
        this.currentPlays.set(initialPlays);
    }
//endregion

//region private methods
    private void resetPointsRedrawsPlays()
    {
        this.playersCurrentPoints.set(0);
        this.currentRedraws.set(initialRedraws);
        this.currentPlays.set(initialPlays);
    }

    private void generateLetters() {
        for (int i = 0; i < session.getHandSize(); i++) {
            var newLetter = new LetterTile(ScrabbleLettersValues.drawRandomTile());
            this.tileRackRowTiles.add(newLetter); // Start all tiles in rack
        }
    }

    /**
     * Refills tile rack. Only up to the hand size allowed this level.
     */
    private void refillTileTack()
    {
        var tilesToReplace = (getHandSize() - (tileRackRowTiles.size() + wordRowTiles.size()));
        for (int i = 0; i < tilesToReplace; i++){
            tileRackRowTiles.add(new LetterTile(ScrabbleLettersValues.drawRandomTile()));
        }
    }
//endregion
}