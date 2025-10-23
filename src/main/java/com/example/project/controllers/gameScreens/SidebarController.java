package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * The sidebar on shop and level with the current round info.
 */
public class SidebarController
{
    @FXML
    Pane root;

    /**
     * This runs after the constructor and after all @FXML fields are initialized once each time application opened.
     */
    @FXML
    private Label moneyLabel;

    @FXML private Label playsLeftLabel;

    @FXML private Label redrawsLeftLabel;

    @FXML Label scoreToBeatLabel;
    @FXML Label currentScoreLabel;
    @FXML Label rawPoints;
    @FXML Label multiplier;
    @FXML private Label ScoreAtLeastText;
    @FXML private Pane playersScoreContainer;
    @FXML private Pane wordScoreContainer;

    private Session session;

    /**
     * Binds things used in multiple screens, money, redraws plays.
     * @param session session.
     */
    public void bindPersistentInfo(Session session)
    {
        this.session = session;

        this.session.getMoneyProperty().addListener((obs, oldVal, newVal) -> syncMoney());
        this.session.getCurrentPlays().addListener((obs, oldVal, newVal) -> syncPlaysCount());
        this.session.getCurrentRedraws().addListener((obs, oldVal, newVal) -> syncRedrawsCount());
        this.session.getLevelRequirement().addListener((obs, oldVal, newVal) -> syncScoreToBeat());

        // TODO get tooltip setup to work with sidebar containers

        syncPlaysCount();
        syncRedrawsCount();
        syncMoney();
        syncScoreToBeat();
    }

    /**
     * Only show things relevant to upgrades or the shop. Money, redraws, plays.
     */
    public void hideLevelInfo()
    {
        playersScoreContainer.visibleProperty().set(false);
        wordScoreContainer.visibleProperty().set(false);
        ScoreAtLeastText.setText("Next level");
    }

    /**
     * Returns combo label.
     * @return combo label.
     */
    public Label getComboLabel(){
        return this.multiplier;
    }

    /**
     * Setup sync able properties.
     * @param levelModel level model.
     */
    public void setupProperties(LevelModel levelModel)
    {
        // Binds the money display to Session money property for automatic updates
        bindPersistentInfo(levelModel.getSession());
        levelModel.wordPointsProperty().addListener((obs, oldVal, newVal) -> syncwordPointsProperty(newVal));
        levelModel.wordMultiProperty().addListener((obs, oldVal, newVal) -> syncwordMultiProperty(newVal));
        levelModel.getPlayersTotalPoints().addListener((obs, oldVal, newVal) -> syncTotalScoreProperty(newVal));
        levelModel.getSession().getLevelRequirement().addListener((obs, oldVal, newVal) -> syncScoreToBeat());

        syncwordPointsProperty(levelModel.wordPointsProperty().get());
        syncwordMultiProperty(levelModel.wordMultiProperty().get());
        syncTotalScoreProperty(levelModel.getPlayersTotalPoints().get());
        syncScoreToBeat();
    }

    private void syncScoreToBeat(){
        scoreToBeatLabel.setText(String.format("%s", this.session.getLevelRequirement().get()));
    }

    private void syncMoney()
    {
        moneyLabel.setText(String.format("Funds: $%.2f", this.session.getMoneyProperty().get()));
    }

    private void syncPlaysCount(){
        playsLeftLabel.setText(String.valueOf(this.session.getCurrentPlays().get()));
    }

    private void syncRedrawsCount(){
        redrawsLeftLabel.setText(String.valueOf(this.session.getCurrentRedraws().get()));
    }

    /**
     * raw points label to be multiplied by multiplier.
     * @return label.
     */
    public Label getRawPointsLabel(){
        return this.rawPoints;
    }

    /**
     * current score label.
     * @return label.
     */
    public Label getCurrentScoreLabel(){
        return this.currentScoreLabel;
    }

    /**
     * word score multiplier label.
     * @return label.
     */
    public Label getmultiplierLabel(){
        return this.multiplier;
    }

    private void syncwordMultiProperty(Number newVal)
    {
        this.multiplier.setText(String.format("%s", newVal));
    }

    private void syncwordPointsProperty(Number newVal)
    {
        this.rawPoints.setText(String.format("%s", newVal));
    }

    private void syncTotalScoreProperty(Number newVal)
    {
        this.currentScoreLabel.setText(String.format("%s", newVal));
    }
}
