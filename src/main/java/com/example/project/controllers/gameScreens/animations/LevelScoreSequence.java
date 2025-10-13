package com.example.project.controllers.gameScreens.animations;

import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

/**
 * Animates each level tile popping up and the score text increasing for every tile in the word row.
 */
public class LevelScoreSequence extends AnimationWrapper
{
    /**
     * Constructor.
     * @param wordTileControllers tiles to animate.
     * @param levelModel level model.
     * @param comboCountLabel the text label to add the sum to.
     * @param multiplierLabel the text label to add the multi to.
     */
    public LevelScoreSequence(List<LetterTileController> wordTileControllers, LevelModel levelModel, Label comboCountLabel, Label multiplierLabel)
    {
        super();

        for (LetterTileController control : wordTileControllers)
        {
            var translateUp = new TranslateTransition(Duration.seconds(0.1), control.getRoot());
            translateUp.setByY(-10);
            translateUp.setOnFinished(e ->
            {
                levelModel.addToCombo(control.getModel());
                levelModel.getTileScoreSoundPlayer().playNextNote();
            });

            this.sequentialAnimation.getChildren().add(translateUp);
            TextEmphasisAnimation sumComboSequence = new TextEmphasisAnimation(comboCountLabel, Color.WHITE, Color.WHITE, Duration.seconds(0));
            TextEmphasisAnimation multiComboSequence = new TextEmphasisAnimation(multiplierLabel, Color.WHITE, Color.WHITE, Duration.seconds(0));
            this.sequentialAnimation.getChildren().addAll(sumComboSequence.getChildren());
            this.sequentialAnimation.getChildren().addAll(multiComboSequence.getChildren());
        }

        // After all tiles
        var timeDelay = new PauseTransition(Duration.seconds(1));
        this.sequentialAnimation.getChildren().add(timeDelay);
    }
}
