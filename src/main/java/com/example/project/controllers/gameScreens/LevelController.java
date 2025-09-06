package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.EmptyTileController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.TileLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

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
        initializeView();
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
        updatePlayRedrawButtons();
    }

    /**
     * Initialize the view with empty slots and initial tiles
     */
    private void initializeView() {
        createEmptySlots();
        createLetterTileControllers();
        loadUpgradeTiles();

        // Initial view update
        onModelChanged();
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
            LetterTileController letterController = TileLoader.createTileController(tile);

            // Set up click handler
            letterController.getRoot().setOnMouseClicked(e -> onLetterTileClicked(letterController, tile));

            // Store mapping
            tileControllerMap.put(tile, letterController);
        }
    }

    /**
     * Load upgrade tiles
     */
    private void loadUpgradeTiles() {
        for (UpgradeTile upgradeTile : levelModel.getUpgrades()) {
            UpgradeTileViewController newUpgrade = TileLoader.createTileController(upgradeTile);
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
        EmptyTileController controller = TileLoader.createTileController(emptyTile);
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