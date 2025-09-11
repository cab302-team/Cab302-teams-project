package com.example.project.models.gameScreens;

import com.example.project.controllers.gameScreens.ModelObserver;
import com.example.project.services.Logger;
import com.example.project.services.Session;

/**
 * Game Screen Model. extended by loginModel, shopModel, levelModel.
 */
public abstract class GameScreenModel
{
    protected Session session;

    protected Logger logger = new Logger();

    protected GameScreenModel (Session session)
    {
        this.session = session;
    }
}
