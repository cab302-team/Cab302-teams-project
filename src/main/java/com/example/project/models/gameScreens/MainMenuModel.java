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
     * @param sceneManager scenes.
     */
    public MainMenuModel(Session session, SceneManager sceneManager)
    {
        super(session, sceneManager);
    }

    /**
     * switches to the level screen
     */
    public void onStartClicked()
    {
        this.sceneManager.switchScene(GameScenes.LEVEL);
    }

    /**
     * switches to the login screen
     */
    public void onLogoutClicked()
    {
        this.sceneManager.switchScene(GameScenes.LOGIN);
    }

}
