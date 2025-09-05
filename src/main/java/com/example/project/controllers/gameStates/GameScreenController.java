package com.example.project.controllers.gameStates;

import com.example.project.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton Game screen controller that has some startup on screen thing todo. Level, shop controllers.
 */
public abstract class GameScreenController
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameScreenController.class);
    protected Logger logger = new Logger();

    protected GameScreenController() {}

    protected GameScreenController(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * Called when the game scene is changed to one of the GameScenes Login, Level, Shop etc.
     */
    public abstract void onSceneChangedToThis();
}
