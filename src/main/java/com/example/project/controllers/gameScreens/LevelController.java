package com.example.project.controllers.gameScreens;

import com.example.project.models.tileGroups.LetterTileGroup;
import com.example.project.models.tileGroups.UpgradeTileGroup;
import com.example.project.controllers.gameScreens.animations.LevelScoreSequence;
import com.example.project.controllers.gameScreens.animations.ScoreTimeline;
import com.example.project.controllers.gameScreens.animations.TextEmphasisAnimation;
import com.example.project.controllers.popupControllers.DefinitionController;
import com.example.project.controllers.tiles.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.popups.DefinitionPopup;
import com.example.project.services.GameScenes;
import com.example.project.services.PopupLoader;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import javafx.animation.*;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import java.util.*;


/**
 * Controller for the level screen.
 */
public class LevelController extends GameScreenController
{
    @FXML Label levelWonLostText;
    @FXML HBox tileRackContainer;
    @FXML HBox wordWindowContainer;
    @FXML HBox upgradeTilesContainer;
    @FXML Button playButton;
    @FXML Button redrawButton;
    @FXML Label scoreToBeatLabel;
    @FXML Label currentScoreLabel;
    @FXML Label comboCountLabel;
    @FXML Label comboMultiplierLabel;
    @FXML VBox redrawContainer;
    @FXML Button confirmRedrawButton;
    @FXML private StackPane root;
    @FXML private Label playsLeftLabel;
    @FXML private Label redrawsLeftLabel;
    @FXML private StackPane definitionContainer;

    private static LevelModel levelModel;
    private DefinitionPopup definitionPopup = new DefinitionPopup();
    private DefinitionController definitionController;
    private UpgradeTileGroup upgradeGroup;
    private LetterTileGroup tileRack;
    private LetterTileGroup wordTilesRow;
    private LetterTileGroup redrawTilesColumn;

    /**
     * Constructor only called once each time application opened.
     */
    public LevelController()
    {
        super();
        levelModel = new LevelModel(Session.getInstance());
        Session.getInstance().setLevelModel(levelModel);
    }

    protected LevelController(LevelModel model) { levelModel = model; }

    /**
     * This runs after the constructor and after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    private Label moneyLabel; //Label component for displaying the players current money, this should automatically update through data binding to the Session money property

    /**
     * @see Session#getMoneyProperty() for the money binding
     */
    @FXML
    public void initialize()
    {
                // Binds the money display to Session money property for automatic updates
        moneyLabel.textProperty().bind(
                Session.getInstance().getMoneyProperty().asString("Money: $%d")
        );

        // Setup Listeners. (automatically updates each property when they're changed)
        levelModel.getPlayersTotalPoints().addListener((obs, oldVal, newVal) -> syncTotalScoreProperty(newVal));
        levelModel.wordPointsProperty().addListener((obs, oldVal, newVal) -> syncwordPointsProperty(newVal));
        levelModel.wordMultiProperty().addListener((obs, oldVal, newVal) -> syncwordMultiProperty(newVal));
        levelModel.getCurrentRedraws().addListener((obs, oldVal, newVal) -> syncRedrawButton());
        levelModel.getCurrentPlays().addListener((obs, oldVal, newVal) -> syncPlayButton());
        levelModel.getIsRedrawActive().addListener((obs, oldVal, newVal) -> syncRedrawWindow(newVal));
        definitionPopup.getIsDefinitionActive().addListener((obs, oldVal, newVal) -> syncDefinitionWindow(newVal));

        tileRack = new LetterTileGroup(levelModel.getHandSize(), tileRackContainer,
                levelModel.getTileRackTilesProperty(), this::onLetterTileClicked);

        wordTilesRow = new LetterTileGroup(levelModel.getWordWindowSize(), wordWindowContainer,
                levelModel.getWordWindowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton));


