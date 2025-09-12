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
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @FXML
    VBox redrawContainer;

    private static LevelModel levelModel;

    private final List<EmptyTileController> wordWindowTileSlots = new ArrayList<>();
    private final List<EmptyTileController> tileRackTileSlots = new ArrayList<>();
    private final List<EmptyTileController> redrawWindowTileSlots = new ArrayList<>();
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
        // TODO: adding this currently breaks it updateTileRow(redrawWindowTileSlots, levelModel.getRedrawRowTiles());
        updatePlayButton();
        updateTileRow(redrawWindowTileSlots, levelModel.getRedrawRowTiles());

        syncRedrawButtonText();

        // sync upgrade tiles.
        upgradeTileRackAtTop.getChildren().clear();
        upgradeTiles.clear();
        loadUpgradeTiles();
    }

    /**
     * Sync redraw button text.
     */
    public void syncRedrawButtonText()
    {
        if (!levelModel.isRedrawActive()){
            redrawButton.setText("redraw");
        }
        else {
            if (levelModel.getRedrawRowTiles().isEmpty()) {
                redrawButton.setText("cancel");
            }
            else{
                redrawButton.setText("confirm");
            }
        }

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

    private void updatePlayButton()
    {
        playButton.setDisable(levelModel.getWordRowTiles().isEmpty() || !levelModel.isWordValid());
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
    private void onPlayButton() {
        // TODO: add scoring and getting new tiles after playing.
        this.logger.logMessage("TODO: play button things.");
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
    private void onRedrawButton()
    {
        TranslateTransition redrawWindowSlideIn = new TranslateTransition(Duration.millis(500), redrawContainer);

        var slideIn = -50;
        var slideOut = 200;
        // if open, slide out. if out slide in.
        var newWindowPos = levelModel.isRedrawActive() ? slideOut : slideIn;
        redrawWindowSlideIn.setToX(newWindowPos);

        redrawWindowSlideIn.setOnFinished(e ->
        {
            levelModel.redrawTiles();
            levelModel.toggleRedrawState();
        });

        redrawWindowSlideIn.play();
    }
}