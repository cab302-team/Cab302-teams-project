package com.example.project.controllers.gameScreens.animations;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

/**
 * An animation to quickly enlarge text and then return it to its original scale.
 */
public class TextEmphasisAnimation extends AnimationWrapper
{
    /**
     * create the animations for text growing then shrinking
     * @param textToAnimate Label.
     * @param colorAtStart initial colour change.
     * @param changeColourAtEnd colour change after finished.
     * @param timePauseAfterAnim time delay after animation until animation finished.
     */
    public TextEmphasisAnimation(Label textToAnimate, Paint colorAtStart, Paint changeColourAtEnd, Duration timePauseAfterAnim)
    {
        super();

        var colourTextAtStart = new PauseTransition(Duration.seconds(0));
        colourTextAtStart.setOnFinished(e -> textToAnimate.setTextFill(colorAtStart));
        this.sequentialAnimation.getChildren().add(colourTextAtStart);

        var revertSizeTransition = new ScaleTransition(Duration.seconds(0.2), textToAnimate);
        revertSizeTransition.setToY(1);
        revertSizeTransition.setToX(1);

        var doubleSizeTransition = new ScaleTransition(Duration.seconds(0.2), textToAnimate);
        doubleSizeTransition.setToY(2);
        doubleSizeTransition.setToX(2);

        var pauseAfter = new PauseTransition(timePauseAfterAnim);
        pauseAfter.setOnFinished(e -> textToAnimate.setTextFill(changeColourAtEnd));

        this.sequentialAnimation.getChildren().add(doubleSizeTransition);
        this.sequentialAnimation.getChildren().add(revertSizeTransition);
        this.sequentialAnimation.getChildren().add(pauseAfter);
    }
}
