package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTileModel;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.models.tiles.ScrabbleTileProvider;
import com.example.project.services.sound.GameSoundPlayer;
import com.example.project.services.sqlite.dAOs.DictionaryDAO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Represents the level model.
 */
public class LevelModel extends GameScreenModel
{
    private final ObservableList<LetterTileModel> wordWindowTiles = FXCollections.observableArrayList();
    private final ObservableList<LetterTileModel> tileRackTiles = FXCollections.observableArrayList();
    private final ObservableList<LetterTileModel> redrawWindowTiles = FXCollections.observableArrayList();
    private final ReadOnlyBooleanWrapper isRedrawActive = new ReadOnlyBooleanWrapper(false);
    private final ReadOnlyIntegerWrapper wordPoints = new ReadOnlyIntegerWrapper(0);
    private final ReadOnlyIntegerWrapper wordMulti = new ReadOnlyIntegerWrapper(0);
    private final ReadOnlyIntegerWrapper playersTotalPoints = new ReadOnlyIntegerWrapper(0);
    private final DictionaryDAO dictionary = new DictionaryDAO();
    private final int initialRedraws = 4;
    private final ReadOnlyIntegerWrapper currentRedraws = new ReadOnlyIntegerWrapper(initialRedraws);
    private final int initialPlays = 4;
    private final ReadOnlyIntegerWrapper currentPlays = new ReadOnlyIntegerWrapper(initialPlays);
    private final ScoreChimePlayer tileScoreSoundPlayer = new ScoreChimePlayer();
    private final ScrabbleTileProvider scrabbleLettersBalancer = new ScrabbleTileProvider();

    /**
     * @param session game session.
     */
    public LevelModel(Session session)
    {
        super(session);
        generateLetters();
    }

    /**
     * Gets the tile score sound effect player.
     * @return LevelTileScoreSoundPlayer.
     */
    public ScoreChimePlayer getTileScoreSoundPlayer() { return this.tileScoreSoundPlayer; }

    /**
     * @return Read-only list of tiles currently in the word area
     */
    public ReadOnlyListProperty<LetterTileModel> getWordWindowTilesProperty() {
        return new ReadOnlyListWrapper<>(wordWindowTiles).getReadOnlyProperty();
    }

    /**
     * @return Read-only list of tiles currently in the rack
     */
    public ReadOnlyListProperty<LetterTileModel> getTileRackTilesProperty() {
        return new ReadOnlyListWrapper<>(tileRackTiles).getReadOnlyProperty();
    }

    /**
     * @return Read-only list of tiles currently in the redraw window.
     */
    public ReadOnlyListProperty<LetterTileModel> getRedrawWindowTilesProperty() {
        return new ReadOnlyListWrapper<>(redrawWindowTiles).getReadOnlyProperty();
    }
    /**
     * @return the total points property to observe.
     */
    public ReadOnlyIntegerProperty getPlayersTotalPoints() {
        return playersTotalPoints.getReadOnlyProperty();
    }

    /**
     * @return the sum combo points property to observe.
     */
    public ReadOnlyIntegerProperty wordPointsProperty() {
        return wordPoints.getReadOnlyProperty();
    }

    /**
     * @return the players current level points property to observe.
     */
    public ReadOnlyIntegerProperty getPlayersCurrentPoints() { return playersTotalPoints.getReadOnlyProperty(); }

    /**
     * word multiplier.
     * @return multiplier.
     */
    public ReadOnlyIntegerProperty wordMultiProperty() {
        return wordMulti.getReadOnlyProperty();
    }

    /**
     * gets the redraw property.
     * @return returns indication if redraw active.
     */
    public ReadOnlyBooleanProperty getIsRedrawActive()
    {
        return isRedrawActive.getReadOnlyProperty();
    }

    /**
     * set redraw active.
     * @param newValue get if redraw window is on screen.
     */
    public void setIsRedrawActive(boolean newValue)
    {
        this.isRedrawActive.set(newValue);
    }

