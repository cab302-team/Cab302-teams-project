package com.example.project.Controllers.gameScreens;

import com.example.project.Logger;

/**
 * Singleton Game screen controller that has some startup on screen thing todo. Level, shop controllers.
 */
public abstract class gameScreenController
{
    protected Logger logger = new Logger();

    public abstract void onSceneChangedToThis();
}
