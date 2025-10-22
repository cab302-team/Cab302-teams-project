package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.DailyRewardModel;
import com.example.project.models.gameScreens.DailyRewardType;
import com.example.project.services.GameScene;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.Random;

/**
 * Controller for the daily reward popup screen.
 */
public class DailyRewardController extends GameScreenController
{
    @FXML private StackPane root;
    @FXML private ImageView wheelImage;
    @FXML private ImageView stickImage;
    @FXML private Button spinButton;
    @FXML private Label rewardResultLabel;
    @FXML private Pane confettiLayer;

    private final Random random = new Random();
    private DailyRewardModel model;

    /**
     * Initialises the daily reward view by resetting the reward label.
     */
    @Override
    public void setup(Session session, SceneManager sceneManager)
    {
        model = new DailyRewardModel(session, sceneManager);
        rewardResultLabel.setText("");
    }

    @Override
    public void onSceneChangedToThis() {
        rewardResultLabel.setText("");
        confettiLayer.getChildren().clear();

        if (model.getSession().hasClaimedRewardToday()) {
            spinButton.setDisable(true);
            rewardResultLabel.setText("You’ve already claimed today’s reward.");
        } else {
            spinButton.setDisable(false);
        }
    }

    @FXML
    private void onSpinButtonClicked() {
        // Block spin if already claimed
        if (model.getSession().hasClaimedRewardToday())
        {
            rewardResultLabel.setText("Already claimed.");
            spinButton.setDisable(true);
            return;
        }

        spinButton.setDisable(true);

        int spinDegrees = 720 + random.nextInt(360); // random stop angle
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), stickImage);
        rotate.setByAngle(spinDegrees);
        rotate.setOnFinished(event ->
        {
            DailyRewardType reward = model.rollReward();
            model.applyReward(reward);
            showReward(reward);
        });
        rotate.play();
    }

    private void showReward(DailyRewardType reward)
    {
        var timeDelay = new PauseTransition(Duration.millis(3000));

        String message = switch (reward) {
            case Daily_Reward_Won_1Dollar -> "You won $1!";
            case Daily_Reward_Won_5Dollars -> "You won $5!";
            case Daily_Reward_Won_NOTHING -> "You won... nothing! Try again tomorrow for a better Prize!";
        };

        rewardResultLabel.setText(message);
        model.getSession().setLastRewardDate(LocalDate.now());
        timeDelay.setOnFinished(e -> model.getSceneManager().switchScene(GameScene.MAINMENU));
        timeDelay.play();
    }
}