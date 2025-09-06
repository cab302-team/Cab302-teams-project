package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.GameScreenModel;
import com.example.project.services.Logger;
import com.example.project.services.Session;

/**
 * Game screen controller that has some startup on screen thing todo. Level, shop controllers.
 */
public abstract class GameScreenController
{
    protected Logger logger = new Logger();

    protected GameScreenController() {}

    protected GameScreenController(Logger logger)
    {
        this.logger = logger;
    }

    public abstract GameScreenModel getModel();

    public void setSession(Session newSession)
    {
        getModel().setSession(newSession);
    }

    /**
     * Called when the game scene is changed to one of the GameScenes Login, Level, Shop etc.
     */
    public abstract void onSceneChangedToThis();
}
