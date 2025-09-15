package com.example.project.controllers.gameScreens;

import com.example.project.controllers.gameScreens.animations.LevelScoreSequence;
import com.example.project.controllers.gameScreens.animations.TextEmphasisAnimation;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.TileLoader;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the level screen.
 */
public class LevelController extends GameScreenController {

    @FXML
    private Label levelWonLostText;

    @FXML
    private HBox tileRackContainer;

    @FXML
    private HBox wordViewHBox;

    @FXML
    private HBox upgradeTilesContainer;

    @FXML
    private Button playButton;

    @FXML
    private Button redrawButton;

    @FXML
    private Label scoreRequiredText;

    @FXML
    private Label levelPointsText;

    @FXML
    private VBox redrawContainer;

    @FXML
    private Button confirmRedrawButton;

    @FXML private Label scoreToBeatLabel;
    @FXML private Label currentScoreLabel;
    @FXML private Label comboCountLabel;
    @FXML private Label comboMultiplierLabel;
    @FXML private Label playsLeftLabel;
    @FXML private Label redrawsLeftLabel;

    @FXML private StackPane rootPane;
    @FXML private ImageView tileStandImage;
    @FXML private ImageView tileRackImage;
    @FXML private ImageView tileRackRectangleImage;
    @FXML private ImageView backgroundImage;
    @FXML private StackPane gameStack;
    @FXML private VBox sidebar;

    // Word labels
    @FXML private Label scoreHeaderLabel;
    @FXML private Label yourScoreHeaderLabel;
    @FXML private Label comboHeaderLabel;

    private static LevelModel levelModel;
    private UpgradeTileGroup upgradeGroup;
    private LetterTileGroup tileRackGroup;
    private LetterTileGroup wordRow;
    private LetterTileGroup redrawColumn;

    private final Map<LetterTile, LetterTileController> tileControllerMap = new HashMap<>();

    /**
     * Constructor only called once each time application opened.
     */
    public LevelController() {
        super();
        levelModel = new LevelModel(Session.getInstance());
    }

