package com.example.project.controllers.gameScreens.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.Random;

public class InfiniteFloatingAnimation
{
    private final Random random = new Random();

    public void apply(Node node, double intervalSeconds)
    {
        moveToRandom(node, intervalSeconds, intervalSeconds);
    }

    private void moveToRandom(Node node, double minD, double maxD)
    {
        // pick random target position
        double x = random.nextDouble(0, 10);
        double y = random.nextDouble(0, 10);

        double duration = minD + random.nextDouble() * (maxD - minD);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(duration), node);
        tt.setToX(x);
        tt.setToY(y);
        tt.setInterpolator(javafx.animation.Interpolator.LINEAR); // constant speed

        // when done, pick a new target
        tt.setOnFinished(e -> moveToRandom(node, minD, maxD));
        tt.play();
    }
}
