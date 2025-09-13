package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
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
    Label scoreRequiredText;

    @FXML
    Label levelPointsText;

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
        levelModel.playersPointsProperty().addListener((obs, oldVal, newVal) -> syncPlayersPointsProperty(newVal));
        levelModel.currentRedrawsProperty().addListener((obs, oldVal, newVal) -> syncRedrawButton());
        levelModel.currentPlaysProperty().addListener((obs, oldVal, newVal) -> syncPlayButton());

        tileRack = new LetterTileGroup(levelModel.getHandSize(), tileRackContainer,
                levelModel.getTileRackRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton));

        wordRow = new LetterTileGroup(levelModel.getMaxWordSize(), wordViewHBox,
                levelModel.getWordRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton));

        redrawColumn = new LetterTileGroup(levelModel.getRedrawWindowSize(), redrawContainer,
                levelModel.getRedrawRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton, this::syncRedrawButton,this::syncConfirmRedrawButton));

        upgradeGroup = new UpgradeTileGroup(upgradeTilesContainer, levelModel.upgradeTilesProprety());
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");
        scoreRequiredText.setText(String.format("required: %s", levelModel.getHowManyPointsToBeatLevel()));
        levelModel.setupNewLevel();
        levelWonLostText.setText("");

        // sync observable properties.
        syncPlayersPointsProperty(levelModel.playersPointsProperty().get());
        syncPlayButton();
        syncRedrawButton();
        syncConfirmRedrawButton();
    }

    private void syncPlayersPointsProperty(Number newVal)
    {
        this.levelPointsText.setText(String.format("%s", newVal));
    }

    private void syncRedrawButton()
    {
        var redraws = levelModel.currentRedrawsProperty().get();
        redrawButton.setDisable(redraws == 0 || !levelModel.getRedrawRowTilesProperty().isEmpty());
        var buttonText = levelModel.getIsRedrawActive() ? "cancel" : "redraw";
        this.redrawButton.setText(String.format("%s (redraws left: %s)", buttonText, levelModel.currentRedrawsProperty().get()));
    }

    private void syncPlayButton()
    {
        var plays = levelModel.currentPlaysProperty().get();
        playButton.setDisable((plays == 0) || !levelModel.isWordValid() || levelModel.getWordRowTilesProperty().get().isEmpty() || levelModel.getIsRedrawActive());
        this.playButton.setText(String.format("plays left: %s", plays));
    }

    private void syncConfirmRedrawButton(){
        confirmRedrawButton.setDisable(levelModel.getRedrawRowTilesProperty().isEmpty());
    }

    /**
     * Handle tile clicks.
     */
    private void onLetterTileClicked(LetterTileController tileController) {
        boolean moved = levelModel.tryMoveTile(tileController.getModel());
        if (!moved) { this.logger.logMessage("Cannot move tile - no space available or tile not found."); }
    }

    /**
     * Handle play button
     */
    @FXML
    private void onPlayButton()
    {
        levelModel.decreasePlays();
        playButton.setDisable(true);

        var tileScoringSequence = this.animationUtils.createLevelScoreSequence(wordRow.getControllers(),
                levelModel, levelPointsText);
        tileScoringSequence.setOnFinished(e ->
        {
            playButton.setDisable(false);
            levelModel.playTiles();
            checkLevelState();
        });

        tileScoringSequence.play();
    }

    private void checkLevelState()
    {
        if (levelModel.hasWon())
        {
            levelWonLostText.setText("YOU WON!");
            var youWonSequence = this.animationUtils.animateTextEmphasis(levelWonLostText, Color.GREEN, Color.BLACK,
                    Duration.seconds(1));
            youWonSequence.setOnFinished(e -> levelModel.onWonLevel());
            youWonSequence.play();
        }

        else if (levelModel.hasLost()) { levelModel.onLostLevel(); }
    }

    @FXML
    private void onSkipButton() { SceneManager.getInstance().switchScene(GameScenes.SHOP); }

    /**
     * redraw button opens and exits.
     */
    @FXML
    private void onRedrawButton()
    {
        var distance = levelModel.getIsRedrawActive() ? 200 : -50; // slide on if inactive. slide out if active.
        var redrawWindowSlide = animationUtils.slideTransition(Duration.millis(50), redrawContainer ,distance);
        redrawWindowSlide.setOnFinished(e -> levelModel.clearRedrawTiles());
        redrawWindowSlide.play();
        levelModel.toggleRedrawState();
        syncRedrawButton();
    }

    /**
     * Handle redraw confirm button.
     */
    @FXML
    private void onConfirmRedrawButton() {
        if (!levelModel.getRedrawRowTilesProperty().isEmpty()){
            levelModel.redrawTiles();
            onRedrawButton();
        }
    }
}