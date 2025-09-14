package com.example.project.controllers.gameScreens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.Random;

public class FloatingAnimation
{
    private final Random random = new Random();
    private final TranslateTransition translation = new TranslateTransition();
    private double maxOffset;

    public void apply(Node node, double newMaxOffset, double intervalSeconds)
    {
        moveToRandom(node, newMaxOffset, intervalSeconds, intervalSeconds);
    }

    private void moveToRandom(Node node, double newMaxOffset, double minD, double maxD) {
        // pick random target position
        double x = random.nextDouble(0, 10);
        double y = random.nextDouble(0, 10);

        double duration = minD + random.nextDouble() * (maxD - minD);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(duration), node);
        tt.setToX(x);
        tt.setToY(y);
        tt.setInterpolator(javafx.animation.Interpolator.LINEAR); // constant speed

        // when done, pick a new target
        tt.setOnFinished(e -> moveToRandom(node, maxOffset, minD, maxD));
        tt.play();
    }

    private double periodSeconds;

    public void applySinusoidal(Node node, double maxOffset, double periodSeconds) {
        this.maxOffset = maxOffset;
        this.periodSeconds = periodSeconds;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(16), event -> {
                    // current time in seconds
                    double t = (System.currentTimeMillis() % (long)(periodSeconds * 1000)) / 1000.0;

                    // sinusoidal offsets
                    double x = Math.sin(t * 2 * Math.PI / periodSeconds) * maxOffset;
                    double y = Math.cos(t * 2 * Math.PI / periodSeconds) * maxOffset / 2; // less vertical drift

                    node.setTranslateX(x);
                    node.setTranslateY(y);
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
