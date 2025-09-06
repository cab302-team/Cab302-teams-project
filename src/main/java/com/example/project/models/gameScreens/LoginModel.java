package com.example.project.models.gameScreens;

import com.example.project.models.User;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;

import java.util.Objects;

/**
 * Login model class.
 */
public class LoginModel extends GameScreenModel
{
    private final UsersDAO usersDAO;

    /**
     * @param dao users database implementation.
     */
    public LoginModel(UsersDAO dao)
    {
        this.usersDAO = dao;
    }

    /**
     * @param username username
     * @param password password
     * @return returns value indicating if login is valid.
     */
    public boolean isValidLogin(String username, String password)
    {
        var user = this.usersDAO.getUser(username);
        return Objects.equals(user.getPassword(), password);
    }

    /**
     * @param username username.
     * @param password password.
     */
    public void loginUser(String username, String password)
    {
        var user = this.usersDAO.getUser(username);
        this.session.setUser(user);
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }

    /**
     * @param username username.
     * @return returns valud indicating if user is already in the database (signed up).
     */
    public boolean isSignedUp(String username)
    {
        return usersDAO.doesUserExist(username);
    }

    /**
     * Adds user to database.
     * @param username username
     * @param password password
     */
    public void signUp(String username, String password)
    {
        this.usersDAO.addUser(new User(username, password, 0));
    }
}