        redrawTilesColumn = new LetterTileGroup(levelModel.getRedrawWindowSize(), redrawContainer,
                levelModel.getRedrawWindowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncRedrawButton,this::syncConfirmRedrawButton));

        setupDefinitionPopup();
        upgradeGroup = new UpgradeTileGroup(upgradeTilesContainer, levelModel.getUpgradeTilesProperty());

        tileRack.syncTiles();
        wordTilesRow.syncTiles();
        redrawTilesColumn.syncTiles();
        upgradeGroup.syncTiles();
    }

    @Override
    public void onSceneChangedToThis()
    {
        levelModel.setupNewLevel();
        this.logger.logMessage("level page loaded.");
        scoreToBeatLabel.setText(String.format("required: %s", levelModel.getLevelRequirement()));
        levelModel.setupNewLevel();
        levelWonLostText.setText("");

        // sync observable properties.
        syncwordPointsProperty(levelModel.wordPointsProperty().get());
        syncwordMultiProperty(levelModel.wordMultiProperty().get());
        syncTotalScoreProperty(levelModel.getPlayersTotalPoints().get());
        syncPlayButton();
        syncRedrawButton();
        syncConfirmRedrawButton();
    }

    private void setupDefinitionPopup() {
        this.definitionController = PopupLoader.createDefinitionPopup(definitionPopup);

        definitionContainer.setMouseTransparent(true);
        definitionContainer.setVisible(false);

        var popupRoot = definitionController.getStack();
        definitionContainer.getChildren().add(popupRoot);

        popupRoot.managedProperty().bind(definitionPopup.getIsDefinitionActive());
        definitionPopup.setIsDefinitionActive(false);

        // Listener for auto hiding the popup when clicking the container area outside the popup's content
        definitionContainer.setOnMouseClicked(event -> {
            if (event.getTarget() == definitionContainer) {
                definitionPopup.setIsDefinitionActive(false);
            }
            event.consume();
        });

        // Listener for changing cursor appearance anywhere on screen
        definitionPopup.getIsDefinitionActive().addListener((activeObs, wasActive, isActive) -> {
            if (isActive) {
                definitionContainer.setCursor(Cursor.HAND);
            } else {
                definitionContainer.setCursor(Cursor.DEFAULT);
                this.syncLevelWonText();
            }
        });
    }

    private void syncRedrawWindow(boolean isRedrawActive)
    {
        var distance = isRedrawActive ? -50 : 200; // slide on if inactive. slide out if active.
        TranslateTransition redrawWindowSlide = new TranslateTransition(Duration.millis(500), redrawContainer);
        redrawWindowSlide.setToX(distance);
        redrawWindowSlide.play();
        syncRedrawButton();
    }

    private void syncDefinitionWindow(boolean isDefinitionActive){
        if (isDefinitionActive) {
            definitionContainer.setVisible(true);
            definitionContainer.setMouseTransparent(false);
        }
        var distance = isDefinitionActive ? 300 : 1000; // slide on if inactive. slide out if active.
        TranslateTransition definitionWindowSlide = new TranslateTransition(Duration.millis(500), definitionContainer);
        definitionWindowSlide.setToX(distance);
        definitionWindowSlide.setOnFinished(e ->
        {
            if (!isDefinitionActive) {
                definitionContainer.setVisible(false);
            }
        });
        definitionWindowSlide.play();
    }

    private void syncwordPointsProperty(Number newVal)
    {
        this.comboCountLabel.setText(String.format("%s", newVal));
    }

    private void syncwordMultiProperty(Number newVal)
    {
        this.comboMultiplierLabel.setText(String.format("%s", newVal));
    }

    private void syncTotalScoreProperty(Number newVal)
    {
        this.currentScoreLabel.setText(String.format("%s", newVal));
    }

    private void syncRedrawButton()
    {
        var redraws = levelModel.getCurrentRedraws().get();
        redrawButton.setDisable(redraws == 0);
        var buttonText = levelModel.getIsRedrawActive().get() ? "cancel" : "redraw";
        this.redrawButton.setText(String.format("%s (redraws left: %s)", buttonText, levelModel.getCurrentRedraws().get()));
        redrawsLeftLabel.setText(String.valueOf(redraws));
        redrawButton.setText(levelModel.getIsRedrawActive().get() ? "⟳ cancel" : "⟳");
    }

    private void syncPlayButton()
    {
        var plays = levelModel.getCurrentPlays().get();
        playButton.setDisable((plays == 0) || !levelModel.isCurrentWordValid() || levelModel.getWordWindowTilesProperty().isEmpty() || levelModel.getIsRedrawActive().get());
        this.playButton.setText(String.format("plays left: %s", plays));
        playsLeftLabel.setText(String.valueOf(plays));
        playButton.setText("▶");
    }

    private void syncConfirmRedrawButton(){
        confirmRedrawButton.setDisable(levelModel.getRedrawWindowTilesProperty().isEmpty());
    }

    /**
     * Handle tile clicks.
     */
    private void onLetterTileClicked(LetterTileController tileController)
    {
        boolean moved = levelModel.tryMoveTile(tileController.getModel());
        if (!moved) { this.logger.logMessage("Cannot move tile - no space available or tile not found."); }
    }

    /**
     * Handle play button
     */
    @FXML
    protected void onPlayButton()
    {
        definitionPopup.setPopup(levelModel.getCurrentWord());
        playButton.setDisable(true);
        int startScore = levelModel.getPlayersTotalPoints().get();
        var tileScoringSequence = new LevelScoreSequence(wordTilesRow.getControllers(), levelModel, comboCountLabel, comboMultiplierLabel);
        tileScoringSequence.setOnFinished(e ->
        {
            // Capturing scores before upgrades run
            int preUpgradePoints = levelModel.wordPointsProperty().get();
            int preUpgradeMulti = levelModel.wordMultiProperty().get();

            int endScore = startScore + levelModel.calcTotalWordScore(); // Upgrades run here

            // Capturing scores after upgrades run
            int postUpgradePoints = levelModel.wordPointsProperty().get();
            int postUpgradeMulti = levelModel.wordMultiProperty().get();

            // Check if any upgrade changed the points or multiplier
            boolean pointsChanged = preUpgradePoints != postUpgradePoints;
            boolean multiChanged = preUpgradeMulti != postUpgradeMulti;
            boolean upgradesApplied = pointsChanged || multiChanged;

            SequentialTransition upgradeSequence = new SequentialTransition();

            if (upgradesApplied) {
                if (pointsChanged) {
                    TextEmphasisAnimation pointsEmphasis = new TextEmphasisAnimation(comboCountLabel, Color.BLUE, Color.WHITE, Duration.seconds(0));
                    upgradeSequence.getChildren().addAll(pointsEmphasis.getChildren());
                    upgradeSequence.getChildren().add(new javafx.animation.PauseTransition(Duration.millis(1)));
                }
                if (multiChanged) {
                    TextEmphasisAnimation multiEmphasis = new TextEmphasisAnimation(comboMultiplierLabel, Color.RED, Color.WHITE, Duration.seconds(0));
                    upgradeSequence.getChildren().addAll(multiEmphasis.getChildren());
                }

                upgradeSequence.setOnFinished(g -> {
                    levelModel.getTileScoreSoundPlayer().playNextNote();
                    runTotalScoreAnimation(startScore, endScore);
                });

                upgradeSequence.play();
            } else {
                // If no upgrades changed the score, skip the upgrade animation and run total score immediately
                runTotalScoreAnimation(startScore, endScore);
            }
        });

        tileScoringSequence.play();
    }

    private void runTotalScoreAnimation(int startScore, int endScore) {
        ScoreTimeline totalScoreTimeline = new ScoreTimeline();
        Timeline timeline = totalScoreTimeline.animateTotalScore(startScore, endScore, currentScoreLabel, 1000);
        timeline.setOnFinished(f ->
        {
            TextEmphasisAnimation scoreEmphasis = new TextEmphasisAnimation(currentScoreLabel, Color.GREEN, Color.BLACK, Duration.seconds(0));
            scoreEmphasis.play();
            levelModel.getTileScoreSoundPlayer().playNextNote();
            playButton.setDisable(false);
            levelModel.playTiles();
            levelModel.resetCombo();
            levelModel.setTotalScore(endScore);
            levelModel.getTileScoreSoundPlayer().reset();
            definitionPopup.setIsDefinitionActive(true);
        });
        timeline.play();
    }

    private void syncLevelWonText()
    {
        if (levelModel.hasWon()){
            levelWonLostText.setText("YOU WON!");
            TextEmphasisAnimation youWonSequence = new TextEmphasisAnimation(levelWonLostText, Color.GREEN, Color.BLACK, Duration.seconds(1));
            youWonSequence.setOnFinished(e -> levelModel.onWonLevel());
            youWonSequence.play();
        }
        else if (levelModel.hasLost()){
            levelWonLostText.setText("You Lost");
            TextEmphasisAnimation animSequence =
                    new TextEmphasisAnimation(levelWonLostText, Color.RED, Color.BLACK, Duration.seconds(1));
            animSequence.setOnFinished(e -> levelModel.onLostLevel());
            animSequence.play();
        }
        else{
            levelWonLostText.setText("");
        }
    }

    @FXML
    protected void onSkipButton() { SceneManager.getInstance().switchScene(GameScenes.SHOP); }

    /**
     * redraw button opens or cancels the redraw.
     */
    @FXML
    protected void onRedrawButton()
    {
        levelModel.setIsRedrawActive(!levelModel.getIsRedrawActive().get());
        levelModel.returnRedrawTilesToTheRack();
    }

    /**
     * Handle redraw confirm button.
     */
    @FXML
    protected void onConfirmRedrawButton() {
        levelModel.setIsRedrawActive(!levelModel.getIsRedrawActive().get());
        levelModel.redrawTiles();
    }
}