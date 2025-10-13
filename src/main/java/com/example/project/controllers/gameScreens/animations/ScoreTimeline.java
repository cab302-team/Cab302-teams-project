package com.example.project.controllers.gameScreens.animations;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Class for total score counter animation
 */
public class ScoreTimeline {
    /**
     * @param startScore int from current total score
     * @param endScore int from calculated current score
     * @param currentScoreLabel Label for total score
     * @return timeline of total score counter
     */
    public Timeline animateTotalScore(int startScore, int endScore, Label currentScoreLabel, long totalDurationMillis) {
        // Total scoring timeline
        Timeline scoreTimeline = new Timeline();
        IntegerProperty currentScore = new SimpleIntegerProperty(startScore);

        int scoreDifference = endScore - startScore;

        int steps = scoreDifference;

        double durationPerStepMillis = (double) totalDurationMillis / steps;

        scoreTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(durationPerStepMillis), event -> {
                    if (currentScore.get() < endScore) {
                        currentScore.set(currentScore.get() + 1);
                        currentScoreLabel.setText(String.valueOf(currentScore.get()));
                    }
                })
        );
        scoreTimeline.setCycleCount(steps);

        scoreTimeline.setOnFinished(event -> {
            currentScoreLabel.setText(String.valueOf(endScore));
        });

        return scoreTimeline;
    }
}
