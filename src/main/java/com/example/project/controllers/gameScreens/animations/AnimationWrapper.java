package com.example.project.controllers.gameScreens.animations;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Animation wrapper class for our projects animations that get used.
 */
public abstract class AnimationWrapper
{
    protected final SequentialTransition sequentialAnimation;

    protected AnimationWrapper() {
        this.sequentialAnimation = new SequentialTransition();
    }

    /**
     * play.
     */
    public void play() {
        sequentialAnimation.play();
    }

    /**
     * Stop.
     */
    public void stop() {
        sequentialAnimation.stop();
    }

    /**
     * Pause
     */
    public void pause() {
        sequentialAnimation.pause();
    }

    /**
     * Set on finished.
     * @param handler actions to do.
     */
    public void setOnFinished(EventHandler<ActionEvent> handler) {
        sequentialAnimation.setOnFinished(handler);
    }

    /**
     * returns animations children.
     * @return list of animations.
     */
    public ObservableList<Animation> getChildren(){
        return this.sequentialAnimation.getChildren();
    }
}
