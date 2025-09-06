package com.example.project.models.gameScreens;

import com.example.project.services.Session;

/**
 * Game Screen Model. extended by loginModel, shopModel, levelModel.
 */
public abstract class GameScreenModel
{
    protected Session session;

    protected GameScreenModel ()
    {    }

    public void setSession(Session newSession)
    {
        session = newSession;
    }
}
