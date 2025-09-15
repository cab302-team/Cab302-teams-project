package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import java.util.List;


public class AnimationsForScreens
{
    /**
     * create the animations for text growing then shrinking
     * @param textToAnimate Label.
     * @return list of animations to use.
     */
    public SequentialTransition animateTextEmphasis(Label textToAnimate, Paint colorAtStart, Paint changeColourAtEnd, Duration timePauseAfterAnim)
    {
        SequentialTransition sequence = new SequentialTransition();

        var colourTextAtStart = new PauseTransition(Duration.seconds(0));
        colourTextAtStart.setOnFinished(e -> textToAnimate.setTextFill(colorAtStart));
        sequence.getChildren().add(colourTextAtStart);

        var revertSizeTransition = new ScaleTransition(Duration.seconds(0.2), textToAnimate);
        revertSizeTransition.setToY(1);
        revertSizeTransition.setToX(1);

        var doubleSizeTransition = new ScaleTransition(Duration.seconds(0.2), textToAnimate);
        doubleSizeTransition.setToY(2);
        doubleSizeTransition.setToX(2);

        var pauseAfter = new PauseTransition(timePauseAfterAnim);
        pauseAfter.setOnFinished(e -> textToAnimate.setTextFill(changeColourAtEnd));

        sequence.getChildren().add(doubleSizeTransition);
        sequence.getChildren().add(revertSizeTransition);
        sequence.getChildren().add(pauseAfter);

        return sequence;
    }

    public SequentialTransition createLevelScoreSequence(List<LetterTileController> wordTileControllers, LevelModel levelModel, Label comboCountLabel, Label multiplierLabel)
    {
        // Tile scoring transition
        SequentialTransition tileTransitions = new SequentialTransition();

        for (LetterTileController control : wordTileControllers)
        {
            var translateUp = new TranslateTransition(Duration.seconds(0.1), control.getRoot());
            translateUp.setByY(-10);

            translateUp.setOnFinished(e -> levelModel.addToCombo(control.getModel()));

            tileTransitions.getChildren().add(translateUp);
            var textSumSequence = this.animateTextEmphasis(comboCountLabel, Color.BLUE, Color.BLACK, Duration.seconds(0));
            var textMultiSequence = this.animateTextEmphasis(multiplierLabel, Color.RED, Color.BLACK, Duration.seconds(0));
            tileTransitions.getChildren().addAll(textSumSequence.getChildren());
            tileTransitions.getChildren().addAll(textMultiSequence.getChildren());
        }

        // After all tiles
        var timeDelay = new PauseTransition(Duration.seconds(1));
        tileTransitions.getChildren().add(timeDelay);
        return tileTransitions;
    }


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

    public TranslateTransition slideTransition(Duration time, Pane containerToAnimate, int distance)
    {
        TranslateTransition slide = new TranslateTransition(time, containerToAnimate);
        slide.setToX(distance);
        return slide;
    }
}
