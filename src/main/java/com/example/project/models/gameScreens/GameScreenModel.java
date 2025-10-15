package com.example.project.models.gameScreens;

import com.example.project.services.Logger;
import com.example.project.services.Session;
import com.example.project.services.SessionManager;

/**
 * Game Screen Model. extended by loginModel, shopModel, levelModel.
 */
public abstract class GameScreenModel
{
    protected final Session session;

    protected final Logger logger = new Logger();

    protected GameScreenModel ()
    {
        this.session = SessionManager.getSession();
    }

    protected GameScreenModel (Session session)
    {
        this.session = session;
    }
}
