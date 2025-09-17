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
    public Timeline animateTotalScore(int startScore, int endScore, Label currentScoreLabel) {
        // Total scoring timeline
        Timeline scoreTimeline = new Timeline();
        IntegerProperty currentScore = new SimpleIntegerProperty(startScore);

        scoreTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(50), event -> {
                    if (currentScore.get() < endScore) {
                        currentScore.set(currentScore.get() + 1);
                        currentScoreLabel.setText(String.valueOf(currentScore.get()));
                    }
                })
        );
        scoreTimeline.setCycleCount(endScore - startScore);
        return scoreTimeline;
    }
}
