package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.DailyRewardModel;
import com.example.project.models.gameScreens.DailyRewardType;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.TranslateTransition;

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
        Platform.runLater(() -> showConfetti());

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

        for (int i = 0; i < 100; i++) {
            Rectangle confetti = new Rectangle(6, 12); // Bigger and brighter
            confetti.setFill(Color.hsb(random.nextInt(360), 1.0, 1.0));

            double centerX = confettiLayer.getWidth() / 2;
            double centerY = confettiLayer.getHeight() / 2;
            confetti.setLayoutX(centerX);
            confetti.setLayoutY(centerY);

            TranslateTransition drop = new TranslateTransition(Duration.seconds(1.5 + random.nextDouble()), confetti);
            drop.setByX(random.nextDouble() * 400 - 200); // Random spread: -200 to +200
            drop.setByY(random.nextDouble() * 400 - 100); // More vertical scatter

            drop.setCycleCount(1);
            confettiLayer.getChildren().add(confetti);
            drop.play();

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> confettiLayer.getChildren().clear());
            delay.play();
        }
    }
}