package com.example.project.controllers.gameScreens;


import com.example.project.controllers.tileViewControllers.EmptyTileController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.TileLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Label;


/**
 * Controller for the level screen.
 */
public class LevelController extends GameScreenController implements ModelObserver {
    @FXML
    HBox tileRackContainer;

    @FXML
    HBox wordViewHBox;

    @FXML
    HBox upgradeTileRackAtTop;

    @FXML
    Button playButton;

    @FXML
    Button redrawButton;

    @FXML private Label scoreToBeatLabel;
    @FXML private Label currentScoreLabel;
    @FXML private Label comboCountLabel;
    @FXML private Label comboMultiplierLabel;
    @FXML private Label playsLeftLabel;
    @FXML private Label redrawsLeftLabel;


    private static LevelModel levelModel;

    private final List<EmptyTileController> wordWindowTileSlots = new ArrayList<>();
    private final List<EmptyTileController> tileRackTileSlots = new ArrayList<>();
    private final List<UpgradeTileViewController> upgradeTiles = new ArrayList<>();
    private final Map<LetterTile, LetterTileController> tileControllerMap = new HashMap<>();

    /**
     * Constructor only called once each time application opened.
     */
    public LevelController()
    {
        levelModel = new LevelModel(Session.getInstance(), this);
    }

    /**
     * This runs after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    public void initialize() {
        createEmptySlots();
        onModelChanged();
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");
        onModelChanged();
    }

    /**
     * Observer called when model changes
     */
    @Override
    public void onModelChanged()
    {
        // clear tile controller map and recreate controllers incase any tiles were added/redrawn.
        tileControllerMap.clear();
        createLetterTileControllers();
        updateTileRow(wordWindowTileSlots, levelModel.getWordRowTiles());
        updateTileRow(tileRackTileSlots, levelModel.getTileRackRowTiles());
        updateRoundScore(levelModel.getRoundScore());
        updateLevelScore(levelModel.getLevelScore());
        updatePlayCount(levelModel.getPlayCount());
        updatePlayRedrawButtons();
        // sync upgrade tiles.
        upgradeTileRackAtTop.getChildren().clear();
        upgradeTiles.clear();
        loadUpgradeTiles();
    }

    /**
     * Create empty slots for both word area and tile rack
     */
    private void createEmptySlots() {
        // Create word area slots
        for (var i = 0; i < levelModel.getMaxWordSize(); i++) {
            var emptyTileController = loadEmptySlotIntoContainer(wordViewHBox);
            wordWindowTileSlots.add(emptyTileController);
        }

        // Create tile rack slots
        for (var i = 0; i < levelModel.getHandSize(); i++) {
            var emptyTileController = loadEmptySlotIntoContainer(tileRackContainer);
            tileRackTileSlots.add(emptyTileController);
        }
    }

    /**
     * Create tile controllers for all letter tiles
     */
    private void createLetterTileControllers()
    {
        for (LetterTile tile : levelModel.getLetterTiles())
        {
            LetterTileController letterController = TileLoader.createLetterTile(tile);
            letterController.getRoot().setOnMouseClicked(e -> onLetterTileClicked(letterController, tile));
            tileControllerMap.put(tile, letterController);
        }
    }

    /**
     * Load upgrade tiles
     */
    private void loadUpgradeTiles()
    {
        for (UpgradeTile upgradeTile : levelModel.getUpgrades()) {
            UpgradeTileViewController newUpgrade = TileLoader.createUpgradeTile(upgradeTile);
            upgradeTileRackAtTop.getChildren().add(newUpgrade.getRoot());
            upgradeTiles.add(newUpgrade);
        }
    }

    private void updateTileRow(List<EmptyTileController> rowsEmptyTiles, List<LetterTile> letterTilesToPutIn)
    {
        // Clear all word area slots
        for (EmptyTileController slot : rowsEmptyTiles) {
            slot.clearLetterTile();
        }

        // Place tiles from model into slots
        for (int i = 0; i < letterTilesToPutIn.size(); i++)
        {
            LetterTile tile = letterTilesToPutIn.get(i);
            LetterTileController controller = tileControllerMap.get(tile);
            rowsEmptyTiles.get(i).setLetter(controller);
        }
    }

    private void updatePlayRedrawButtons()
    {
        playButton.setDisable(levelModel.getWordRowTiles().isEmpty() || !levelModel.isWordValid());
        redrawButton.setDisable(levelModel.getWordRowTiles().isEmpty());
    }

    private void updateRoundScore(Integer roundscore)
    {
        String score_txt = Integer.toString(roundscore);
        currentScoreLabel.setText(score_txt);
    }

