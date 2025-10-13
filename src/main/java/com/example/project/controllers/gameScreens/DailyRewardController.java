package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.DailyRewardModel;
import com.example.project.models.gameScreens.DailyRewardType;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

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

    @Override
    public void onSceneChangedToThis() {
        // Optional: logic to run when this scene is shown
        // Could auto-spin or just leave empty for now
    }

    @FXML
    private void onSpinButtonClicked() {
        spinButton.setDisable(true);

        // Create spin animation
        int spinDegrees = 720 + random.nextInt(360);
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), wheelImage);
        rotate.setByAngle(spinDegrees);
        rotate.setOnFinished(event -> {
            // Determine and apply reward
            DailyRewardType reward = DailyRewardModel.rollReward();
            DailyRewardModel.applyReward(reward);
            showReward(reward);
        });
        rotate.play();
    }

    private void showReward(DailyRewardType reward) {
        String message = switch (reward) {
            case FREE_SHOP_ITEM -> "🎁 You won a FREE shop item!";
            case DIAMOND_40 -> "💎 You won $40!";
            case LUCKY_WORD -> "🍀 Lucky Word activated! Double rewards on your next word!";
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
}