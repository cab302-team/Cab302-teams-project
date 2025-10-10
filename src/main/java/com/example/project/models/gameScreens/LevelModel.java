package com.example.project.models.gameScreens;

import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.models.tiles.ScrabbleTileProvider;
import com.example.project.services.sqlite.dAOs.DictionaryDAO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Represents the level model.
 */
public class LevelModel extends GameScreenModel
{
    private final ObservableList<LetterTile> wordRowTiles = FXCollections.observableArrayList();
    private final ObservableList<LetterTile> tileRackRowTiles = FXCollections.observableArrayList();
    private final ObservableList<LetterTile> redrawRowTiles = FXCollections.observableArrayList();
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
    public int getMaxWordSize() { return session.getWordSize(); }

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
        this.resetLevelVariables();
        this.session.resetGame();
        SceneManager.getInstance().switchScene(GameScenes.LOGIN);
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
        if (!isRedrawActive.get()) {
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

        if (moved) { tile.getClackSoundPlayer().replay(); }
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
        this.redrawRowTiles.clear();
        refillTileTack();
    }

    /**
     * add combo sum and multiCombo
     * @param tile tile.
     */
    public void addToCombo(LetterTile tile) {
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
        this.wordRowTiles.clear();
        this.refillTileTack();
        this.currentPlays.set(this.currentPlays.get() - 1);
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
    public void setupNewLevel()
    {
        this.wordRowTiles.clear();
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
            var newLetter = new LetterTile(this.scrabbleLettersBalancer.drawRandomTile());
            this.tileRackRowTiles.add(newLetter); // Start all tiles in rack
        }
    }

    /**
     * Refills tile rack. Only up to the hand size allowed this level.
     */
    private void refillTileTack()
    {
        var tilesPlayerHas = tileRackRowTiles.size() + wordRowTiles.size() + redrawRowTiles.size();
        var tilesToReplace = (getHandSize() - tilesPlayerHas);
        for (int i = 0; i < tilesToReplace; i++){
            tileRackRowTiles.add(new LetterTile(this.scrabbleLettersBalancer.drawRandomTile()));
        }
    }
}