package com.example.project.models.gameScreens;

import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;

/**
 * main menu model class
 */
public class MainMenuModel extends GameScreenModel
{

    /**
     * constructor.
     * @param session session to use for the game.
     */
    public MainMenuModel(Session session)
    {
        super(session);
    }

    /**
     * switches to the level screen
     */
    public void onStartClicked()
    {
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }

    /**
     * switches to the login screen
     */
    public void onLogoutClicked()
    {
        SceneManager.getInstance().switchScene(GameScenes.LOGIN);
    }

}
