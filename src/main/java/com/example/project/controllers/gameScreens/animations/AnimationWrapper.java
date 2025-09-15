package com.example.project.controllers.gameScreens.animations;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class AnimationWrapper
{
    protected final SequentialTransition sequentialAnimation;

    protected AnimationWrapper() {
        this.sequentialAnimation = new SequentialTransition();
    }

    public void play() {
        sequentialAnimation.play();
    }

    public void stop() {
        sequentialAnimation.stop();
    }

    public void pause() {
        sequentialAnimation.pause();
    }

    public void setOnFinished(EventHandler<ActionEvent> handler) {
        sequentialAnimation.setOnFinished(handler);
    }

    public Animation getSequentialAnimation() {
        return sequentialAnimation;
    }

    public ObservableList<Animation> getChildren(){
        return this.sequentialAnimation.getChildren();
    }
}
