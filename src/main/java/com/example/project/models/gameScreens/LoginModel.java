package com.example.project.models.gameScreens;

import com.example.project.models.User;
import com.example.project.services.PasswordHasher;
import com.example.project.services.Session;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 * Login model class.
 */
public class LoginModel extends GameScreenModel
{
    private final UsersDAO usersDAO;

    private ReadOnlyStringWrapper welcomeText = new ReadOnlyStringWrapper("");

    public ReadOnlyStringProperty getWelcomeTextProperty(){
        return this.welcomeText;
    }

    /**
     * constructor.
     * @param session session to use for the game.
     * @param dao users database implementation.
     */
    public LoginModel(Session session, UsersDAO dao)
    {
        super(session);
        this.usersDAO = dao;
    }

    /**
     * @param username username
     * @param password password
     * @return returns value indicating if login is valid.
     */
    private boolean doesPasswordMatch(String username, String password)
    {
        var user = this.usersDAO.getUser(username);
        return PasswordHasher.checkPassword(password, user.getPassword());
    }

    /**
     * @param username username.
     * @param password password.
     */
    public void onLoginClicked(String username, String password)
    {
        if (!this.isInputLegal(username, password)){
            return;
        }

        if (!this.usersDAO.doesUserExist(username))
        {
            this.welcomeText.set("No account found. Sign up first.");
            return;
        }

        if (!this.doesPasswordMatch(username, password)) {
            welcomeText.set("Incorrect password.");
            return;
        }

        var user = this.usersDAO.getUser(username);
        this.session.setUser(user);
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }

    private boolean isInputLegal(String username, String password)
    {
        if (username.isBlank() || password.isBlank())
        {
            this.welcomeText.set("fields cannot be empty.");
            return false;
        }

        // TODO: add to here if we want to restrict special characters or number or put password length / digit requirements.

        return true;
    }

    /**
     * Adds user to database.
     * @param username username
     * @param password password
     */
    public void onSignUpClicked(String username, String password)
    {
        if (!isInputLegal(username, password)){
            return;
        }

        if (this.usersDAO.doesUserExist(username))
        {
            this.welcomeText.set("already signed up. Click login.");
            return;
        }

        this.usersDAO.addUser(new User(username, password, 0));
        this.welcomeText.set("user added successfully.");
    }
}
