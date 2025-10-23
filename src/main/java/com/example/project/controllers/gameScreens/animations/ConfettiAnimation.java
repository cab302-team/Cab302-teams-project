package com.example.project.controllers.gameScreens.animations;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

/**
 * class to play confetti animation.
 */
public class ConfettiAnimation
{
    /**
     * play animation.
     * @param confettiLayer pane to play confetti on.
     */
    public static void playConfetti(Pane confettiLayer)
    {
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