    private void updateLevelScore(Integer levelscore)
    {
        String score_txt = Integer.toString(levelscore);
        scoreToBeatLabel.setText(score_txt);
    }

    private void updatePlayCount(Integer playcount)
    {
        String count_txt = Integer.toString(playcount);
        playsLeftLabel.setText(count_txt);
    }

    /**
     * Handle tile clicks.
     */
    private void onLetterTileClicked(LetterTileController tileController, LetterTile tile) {
        boolean moved = levelModel.tryMoveTile(tile);

        if (!moved) { this.logger.logMessage("Cannot move tile - no space available or tile not found."); }
        // View updates happen automatically via onModelChanged()
    }

    /**
     * Load a new empty slot into container
     */
    private EmptyTileController loadEmptySlotIntoContainer(HBox container)
    {
        var emptyTile = new EmptyTileSlot();
        EmptyTileController controller = TileLoader.createEmptyTileController(emptyTile);
        container.getChildren().add(controller.getRoot());
        return controller;
    }

    /**
     * Handle play button delegate to model.
     */
    @FXML
    private void onPlayButton() {
        this.logger.logMessage("TODO: play button things.");
        List<LetterTile> tiles = new ArrayList<>(levelModel.getWordRowTiles());
        int wordlength = 0;
        int wordsum = 0;
        // wordscore = (wordsum + any sum modifiers) x (wordlength + any multi modifiers)
        for (LetterTile tile : tiles) {
            wordlength += 1;
            int value = tile.getValue();
            wordsum += value;

        }
        // TODO: add sum and multi modifiers
        int wordscore = wordsum * wordlength;

        // TODO: seperate method for animation timeline for adding modifiers (maybe light up tiles as well)
        // imported javafx.animation.Timeline and javafx.animation.KeyFrame for these methods
        animateSum(wordsum, wordlength, wordscore);


    }

    // method for wordsum animated counter which once finished chains in next animation
    private void animateSum(int finalSum, int finalMulti, int finalScore) {
        IntegerProperty currentSum = new SimpleIntegerProperty(0);

        Timeline sumTimeline = new Timeline();
        sumTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), event -> {
                    if (currentSum.get() < finalSum) {
                        currentSum.set(currentSum.get() + 1);
                        comboCountLabel.setText(String.valueOf(currentSum.get()));
                    }
                })
        );
        sumTimeline.setCycleCount(finalSum);
        sumTimeline.play();

        // Chain the next animation to start after this one finishes
        sumTimeline.setOnFinished(e -> animateMulti(finalMulti, finalScore));
    }

    // method for wordlength animated counter which once finished chains in final animation
    private void animateMulti(int finalMulti, int finalScore) {
        IntegerProperty currentMultiplier = new SimpleIntegerProperty(0);

        Timeline multiplierTimeline = new Timeline();
        multiplierTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), event -> {
                    if (currentMultiplier.get() < finalMulti) {
                        currentMultiplier.set(currentMultiplier.get() + 1);
                        comboMultiplierLabel.setText(String.valueOf(currentMultiplier.get()));
                    }
                })
        );
        multiplierTimeline.setCycleCount(finalMulti);
        multiplierTimeline.play();

        // Chain the final animation to start after this one finishes
        multiplierTimeline.setOnFinished(e -> animateTotalScore(finalScore));
    }

    // method for wordscore animated counter which once finished also updates levelmodel
    private void animateTotalScore(int finalScore) {
        IntegerProperty currentScore = new SimpleIntegerProperty(levelModel.getRoundScore());
        int finalTotalScore = levelModel.getRoundScore() + finalScore;

        Timeline scoreTimeline = new Timeline();
        scoreTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(50), event -> {
                    if (currentScore.get() < finalTotalScore) {
                        currentScore.set(currentScore.get() + 1);
                        currentScoreLabel.setText(String.valueOf(currentScore.get()));
                    }
                })
        );
        scoreTimeline.setCycleCount(finalScore);
        scoreTimeline.play();

        // Final updates after all animations are done
        scoreTimeline.setOnFinished(e -> {
            levelModel.updatePlayCount();
            levelModel.updateRoundScore(finalScore);
            levelModel.redrawTiles();
            comboCountLabel.setText("0");
            comboMultiplierLabel.setText("0");
            onModelChanged(); // This updates the UI and redraws tiles
        });
    }

    @FXML
    private void onSkipButton(){
        this.logger.logMessage("Skip to shop");
        SceneManager.getInstance().switchScene(GameScenes.SHOP);
    }

    /**
     * Handle redraw button delegated to model.
     */
    @FXML
    private void onRedrawButton() {
        // View updates automatically via observer pattern
        if(!levelModel.getWordRowTiles().isEmpty()){
            levelModel.redrawTiles();
        }
    }
}