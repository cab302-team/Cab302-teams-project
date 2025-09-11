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
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

/**
 * Controller for the level screen.
 */
public class LevelController extends GameScreenController
{
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

    @FXML
    Label scoreRequiredText;

    @FXML
    Label playersPointsText;

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
        levelModel = new LevelModel(Session.getInstance());
    }

    private void syncTileRackRowTiles()
    {
        tileControllerMap.clear();
        createLetterTileControllers();
        updateTileRow(wordWindowTileSlots, levelModel.getWordviewRowTilesProperty().get());
        updateTileRow(tileRackTileSlots, levelModel.getTileRackRowTilesProperty().get());
    }

    private void syncWordviewTiles()
    {
        // TODO: use 2 lists of controllers NOT a map.
        tileControllerMap.clear();
        createLetterTileControllers();
        updateTileRow(wordWindowTileSlots, levelModel.getWordviewRowTilesProperty().get());
        updateTileRow(tileRackTileSlots, levelModel.getTileRackRowTilesProperty().get());
    }

    private void syncPlayersPointsProperty(Number newVal)
    {
        this.playersPointsText.setText(String.format("you: %s", newVal));
    }

    private void syncRedrawButton()
    {
        var redraws = levelModel.getWordviewRowTilesProperty().get();
        redrawButton.setDisable(redraws.isEmpty() || (redraws.equals(0)));
        this.redrawButton.setText(String.format("redraws left: %s", levelModel.currentRedrawsProperty().get()));
    }

    private void syncPlayButton()
    {
        var plays = levelModel.currentPlaysProperty().get();
        playButton.setDisable((plays == 0) || !levelModel.isWordValid() || levelModel.getWordviewRowTilesProperty().get().isEmpty());
        this.playButton.setText(String.format("plays left: %s", plays));
    }

    private void syncUpgradeTiles(){
        upgradeTileRackAtTop.getChildren().clear();
        upgradeTiles.clear();
        loadUpgradeTiles();
    }

    /**
     * This runs after the constructor and after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    public void initialize()
    {
        createEmptySlots(); // TODO: if word option or hand size changes will need to change this.

        // Setup Listeners. (automatically updates each property when they're changed)
        // listener
        levelModel.playersPointsProperty().addListener((obs, oldVal, newVal) -> {
            syncPlayersPointsProperty(newVal);
        });

        levelModel.currentRedrawsProperty().addListener((obs, oldVal, newVal) -> {
            syncRedrawButton();
        });

        levelModel.currentPlaysProperty().addListener((obs, oldVal, newVal) -> {
            syncPlayButton();
        });

        levelModel.upgradeTilesProprety().addListener((obs, oldVal, newVal) -> {
            syncUpgradeTiles();
        });

        levelModel.getTileRackRowTilesProperty().addListener((obs, oldVal, newVal) -> {
            syncTileRackRowTiles();
            syncPlayButton();
            syncRedrawButton();
        });

        levelModel.getWordviewRowTilesProperty().addListener((obs, oldVal, newVal) -> {
            syncWordviewTiles();
            syncPlayButton();
            syncRedrawButton();
        });
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");

        scoreRequiredText.setText(String.format("require: %s", levelModel.getHowManyPointsToBeatLevel()));

        // sync observable properties.
        syncPlayersPointsProperty(levelModel.playersPointsProperty().get());
        syncPlayButton();
        syncRedrawButton();
        syncUpgradeTiles();
        syncTileRackRowTiles();
        syncWordviewTiles();
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
        for (UpgradeTile upgradeTile : levelModel.upgradeTilesProprety().get()) {
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
    private void onPlayButton()
    {
        levelModel.decreasePlays();
        SequentialTransition tileTransitions = new SequentialTransition();

        for (LetterTile tc : levelModel.getWordviewRowTilesProperty().get())
        {
            this.logger.logMessage(String.format("animated tile %s", tc.getLetter()));
            var letterTileController = this.tileControllerMap.get(tc);

            var translateUp = new TranslateTransition(Duration.seconds(0.1), letterTileController.getRoot());
            translateUp.setByY(-10);

            translateUp.setOnFinished(e -> {
                levelModel.addTileToScore(letterTileController.getModel());
                this.logger.logMessage("finished tile anim up.");
                this.playersPointsText.setTextFill(Color.GREEN);
            });

            tileTransitions.getChildren().add(translateUp);
            tileTransitions.getChildren().addAll(animatePointScore());
        }

        // After all tiles
        var timeDelay = new PauseTransition(Duration.seconds(3));
        timeDelay.setOnFinished(e -> wonOrLost());
        tileTransitions.setOnFinished(e -> {
            timeDelay.play();
        });

        tileTransitions.play();
    }

    private List<Animation> animatePointScore()
    {
        var revertSizeTransition = new ScaleTransition(Duration.seconds(0.2), this.playersPointsText);
        revertSizeTransition.setToY(1);
        revertSizeTransition.setToX(1);
        revertSizeTransition.setOnFinished(e -> {
            this.playersPointsText.setTextFill(Color.BLACK);
        });

        var doubleSizeTransition = new ScaleTransition(Duration.seconds(0.2), this.playersPointsText);
        doubleSizeTransition.setToY(2);
        doubleSizeTransition.setToX(2);

        return Arrays.asList(doubleSizeTransition, revertSizeTransition);
    }

    private void wonOrLost()
    {
        levelModel.playTiles();
        if (levelModel.hasWon()) { levelModel.won(); }
        else if (levelModel.hasLost()) { levelModel.lostLevel(); }
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
        if(!levelModel.getWordviewRowTilesProperty().get().isEmpty()){
            levelModel.redrawTiles();
        }
    }
}