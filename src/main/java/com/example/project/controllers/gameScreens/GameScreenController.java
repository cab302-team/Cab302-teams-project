package com.example.project.controllers.gameScreens;

import com.example.project.services.Logger;

/**
 * Game screen controller that has some startup on screen thing todo. Level, shop controllers.
 */
public abstract class GameScreenController
{
    protected Logger logger = new Logger();

    protected GameScreenController() {}

    /**
     * Called when the game scene is changed to one of the GameScenes Login, Level, Shop etc.
     */
    public abstract void onSceneChangedToThis();
}
