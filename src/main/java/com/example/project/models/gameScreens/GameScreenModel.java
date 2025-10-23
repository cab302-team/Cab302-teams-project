package com.example.project.models.gameScreens;

import com.example.project.services.Logger;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;

/**
 * Game Screen Model. extended by loginModel, shopModel, levelModel.
 */
public abstract class GameScreenModel
{
    protected Logger logger = new Logger();

    protected final Session session;

    protected final SceneManager sceneManager;

    /**
     * Get session.
     * @return session.
     */
    public Session getSession() { return session; }

    /**
     * get scene manager
     * @return scene manager
     */
    public SceneManager getSceneManager() { return sceneManager; }

    protected GameScreenModel (Session session, SceneManager sceneManager)
    {
        this.sceneManager = sceneManager;
        this.session = session;
    }
}
