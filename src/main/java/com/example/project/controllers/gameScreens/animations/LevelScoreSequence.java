package com.example.project.controllers.gameScreens.animations;

import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

public class LevelScoreSequence extends AnimationWrapper
{
    public LevelScoreSequence(List<LetterTileController> wordTileControllers, LevelModel levelModel, Label playersPointsText)
    {
        super();

        for (LetterTileController control : wordTileControllers)
        {
            var translateUp = new TranslateTransition(Duration.seconds(0.1), control.getRoot());
            translateUp.setByY(-10);

            translateUp.setOnFinished(e -> levelModel.addTileToScore(control.getModel()));

            this.sequentialAnimation.getChildren().add(translateUp);

            TextEmphasisAnimation textScoreSequence = new TextEmphasisAnimation(playersPointsText, Color.GREEN, Color.BLACK, Duration.seconds(0));
            this.sequentialAnimation.getChildren().addAll(textScoreSequence.getChildren());
        }

        // After all tiles
        var timeDelay = new PauseTransition(Duration.seconds(1));
        this.sequentialAnimation.getChildren().add(timeDelay);
    }
}