    /**
     * gets the redraws property.
     * @return the current redraws.
     */
    public ReadOnlyIntegerWrapper getCurrentRedraws(){
        return currentRedraws;
    }

    /**
     * gets the current plays.
     * @return current plays remaining.
     */
    public ReadOnlyIntegerProperty getCurrentPlays() {
        return currentPlays;
    }

    /**
     * gets the upgrades tiles observable property.
     * @return the user's session upgrade tiles.
     */
    public ReadOnlyListProperty<UpgradeTileModel> getUpgradeTilesProperty(){
        return this.session.getUpgradeTilesProperty();
    }

    /**
     * gets the max word size.
     * @return int.
     */
    public int getWordWindowSize() { return session.getWordWindowSize(); }

    /**
     * gets points need to win the current level.
     * @return points need to win the current level.
     */
    public int getLevelRequirement()
    {
        return this.session.getLevelRequirement();
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

    protected void setCurrentPlays(int newValue){
        this.currentPlays.set(newValue);
    }

    protected void setPlayersScore(int newValue)
    {
        this.playersTotalPoints.set(newValue);
    }

    protected void addTileToWordWindow(LetterTileModel tile)
    {
        this.wordWindowTiles.add(tile);
    }

    protected void addTileToRack(LetterTileModel tile)
    {
        this.tileRackTiles.add(tile);
    }

    protected void addTileToRedrawWindow(LetterTileModel tile)
    {
        this.redrawWindowTiles.add(tile);
    }

    /**
     * Called when the level has been lost.
     * resets the players session info and logs back out to the login screen.
     */
    public void onLostLevel()
    {
        this.resetLevelVariables();
        this.session.resetGame();
        SceneManager.getInstance().switchScene(GameScenes.MAINMENU);
    }

    /**
     * Called when level has been won.
     * reset the per level info: redraws plays. Goes to shop window.
     */
    public void onWonLevel()
    {
        //award money equal to remaining plays
        int remainingPlays = this.currentPlays.get();
        if (remainingPlays > 0)
        {
            session.addMoney(remainingPlays);
            this.logger.logMessage(String.format("You Won! Awarded $%d for %d remaining plays",
                    remainingPlays, remainingPlays));
        }
        this.resetLevelVariables();
        SceneManager.getInstance().switchScene(GameScenes.SHOP);
    }

    /**
     * returns true if player has won.
     * @return value indicating if player has won.
     */
    public boolean hasWon()
    {
        return (this.getLevelRequirement() <= this.playersTotalPoints.get());
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
    private boolean tryMoveTileToWordArea(LetterTileModel tile) {
        if (tileRackTiles.contains(tile) && wordWindowTiles.size() < session.getWordWindowSize()) {
            tileRackTiles.remove(tile);
            wordWindowTiles.add(tile);
        }
        return wordWindowTiles.contains(tile);
    }

    /**
     * Attempts to move a tile from rack to redraw area
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    private boolean tryMoveTileToRedrawArea(LetterTileModel tile)
    {
        if (redrawWindowTiles.size() < session.getRedrawWindowSize() && !redrawWindowTiles.contains(tile))
        {
            var rowItsIn = tileRackTiles.contains(tile) ? tileRackTiles : wordWindowTiles;
            rowItsIn.remove(tile);
            redrawWindowTiles.add(tile);
        }

        return redrawWindowTiles.contains(tile);
    }

    /**
     * Attempts to move a tile from word area or redraw area to rack
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    private boolean tryMoveToTileRack(LetterTileModel tile) {
        if (wordWindowTiles.contains(tile)) {
            wordWindowTiles.remove(tile);
            tileRackTiles.add(tile);
        } else if (redrawWindowTiles.contains(tile)) {
            redrawWindowTiles.remove(tile);
            tileRackTiles.add(tile);
        }

        return tileRackTiles.contains(tile);
    }

    /**
     * determines where tile should go and moves it
     * @param tile The tile to move
     * @return true if move was successful, false otherwise
     */
    public boolean tryMoveTile(LetterTileModel tile) {
        boolean moved = false;

        // When no redraw window is open.
        if (!isRedrawActive.get()) {
            if (tileRackTiles.contains((tile))) {
                moved = tryMoveTileToWordArea(tile);
            }
            else if (wordWindowTiles.contains(tile)) {
                moved = tryMoveToTileRack(tile);
            }
        }
        // when redraw window is open.
        else
        {
            if (!redrawWindowTiles.contains((tile))) {
                moved = tryMoveTileToRedrawArea(tile);
            }
            else {
                moved = tryMoveToTileRack(tile);
            }
        }

        if (moved) { tile.getClackSoundPlayer().replay(); }
        return moved;
    }

    /**
     * Gets the current word formed by tiles in the word area
     * @return returns current word string.
     */
    public String getCurrentWord() {
        StringBuilder word = new StringBuilder();
        for (LetterTileModel tile : wordWindowTiles) {
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
        this.redrawWindowTiles.clear();
        refillTileTack();
    }

    /**
     * add combo sum and multiCombo
     * @param tile tile.
     */
    public void addToCombo(LetterTileModel tile) {
        this.wordPoints.set(this.wordPoints.get() + tile.getValue());
        this.wordMulti.set(this.wordMulti.get() + 1);
    }

    /**
     * @return total score int
     */
    public int calcTotalWordScore() {
        for (UpgradeTileModel upgrade : this.getUpgradeTilesProperty()) {
            upgrade.getUpgradeEffect().run();
        }
        return this.wordPoints.get() * this.wordMulti.get();
    }

    /**
     * sets the current word points before multipliers
     * @param newWordPoints the new word points value
     */
    public void setWordPoints(int newWordPoints) {
        this.wordPoints.set(newWordPoints);
    }

    /**
     * sets the current word multiplier
     * @param newMulti the new multiplier value
     */
    public void setWordMulti(int newMulti) {
        this.wordMulti.set(newMulti);
    }

    /**
     * @param totalScore from calcTotalScore
     * sets Total Score
     */
    public void setTotalScore(int totalScore){
        this.playersTotalPoints.set(totalScore);
    }

    /**
     * clears the word row tiles. and refills the tile rack. and decreases the plays left.
     */
    public void playTiles()
    {
        this.wordWindowTiles.clear();
        this.refillTileTack();
        this.currentPlays.set(this.currentPlays.get() - 1);
    }

    /**
     * sends the selected redraw tiles back to the rack
     */
    public void returnRedrawTilesToTheRack() {
        for (int i = 0; i < redrawWindowTiles.size();) {
            tryMoveToTileRack(redrawWindowTiles.get(i));
        }
    }

    /**
     * Initialise new level. Clears word row, redraw rack. draws new tiles for the player's tile rack.
     */
    public void setupNewLevel()
    {
        this.wordWindowTiles.clear();
        this.returnRedrawTilesToTheRack();
        isRedrawActive.set(false);
        this.currentRedraws.set(initialRedraws);
        this.currentPlays.set(initialPlays);
    }

    /**
     * resets counts for sum and multi in combo
     */
    public void resetCombo()
    {
        this.wordPoints.set(0);
        this.wordMulti.set(0);
    }

    private void resetLevelVariables()
    {
        this.playersTotalPoints.set(0);
        this.currentRedraws.set(initialRedraws);
        this.currentPlays.set(initialPlays);
    }

    private void generateLetters() {
        for (int i = 0; i < session.getHandSize(); i++) {
            var newLetter = new LetterTileModel(this.scrabbleLettersBalancer.drawRandomTile());
            this.tileRackTiles.add(newLetter); // Start all tiles in rack
        }
    }

    /**
     * Refills tile rack. Only up to the hand size allowed this level.
     */
    private void refillTileTack()
    {
        var tilesPlayerHas = tileRackTiles.size() + wordWindowTiles.size() + redrawWindowTiles.size();
        var tilesToReplace = (getHandSize() - tilesPlayerHas);
        for (int i = 0; i < tilesToReplace; i++){
            tileRackTiles.add(new LetterTileModel(this.scrabbleLettersBalancer.drawRandomTile()));
        }
    }
}