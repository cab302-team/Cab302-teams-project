package com.example.project.controllers.gameScreens;

import com.example.project.models.TileGroups.LetterTileGroup;
import com.example.project.models.TileGroups.UpgradeTileGroup;
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
import javafx.fxml.FXML;
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
    @FXML HBox wordViewHBox;
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

        tileRack = new LetterTileGroup(levelModel.getHandSize(), tileRackContainer,
                levelModel.getTileRackRowTilesProperty(), this::onLetterTileClicked);

        wordRow = new LetterTileGroup(levelModel.getMaxWordSize(), wordViewHBox,
                levelModel.getWordRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton));

        redrawColumn = new LetterTileGroup(levelModel.getRedrawWindowSize(), redrawContainer,
                levelModel.getRedrawRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncRedrawButton,this::syncConfirmRedrawButton));

        upgradeGroup = new UpgradeTileGroup(upgradeTilesContainer, levelModel.getUpgradeTilesProperty());

        tileRack.syncTiles();
        wordRow.syncTiles();
        redrawColumn.syncTiles();
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

    private void syncRedrawWindow(boolean isRedrawActive)
    {
        var distance = isRedrawActive ? -50 : 200; // slide on if inactive. slide out if active.
        TranslateTransition redrawWindowSlide = new TranslateTransition(Duration.millis(500), redrawContainer);
        redrawWindowSlide.setToX(distance);
        redrawWindowSlide.play();
        syncRedrawButton();
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
        playButton.setDisable((plays == 0) || !levelModel.isCurrentWordValid() || levelModel.getWordRowTilesProperty().isEmpty() || levelModel.getIsRedrawActive().get());
        this.playButton.setText(String.format("plays left: %s", plays));
        playsLeftLabel.setText(String.valueOf(plays));
        playButton.setText("▶");
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
    protected void onPlayButton()
    {
        playButton.setDisable(true);
        int startScore = levelModel.getPlayersTotalPoints().get();
        var tileScoringSequence = new LevelScoreSequence(wordRow.getControllers(), levelModel, comboCountLabel, comboMultiplierLabel);
        tileScoringSequence.setOnFinished(e ->
        {
            int endScore = startScore + levelModel.calcTotalWordScore();

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
                levelModel.getTileScoreSoundPlayer().reset();
                this.syncLevelWonText();
            });
            timeline.play();
        });

        tileScoringSequence.play();
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