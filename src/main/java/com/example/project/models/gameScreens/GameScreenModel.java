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

    protected ModelObserver observer;

    protected Logger logger = new Logger();

    protected GameScreenModel (Session session, ModelObserver observer)
    {
        this.session = session;
        this.observer = observer;
    }

    protected void notifyObservers()
    {
        if (observer == null){
            this.logger.logMessage("tried to update view on observer but none attached yet.");
            return;
        }

        observer.onModelChanged();
    }
}
