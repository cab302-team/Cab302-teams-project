package com.example.project.controllers.gameScreens;

import com.example.project.controllers.gameScreens.animations.LevelScoreSequence;
import com.example.project.controllers.gameScreens.animations.ScoreTimeline;
import com.example.project.controllers.gameScreens.animations.TextEmphasisAnimation;
import com.example.project.controllers.popupControllers.DefinitionController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.popups.DefinitionPopup;
import com.example.project.services.GameScenes;
import com.example.project.services.PopupLoader;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
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
    @FXML private StackPane sidebar;
    @FXML HBox tileRackContainer;
    @FXML HBox wordViewHBox;
    @FXML HBox upgradeTilesContainer;
    @FXML Button playButton;
    @FXML Button redrawButton;
    @FXML VBox redrawContainer;
    @FXML Button confirmRedrawButton;
    @FXML private StackPane root;
    @FXML private StackPane definitionContainer;

    private static LevelModel levelModel;
    private DefinitionPopup definitionPopup = new DefinitionPopup();
    private DefinitionController definitionController;
    private UpgradeTileGroup upgradeGroup;
    private LetterTileGroup tileRack;
    private LetterTileGroup wordRow;
    private LetterTileGroup redrawColumn;
    private SidebarController sidebarController;

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
     * @see Session#getMoneyProperty() for the money binding
     */
    @FXML
    public void initialize()
    {
        // Setup Listeners. (automatically updates each property when they're changed)
        Session.getInstance().getCurrentRedraws().addListener((obs, oldVal, newVal) -> syncRedrawButton());
        Session.getInstance().getCurrentPlays().addListener((obs, oldVal, newVal) -> syncPlayButton());

        levelModel.getIsRedrawActive().addListener((obs, oldVal, newVal) -> syncRedrawWindow());
        definitionPopup.getIsDefinitionActive().addListener((obs, oldVal, newVal) -> syncDefinitionWindow(newVal));

        tileRack = new LetterTileGroup(levelModel.getHandSize(), tileRackContainer,
                levelModel.getTileRackRowTilesProperty(), this::onLetterTileClicked);

        wordRow = new LetterTileGroup(levelModel.getMaxWordSize(), wordViewHBox,
                levelModel.getWordRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncPlayButton));

        redrawColumn = new LetterTileGroup(levelModel.getRedrawWindowSize(), redrawContainer,
                levelModel.getRedrawRowTilesProperty(), this::onLetterTileClicked,
                List.of(this::syncRedrawButton,this::syncConfirmRedrawButton));

        upgradeGroup = new UpgradeTileGroup(upgradeTilesContainer, levelModel.getUpgradeTilesProperty());

        setupDefinitionPopup();

        var loadedSidebar = this.loadSidebar();
        var sidebarNode = ((StackPane) loadedSidebar.node());
        this.sidebar.getChildren().add(sidebarNode);
        this.sidebarController = loadedSidebar.controller();
        sidebarController.setupProperties(levelModel);
    }

    @Override
    public void onSceneChangedToThis()
    {
        levelModel.setupNewLevel();
        this.logger.logMessage("level page loaded.");
        levelModel.setupNewLevel();
        levelWonLostText.setText("");

        // sync observable properties.
        syncPlayButton();
        syncRedrawButton();
        syncConfirmRedrawButton();
        syncRedrawWindow();
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

    private void syncRedrawWindow()
    {
        var isRedrawActive = levelModel.getIsRedrawActive().get();
        var distance = isRedrawActive ? -50 : 300; // slide on if inactive. slide out if active.
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


    private void syncRedrawButton()
    {
        var redraws = Session.getInstance().getCurrentRedraws().get();
        redrawButton.setDisable(redraws == 0);
        redrawButton.setText(levelModel.getIsRedrawActive().get() ? "cancel" : "redraw");
    }

    private void syncPlayButton()
    {
        var plays = Session.getInstance().getCurrentPlays().get();
        playButton.setDisable((plays == 0) || !levelModel.isCurrentWordValid() || levelModel.getWordRowTilesProperty().isEmpty() || levelModel.getIsRedrawActive().get());
        playButton.setText("play");
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
        definitionPopup.setPopup(levelModel.getCurrentWord());
        playButton.setDisable(true);

        // TODO: or just tell sidebar to do a text emphasis at a point idk.
        var rawPoints = sidebarController.getRawPointsLabel();
        var multiplier = sidebarController.getmultiplierLabel();
        var currentScoreLabel = sidebarController.getCurrentScoreLabel();

        int startScore = levelModel.getPlayersTotalPoints().get();
        var tileScoringSequence = new LevelScoreSequence(wordRow.getControllers(), levelModel, rawPoints, multiplier);

        tileScoringSequence.setOnFinished(e ->
        {
            int endScore = startScore + levelModel.calcTotalWordScore();

            ScoreTimeline totalScoreTimeline = new ScoreTimeline();
            var scoreInitialColour = currentScoreLabel.getTextFill();
            Timeline timeline = totalScoreTimeline.animateTotalScore(startScore, endScore, currentScoreLabel);
            timeline.setOnFinished(f ->
            {
                TextEmphasisAnimation scoreEmphasis = new TextEmphasisAnimation(currentScoreLabel, scoreInitialColour, scoreInitialColour, Duration.seconds(0));
                scoreEmphasis.play();
                playButton.setDisable(false);
                levelModel.playTiles();
                levelModel.resetCombo();
                levelModel.setTotalScore(endScore);
                levelModel.getTileScoreSoundPlayer().reset();
                definitionPopup.setIsDefinitionActive(true);
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