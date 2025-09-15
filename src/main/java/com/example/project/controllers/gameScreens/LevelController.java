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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Label;

//import javax.swing.text.html.ImageView;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

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

    @FXML
    Button confirmRedrawButton;

    @FXML private Label scoreToBeatLabel;
    @FXML private Label currentScoreLabel;
    @FXML private Label comboCountLabel;
    @FXML private Label comboMultiplierLabel;
    @FXML private Label playsLeftLabel;
    @FXML private Label redrawsLeftLabel;

    @FXML private StackPane rootPane;
    @FXML private ImageView tileStand;
    @FXML private ImageView tileRack;
    @FXML private ImageView tileRackRectangle;
    @FXML private ImageView backgroundImage;
    @FXML private StackPane gameStack;
    @FXML private VBox sidebar;
    @FXML private ImageView tileStandImage;
    @FXML private ImageView tileRackImage;
    @FXML private ImageView tileRackRectangleImage;

    // Word labels
    @FXML private Label scoreHeaderLabel;
    @FXML private Label yourScoreHeaderLabel;
    @FXML private Label comboHeaderLabel;


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

        // Tile stand scales to 50% of window width
        tileStandImage.fitWidthProperty().bind(gameStack.widthProperty().multiply(0.45));
        tileStandImage.setPreserveRatio(true);

        // Tile rack scales to 45% of window width
        tileRackImage.fitWidthProperty().bind(gameStack.widthProperty().multiply(0.40));
        tileRackImage.setPreserveRatio(true);

        // Tile rack rectangle = same as rack
        tileRackRectangleImage.fitWidthProperty().bind(gameStack.widthProperty().multiply(0.40));
        tileRackRectangleImage.setPreserveRatio(true);

        gameStack.widthProperty().addListener((obs, oldVal, newVal) -> {
            double scale = newVal.doubleValue() / 1920.0; // base width = 1920

            // Scale numbers
            scoreToBeatLabel.setStyle("-fx-font-size: " + (28 * scale) + "px;");
            currentScoreLabel.setStyle("-fx-font-size: " + (28 * scale) + "px;");
            comboCountLabel.setStyle("-fx-font-size: " + (24 * scale) + "px;");
            comboMultiplierLabel.setStyle("-fx-font-size: " + (24 * scale) + "px;");

            // Scale words
            scoreHeaderLabel.setStyle("-fx-font-size: " + (22 * scale) + "px;");
            yourScoreHeaderLabel.setStyle("-fx-font-size: " + (22 * scale) + "px;");
            comboHeaderLabel.setStyle("-fx-font-size: " + (22 * scale) + "px;");

            // Optional: scale spacing too
            sidebar.setSpacing(20 * scale);
        });

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
        updateTileRow(redrawWindowTileSlots, levelModel.getRedrawRowTiles());
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
        for (LetterTile tile : levelModel.getLetterTiles()) {
            // Create controller + root
            LetterTileController letterController = TileLoader.createLetterTile(tile);
            StackPane tileRoot = (StackPane) letterController.getRoot(); // root of LetterTile.fxml

            // 1. Bind all size constraints to window (5% of gameStack width)
            tileRoot.minWidthProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.minHeightProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.prefWidthProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.prefHeightProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.maxWidthProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.maxHeightProperty().bind(gameStack.widthProperty().multiply(0.035));

            // 2. Background follows tile size
            Rectangle background = (Rectangle) tileRoot.lookup("#theBackground");
            if (background != null) {
                background.setManaged(false); // don’t let it affect parent size
                background.widthProperty().bind(tileRoot.widthProperty());
                background.heightProperty().bind(tileRoot.heightProperty());
            }

            // 3. Scale fonts relative to tile size (base = 80px)
            Label letterLabel = (Label) tileRoot.lookup("#letterLabel");
            Label valueLabel = (Label) tileRoot.lookup("#valueLabel");

            tileRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
                double scale = newVal.doubleValue() / 80.0;
                if (letterLabel != null) {
                    letterLabel.setStyle("-fx-font-size: " + (28 * scale) + "px;");
                }
                if (valueLabel != null) {
                    valueLabel.setStyle("-fx-font-size: " + (16 * scale) + "px;");
                }
            });

            // 4. Click handler
            tileRoot.setOnMouseClicked(e -> onLetterTileClicked(letterController, tile));

            // 5. Add to map
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
        confirmRedrawButton.setDisable(levelModel.getRedrawRowTiles().isEmpty());
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
    private void onRedrawButton() {
        TranslateTransition redrawWindowSlide = new TranslateTransition(Duration.millis(500), redrawContainer);

        if(!levelModel.isRedrawActive()) {
            // slides on screen
            redrawWindowSlide.setToX(-50);
            redrawButton.setText("Cancel Redraw");
        } else {
            // slides off screen
            levelModel.clearRedrawTiles();
            redrawWindowSlide.setToX(200);
            redrawButton.setText("Redraw");
        }
        redrawWindowSlide.play();
        levelModel.toggleRedrawState();
    }

    /**
     * Handle redraw confirm button delegated to model.
     */
    @FXML
    private void onConfirmRedrawButton() {
        // View updates automatically via observer pattern
        if(!levelModel.getRedrawRowTiles().isEmpty()){
            levelModel.redrawTiles();
            onRedrawButton();
        }
    }
}