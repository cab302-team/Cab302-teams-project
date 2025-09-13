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
import javafx.collections.ObservableList;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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

    @FXML
    VBox redrawContainer;

    @FXML
    Button confirmRedrawButton;

    private static LevelModel levelModel;

    private final List<EmptyTileController> wordWindowTileSlots = new ArrayList<>();
    private final List<EmptyTileController> tileRackTileSlots = new ArrayList<>();
    private final List<EmptyTileController> redrawWindowTileSlots = new ArrayList<>();
    private final List<UpgradeTileViewController> upgradeTiles = new ArrayList<>();

    private final List<LetterTileController> tileRackTileControllers = new ArrayList<>();
    private final List<LetterTileController> wordTileControllers = new ArrayList<>();
    private final List<LetterTileController> redrawTileControllers = new ArrayList<>();

    /**
     * Constructor only called once each time application opened.
     */
    public LevelController()
    {
        levelModel = new LevelModel(Session.getInstance());
    }

    private void syncTileRackRowTiles()
    {
        syncTiles(tileRackTileSlots, tileRackTileControllers, levelModel.getTileRackRowTilesProperty());
    }

    private void syncTiles(List<EmptyTileController> rowsEmptyTiles, List<LetterTileController> controllers, ObservableList<LetterTile> listProperty)
    {
        controllers.clear();

        for (LetterTile tile : listProperty){
            var controller = TileLoader.createLetterTile(tile);
            controller.getRoot().setOnMouseClicked(e -> onLetterTileClicked(controller));

            controllers.add(controller);
        }

        // Clear all word area slots
        for (EmptyTileController rowsEmptyTile : rowsEmptyTiles) {
            rowsEmptyTile.clearLetterTile();
        }

        for (int i = 0; i < controllers.size(); i++){
            rowsEmptyTiles.get(i).setLetter(controllers.get(i));
        }
    }

    private void syncWordTiles()
    {
        syncTiles(wordWindowTileSlots, wordTileControllers, levelModel.getWordRowTilesProperty());
    }

    private void syncRedrawRowTiles()
    {
        syncTiles(redrawWindowTileSlots, redrawTileControllers, levelModel.getRedrawRowTilesProperty());
    }

    private void syncPlayersPointsProperty(Number newVal)
    {
        this.playersPointsText.setText(String.format("%s", newVal));
    }

    private void syncRedrawButton()
    {
        var redraws = levelModel.currentRedrawsProperty().get();
        redrawButton.setDisable(redraws == 0);

        if (levelModel.getIsRedrawActive()){
            this.redrawButton.setText(String.format("cancel (redraws left: %s)", levelModel.currentRedrawsProperty().get()));
        }
        else{
            this.redrawButton.setText(String.format("redraw (redraws left: %s)", levelModel.currentRedrawsProperty().get()));
        }
    }

    private void syncPlayButton()
    {
        var plays = levelModel.currentPlaysProperty().get();
        playButton.setDisable((plays == 0) || !levelModel.isWordValid() || levelModel.getWordRowTilesProperty().get().isEmpty() || levelModel.getIsRedrawActive());
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
        createEmptySlots(); // TODO: if word size changes or hand size changes will need to reload empty slots.

        // Setup Listeners. (automatically updates each property when they're changed)
        levelModel.playersPointsProperty().addListener((obs, oldVal, newVal) -> syncPlayersPointsProperty(newVal));

        levelModel.currentRedrawsProperty().addListener((obs, oldVal, newVal) -> syncRedrawButton());

        levelModel.currentPlaysProperty().addListener((obs, oldVal, newVal) -> syncPlayButton());

        levelModel.upgradeTilesProprety().addListener((obs, oldVal, newVal) -> syncUpgradeTiles());

        levelModel.getTileRackRowTilesProperty().addListener((obs, oldVal, newVal) -> {
            syncTileRackRowTiles();
            syncPlayButton();
            syncRedrawButton();
        });

        levelModel.getWordRowTilesProperty().addListener((obs, oldVal, newVal) -> {
            syncWordTiles();
            syncPlayButton();
            syncRedrawButton();
        });

        levelModel.getRedrawRowTilesProperty().addListener((obs, oldVal, newVal) -> {
            syncRedrawRowTiles();
            syncPlayButton();
            syncRedrawButton();
            syncConfirmRedrawButton();
        });
    }

    private void syncConfirmRedrawButton(){
        confirmRedrawButton.setDisable(levelModel.getRedrawRowTilesProperty().isEmpty());
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");
        scoreRequiredText.setText(String.format("required: %s", levelModel.getHowManyPointsToBeatLevel()));
        levelModel.resetLevelTiles();

        // sync observable properties.
        syncPlayersPointsProperty(levelModel.playersPointsProperty().get());
        syncPlayButton();
        syncRedrawButton();
        syncUpgradeTiles();
        syncTileRackRowTiles();
        syncWordTiles();

        syncRedrawRowTiles();
        syncConfirmRedrawButton();
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

        // Create redraw window slots
        for (var i = 0; i < levelModel.getRedrawWindowSize(); i++) {
            var emptyTileController = loadEmptySlotIntoContainer(redrawContainer);
            redrawWindowTileSlots.add(emptyTileController);
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

    /**
     * Handle tile clicks.
     */
    private void onLetterTileClicked(LetterTileController tileController) {
        boolean moved = levelModel.tryMoveTile(tileController.getModel());
        if (!moved) { this.logger.logMessage("Cannot move tile - no space available or tile not found."); }
        // View updates happen automatically via onModelChanged()
    }

    /**
     * Load a new empty slot into a container
     */
    private EmptyTileController loadEmptySlotIntoContainer(Pane container)
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
        playButton.setDisable(true); // disable until finished animating.
        SequentialTransition tileTransitions = new SequentialTransition();

        for (LetterTileController control : wordTileControllers)
        {
            this.logger.logMessage(String.format("animated tile %s", control.getModel().getLetter()));
            var translateUp = new TranslateTransition(Duration.seconds(0.1), control.getRoot());
            translateUp.setByY(-10);

            translateUp.setOnFinished(e -> {
                levelModel.addTileToScore(control.getModel());
                this.logger.logMessage("finished tile anim up.");
                this.playersPointsText.setTextFill(Color.GREEN);
            });

            tileTransitions.getChildren().add(translateUp);
            tileTransitions.getChildren().addAll(animatePointScore());
        }

        // After all tiles
        var timeDelay = new PauseTransition(Duration.seconds(1));

        timeDelay.setOnFinished(e ->{
            playButton.setDisable(false);
            levelModel.playTiles();
            checkLevelState();
        });

        tileTransitions.setOnFinished(e -> timeDelay.play());
        tileTransitions.play();
    }

    private List<Animation> animatePointScore()
    {
        var revertSizeTransition = new ScaleTransition(Duration.seconds(0.2), this.playersPointsText);
        revertSizeTransition.setToY(1);
        revertSizeTransition.setToX(1);
        revertSizeTransition.setOnFinished(e -> this.playersPointsText.setTextFill(Color.BLACK));

        var doubleSizeTransition = new ScaleTransition(Duration.seconds(0.2), this.playersPointsText);
        doubleSizeTransition.setToY(2);
        doubleSizeTransition.setToX(2);

        return Arrays.asList(doubleSizeTransition, revertSizeTransition);
    }

    private void checkLevelState()
    {
        if (levelModel.hasWon()) { levelModel.onWonLevel(); }
        else if (levelModel.hasLost()) { levelModel.onLostLevel(); }
    }

    @FXML
    private void onSkipButton(){
        this.logger.logMessage("Skip to shop");
        SceneManager.getInstance().switchScene(GameScenes.SHOP);
    }

    /**
     * redraw button opens and exits redraw window.
     */
    @FXML
    private void onRedrawButton() {
        TranslateTransition redrawWindowSlide = new TranslateTransition(Duration.millis(500), redrawContainer);

        if(!levelModel.getIsRedrawActive()) {
            // slides on screen
            redrawWindowSlide.setToX(-50);
        } else {
            // slides off screen
            levelModel.clearRedrawTiles();
            redrawWindowSlide.setToX(200);
        }
        redrawWindowSlide.play();
        levelModel.toggleRedrawState();
        syncRedrawButton();
    }

    /**
     * Handle redraw confirm button delegated to model.
     */
    @FXML
    private void onConfirmRedrawButton() {
        if (!levelModel.getRedrawRowTilesProperty().isEmpty()){
            levelModel.redrawTiles();
            onRedrawButton();
        }
    }
}