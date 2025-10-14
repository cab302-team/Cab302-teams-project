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

    private LevelModel levelModel;


    // TODO: actually also need to bind plays, redraws or pass from level controller. since shop could change those? So probably put plays redraws in session skip getting them from level.
    public void bindMoney(){
        moneyLabel.textProperty().bind(
                Session.getInstance().getMoneyProperty().asString("Funds: $%d")
        );
    }

    /**
     * Setup sync able properties.
     * @param levelModel level model.
     */
    public void setupProperties(LevelModel levelModel)
    {
        this.levelModel = levelModel;
        // Binds the money display to Session money property for automatic updates
        bindMoney();

        this.levelModel.wordPointsProperty().addListener((obs, oldVal, newVal) -> syncwordPointsProperty(newVal));
        levelModel.wordMultiProperty().addListener((obs, oldVal, newVal) -> syncwordMultiProperty(newVal));
        levelModel.getPlayersTotalPoints().addListener((obs, oldVal, newVal) -> syncTotalScoreProperty(newVal));

        levelModel.getCurrentPlays().addListener((obs, oldVal, newVal) -> syncPlaysCount());
        levelModel.getCurrentRedraws().addListener((obs, oldVal, newVal) -> syncRedrawsCount());
    }

    private void syncPlaysCount(){
        playsLeftLabel.setText(String.valueOf(levelModel.getCurrentPlays().get())); // TODO: sync plays left to model so its synced in sidebar. same with redraws left count.
    }

    private void syncRedrawsCount(){
        redrawsLeftLabel.setText(String.valueOf(levelModel.getCurrentRedraws().get()));
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

    /**
     * initial sync of properties when scene starts.
     */
    public void sync(){
        scoreToBeatLabel.setText(String.format("%s", levelModel.getLevelRequirement()));
        syncwordPointsProperty(levelModel.wordPointsProperty().get());
        syncwordMultiProperty(levelModel.wordMultiProperty().get());
        syncTotalScoreProperty(levelModel.getPlayersTotalPoints().get());
        syncPlaysCount();
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