    /**
     * This runs after the constructor and after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    public void initialize() {
        // Setup Listeners. (automatically updates each property when they're changed)
        levelModel.getLevelPointsProperty().addListener((obs, oldVal, newVal) -> syncPlayersPointsProperty(newVal));
        levelModel.getCurrentRedrawsProperty().addListener((obs, oldVal, newVal) -> syncRedrawButton());
        levelModel.getCurrentPlaysProperty().addListener((obs, oldVal, newVal) -> syncPlayButton());

        tileRackGroup = new LetterTileGroup(levelModel.getHandSize(), tileRackContainer,
                levelModel.getTileRackRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton));

        wordRow = new LetterTileGroup(levelModel.getMaxWordSize(), wordViewHBox,
                levelModel.getWordRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton));

        redrawColumn = new LetterTileGroup(levelModel.getRedrawWindowSize(), redrawContainer,
                levelModel.getRedrawRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton, this::syncConfirmRedrawButton));

//        upgradeGroup = new UpgradeTileGroup(upgradeTilesContainer, levelModel.getUpgradeTilesProprety());

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
    public void onSceneChangedToThis() {
        this.logger.logMessage("level page loaded.");
        scoreRequiredText.setText(String.format("required: %s", levelModel.getHowManyPointsToBeatLevel()));
        levelModel.setupNewLevel();
        levelWonLostText.setText("");

        // sync observable properties.
        syncPlayersPointsProperty(levelModel.getLevelPointsProperty().get());
        syncPlayButton();
        syncRedrawButton();
        syncConfirmRedrawButton();
    }

    private void syncPlayersPointsProperty(Number newVal) {
        this.levelPointsText.setText(String.format("%s", newVal));
    }

    /**
     * Create tile controllers for all letter tiles
     */
    private void createLetterTileControllers() {
        for (LetterTile tile : levelModel.getTileRackRowTilesProperty().get()) {
            // Create controller + root
            LetterTileController letterController = TileLoader.createLetterTile(tile);
            StackPane tileRoot = (StackPane) letterController.getRoot(); // root of LetterTile.fxml

            // 1. Bind all size constraints to window (3.5% of gameStack width)
            tileRoot.minWidthProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.minHeightProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.prefWidthProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.prefHeightProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.maxWidthProperty().bind(gameStack.widthProperty().multiply(0.035));
            tileRoot.maxHeightProperty().bind(gameStack.widthProperty().multiply(0.035));

            // 2. Background follows tile size
            Rectangle background = (Rectangle) tileRoot.lookup("#theBackground");
            if (background != null) {
                background.setManaged(false);
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
            tileRoot.setOnMouseClicked(e -> onLetterTileClicked(letterController));

            // 5. Add to map
            tileControllerMap.put(tile, letterController);

            gameStack.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                        if (newWin instanceof javafx.stage.Stage stage) {
                            stage.fullScreenProperty().addListener((obsFull, wasFull, isFull) -> {
                                double scale = isFull ? 1.2 : 0.9; // bigger in fullscreen
                                tileControllerMap.values().forEach(c -> c.animateScale(scale));
                            });
                        }
                    });
                }
            });
        }
    }

    /**
     * Load upgrade tiles
     */
    private void loadUpgradeTiles() {
        for (UpgradeTile upgradeTile : levelModel.getUpgradeTilesProprety().get()) {
            UpgradeTileViewController newUpgrade = TileLoader.createUpgradeTile(upgradeTile);
            upgradeTilesContainer.getChildren().add(newUpgrade.getRoot());
        }
    }

    private void syncRedrawButton() {
        var redraws = levelModel.getCurrentRedrawsProperty().get();
        redrawButton.setDisable(redraws == 0);
        var buttonText = levelModel.getIsRedrawActive() ? "cancel" : "redraw";
        this.redrawButton.setText(String.format("%s (redraws left: %s)", buttonText, redraws));
    }

    private void syncPlayButton() {
        var plays = levelModel.getCurrentPlaysProperty().get();
        playButton.setDisable((plays == 0) || !levelModel.isWordValid()
                || levelModel.getWordRowTilesProperty().get().isEmpty()
                || levelModel.getIsRedrawActive());
        this.playButton.setText(String.format("plays left: %s", plays));
    }

    private void syncConfirmRedrawButton() {
        confirmRedrawButton.setDisable(levelModel.getRedrawRowTilesProperty().get().isEmpty());
    }

    /**
     * Handle tile clicks.
     */
    private void onLetterTileClicked(LetterTileController tileController) {
        boolean moved = levelModel.tryMoveTile(tileController.getModel());
        if (!moved) {
            this.logger.logMessage("Cannot move tile - no space available or tile not found.");
        }
    }

    /**
     * Handle play button
     */
    @FXML
    private void onPlayButton() {
        playButton.setDisable(true);

        var tileScoringSequence = new LevelScoreSequence(wordRow.getControllers(),
                levelModel, levelPointsText);

        tileScoringSequence.setOnFinished(e -> {
            playButton.setDisable(false);
            levelModel.playTiles();
            checkLevelState();
        });

        tileScoringSequence.play();
    }

    private void checkLevelState() {
        if (levelModel.hasWon()) {
            levelWonLostText.setText("YOU WON!");
            TextEmphasisAnimation youWonSequence =
                    new TextEmphasisAnimation(levelWonLostText, Color.GREEN, Color.BLACK, Duration.seconds(1));
            youWonSequence.setOnFinished(e -> levelModel.onWonLevel());
            youWonSequence.play();
        } else if (levelModel.hasLost()) {
            levelModel.onLostLevel();
        }
    }

    @FXML
    private void onSkipButton() {
        SceneManager.getInstance().switchScene(GameScenes.SHOP);
    }

    /**
     * redraw button opens or cancels the redraw.
     */
    @FXML
    private void onRedrawButton() {
        toggleRedrawWindow(e -> levelModel.returnRedrawTilesToTheRack());
    }

    private void toggleRedrawWindow(EventHandler<ActionEvent> var1) {
        var distance = levelModel.getIsRedrawActive() ? 200 : -50;
        TranslateTransition redrawWindowSlide = new TranslateTransition(Duration.millis(500), redrawContainer);
        redrawWindowSlide.setToX(distance);
        redrawWindowSlide.setOnFinished(var1);
        redrawWindowSlide.play();
        syncRedrawButton();
        levelModel.toggleRedrawState();
    }

    /**
     * Handle redraw confirm button.
     */
    @FXML
    private void onConfirmRedrawButton() {
        toggleRedrawWindow(e -> levelModel.redrawTiles());
    }
}