package com.example.project.controllers.gameScreens;

import com.example.project.controllers.gameScreens.animations.LevelScoreSequence;
import com.example.project.controllers.gameScreens.animations.ScoreTimeline;
import com.example.project.controllers.gameScreens.animations.TextEmphasisAnimation;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * Controller for the level screen.
 */
public class LevelController extends GameScreenController
{
    @FXML
    Label levelWonLostText;

    @FXML
    HBox tileRackContainer;

    @FXML
    HBox wordViewHBox;

    @FXML
    HBox upgradeTilesContainer;

    @FXML
    Button playButton;

    @FXML
    Button redrawButton;

    @FXML
    Label scoreToBeatLabel;

    @FXML
    Label currentScoreLabel;

    @FXML
    Label comboCountLabel;

    @FXML
    Label comboMultiplierLabel;

    @FXML
    VBox redrawContainer;

    @FXML
    Button confirmRedrawButton;

    private static LevelModel levelModel;
    private UpgradeTileGroup upgradeGroup;
    private LetterTileGroup tileRack;
    private LetterTileGroup wordRow;
    private LetterTileGroup redrawColumn;

    /**
     * Constructor only called once each time application opened.
     */
    public LevelController()
    {
        super();
        levelModel = new LevelModel(Session.getInstance());
    }

    /**
     * This runs after the constructor and after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    public void initialize()
    {
        // Setup Listeners. (automatically updates each property when they're changed)
        levelModel.totalPointsProperty().addListener((obs, oldVal, newVal) -> syncTotalScoreProperty(newVal));
        levelModel.sumComboProperty().addListener((obs, oldVal, newVal) -> syncSumComboProperty(newVal));
        levelModel.multiComboProperty().addListener((obs, oldVal, newVal) -> syncMultiComboProperty(newVal));
        levelModel.getCurrentRedrawsProperty().addListener((obs, oldVal, newVal) -> syncRedrawButton());
        levelModel.getCurrentPlaysProperty().addListener((obs, oldVal, newVal) -> syncPlayButton());

        tileRack = new LetterTileGroup(levelModel.getHandSize(), tileRackContainer,
                levelModel.getTileRackRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton));

        wordRow = new LetterTileGroup(levelModel.getMaxWordSize(), wordViewHBox,
                levelModel.getWordRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton));

        redrawColumn = new LetterTileGroup(levelModel.getRedrawWindowSize(), redrawContainer,
                levelModel.getRedrawRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton,this::syncConfirmRedrawButton));

        upgradeGroup = new UpgradeTileGroup(upgradeTilesContainer, levelModel.getUpgradeTilesProprety());
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");
        scoreToBeatLabel.setText(String.format("required: %s", levelModel.getHowManyPointsToBeatLevel()));
        levelModel.setupNewLevel();
        levelWonLostText.setText("");

        // sync observable properties.
        syncSumComboProperty(levelModel.sumComboProperty().get());
        syncMultiComboProperty(levelModel.multiComboProperty().get());
        syncTotalScoreProperty(levelModel.totalPointsProperty().get());
        syncPlayButton();
        syncRedrawButton();
        syncConfirmRedrawButton();
    }

    private void syncSumComboProperty(Number newVal)
    {
        this.comboCountLabel.setText(String.format("%s", newVal));
    }

    private void syncMultiComboProperty(Number newVal)
    {
        this.comboMultiplierLabel.setText(String.format("%s", newVal));
    }

    private void syncTotalScoreProperty(Number newVal)
    {
        this.currentScoreLabel.setText(String.format("%s", newVal));
    }

    private void syncRedrawButton()
    {
        var redraws = levelModel.getCurrentRedrawsProperty().get();
        redrawButton.setDisable(redraws == 0);
        var buttonText = levelModel.getIsRedrawActive() ? "cancel" : "redraw";
        this.redrawButton.setText(String.format("%s (redraws left: %s)", buttonText, levelModel.getCurrentRedrawsProperty().get()));
    }

    private void syncPlayButton()
    {
        var plays = levelModel.getCurrentPlaysProperty().get();
        playButton.setDisable((plays == 0) || !levelModel.isWordValid() || levelModel.getWordRowTilesProperty().get().isEmpty() || levelModel.getIsRedrawActive());
        this.playButton.setText(String.format("plays left: %s", plays));
    }

    private void syncConfirmRedrawButton(){
        confirmRedrawButton.setDisable(levelModel.getRedrawRowTilesProperty().isEmpty());
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
//    private void onPlayButton()
//    {
//        playButton.setDisable(true);
//
//        var tileScoringSequence = new LevelScoreSequence(wordRow.getControllers(),
//                levelModel, levelPointsText);
//
//        tileScoringSequence.setOnFinished(e ->
//        {
//            playButton.setDisable(false);
//            levelModel.playTiles();
//            checkLevelState();
//        });
//
//        tileScoringSequence.play();
//    }
    private void onPlayButton()
    {
        playButton.setDisable(true);
        int startScore = levelModel.totalPointsProperty().get();
        var tileScoringSequence = new LevelScoreSequence(wordRow.getControllers(), levelModel, comboCountLabel, comboMultiplierLabel);
        tileScoringSequence.setOnFinished(e ->
        {
            int endScore = startScore + levelModel.calcTotalScore();

            ScoreTimeline totalScoreTimeline = new ScoreTimeline();
            Timeline timeline = totalScoreTimeline.animateTotalScore(startScore, endScore, currentScoreLabel);
            timeline.setOnFinished(f ->
            {
                TextEmphasisAnimation scoreEmphasis = new TextEmphasisAnimation(currentScoreLabel, Color.GREEN, Color.BLACK, Duration.seconds(0));
                scoreEmphasis.play();
                playButton.setDisable(false);
                levelModel.playTiles();
                levelModel.resetCombo();
                levelModel.setTotalScore(endScore);
                checkLevelState();
            });
            timeline.play();
        });
        tileScoringSequence.play();
    }


    private void checkLevelState()
    {
        if (levelModel.hasWon())
        {
            levelWonLostText.setText("YOU WON!");
            TextEmphasisAnimation youWonSequence = new TextEmphasisAnimation(levelWonLostText, Color.GREEN, Color.BLACK, Duration.seconds(1));
            youWonSequence.setOnFinished(e -> levelModel.onWonLevel());
            youWonSequence.play();
        }

        else if (levelModel.hasLost()) { levelModel.onLostLevel(); }
    }

    @FXML
    private void onSkipButton() { SceneManager.getInstance().switchScene(GameScenes.SHOP); }

    /**
     * redraw button opens or cancels the redraw.
     */
    @FXML
    private void onRedrawButton()
    {
        toggleRedrawWindow(e -> levelModel.returnRedrawTilesToTheRack());
    }

    private void toggleRedrawWindow(EventHandler<ActionEvent> var1)
    {
        var distance = levelModel.getIsRedrawActive() ? 200 : -50; // slide on if inactive. slide out if active.
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
    private void onConfirmRedrawButton()
    {
        toggleRedrawWindow(e -> levelModel.redrawTiles());
    }
}