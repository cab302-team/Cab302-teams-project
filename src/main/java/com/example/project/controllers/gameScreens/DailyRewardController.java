package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.DailyRewardModel;
import com.example.project.models.gameScreens.DailyRewardType;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.awt.*;
import java.util.Random;

/**
 * Controller for the daily reward popup screen.
 */
public class DailyRewardController extends GameScreenController {

    @FXML
    private Pane root;

    @FXML
    private ImageView wheelImage;

    @FXML
    private Button spinButton;

    @FXML
    private Label rewardResultLabel;

    private final Random random = new Random();

    /**
     * Initializes the daily reward popup scene.
     * This sets the reward label to empty until the player spins the wheel.
     */
    @FXML
    public void initialize() {
        rewardResultLabel.setText("");
    }

    @FXML private Pane confettiLayer;

    @Override
    public void onSceneChangedToThis() {
        // Optional: logic to run when this scene is shown
        // Could auto-spin or just leave empty for now
    }

    @FXML private ImageView stickImage;

    @FXML
    private void onSpinButtonClicked() {
        spinButton.setDisable(true);

        int spinDegrees = 720 + random.nextInt(360); // always ends in a random slice
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
        showConfetti();
        String message = switch (reward) {
            case FREE_SHOP_ITEM -> "🎁 You won a FREE shop item!";
            case DIAMOND_40 -> "💎 You won $40!";
            case LUCKY_WORD -> "🍀 Lucky Word activated! Double rewards on your next word!";
            case BONUS_1 -> "🪙 You won $1!";
            case BONUS_5 -> "💵 You won $5!";
            case FUNNY_LOL -> "😹 You won... NOTHING! Better luck tomorrow.";
        };
        rewardResultLabel.setText(message);

        // Optional: Auto-continue after delay
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Wait 3 seconds
            } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() ->
                    SceneManager.getInstance().switchScene(GameScenes.LEVEL)
            );
        }).start();
    }

    private void showConfetti() {
        Random random = new Random();
        confettiLayer.getChildren().clear();

        for (int i = 0; i < 30; i++) {
            Rectangle confetti = new Rectangle(5, 10);
            confetti.setFill(Color.hsb(random.nextInt(360), 1.0, 1.0));
            confetti.setLayoutX(150 + random.nextInt(150)); // random X near center
            confetti.setLayoutY(100);

            TranslateTransition drop = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), confetti);
            drop.setByY(300 + random.nextInt(200));
            drop.setByX(random.nextInt(100) - 50);
            drop.setCycleCount(1);

            confettiLayer.getChildren().add(confetti);
            drop.play();
        }
    }
}