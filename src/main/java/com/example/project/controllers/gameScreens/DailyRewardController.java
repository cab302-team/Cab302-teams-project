package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.DailyRewardModel;
import com.example.project.models.gameScreens.DailyRewardType;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    /**
     * Initialises the daily reward view by resetting the reward label.
     */
    @FXML
    public void initialize() {
        rewardResultLabel.setText("");
    }

    @Override
    public void onSceneChangedToThis() {
        rewardResultLabel.setText("");
        confettiLayer.getChildren().clear();

        if (Session.getInstance().hasClaimedRewardToday()) {
            spinButton.setDisable(true);
            rewardResultLabel.setText("You’ve already claimed today’s reward.");
        } else {
            spinButton.setDisable(false);
        }
    }

    @FXML
    private void onSpinButtonClicked() {
        // Block spin if already claimed
        if (Session.getInstance().hasClaimedRewardToday()) {
            rewardResultLabel.setText("Already claimed.");
            spinButton.setDisable(true);
            return;
        }

        spinButton.setDisable(true);

        int spinDegrees = 720 + random.nextInt(360); // random stop angle
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), stickImage);
        rotate.setByAngle(spinDegrees);
        rotate.setOnFinished(event -> {
            DailyRewardType reward = DailyRewardModel.rollReward();
            DailyRewardModel.applyReward(reward);
            showReward(reward);
        });
        rotate.play();
    }

    private void showReward(DailyRewardType reward) {
        Platform.runLater(this::playConfetti);

        String message = switch (reward) {
            case Daily_Reward_Won_1Dollar -> "You won $1!";
            case Daily_Reward_Won_5Dollars -> "You won $5!";
            case Daily_Reward_Won_NOTHING -> "You won... nothing! Try again tomorrow for a better Prize!";
        };

        rewardResultLabel.setText(message);
        Session.getInstance().setLastRewardDate(LocalDate.now()); // mark claimed

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
            Platform.runLater(() -> SceneManager.getInstance().switchScene(GameScenes.MAINMENU));
        }).start();
    }

    private void playConfetti() {
        confettiLayer.getChildren().clear();
        Random rand = new Random();
        for (int i = 0; i < 75; i++) {
            Rectangle confetti = new Rectangle(6, 12);
            confetti.setFill(Color.hsb(rand.nextInt(360), 1.0, 1.0));
            confetti.setLayoutX(confettiLayer.getWidth() / 2);
            confetti.setLayoutY(confettiLayer.getHeight() / 2);

            var drop = new javafx.animation.TranslateTransition(Duration.seconds(1 + rand.nextDouble()), confetti);
            drop.setByX(rand.nextDouble() * 400 - 200);
            drop.setByY(rand.nextDouble() * 400 - 100);
            drop.setCycleCount(1);
            confettiLayer.getChildren().add(confetti);
            drop.play();
        }
    }
}