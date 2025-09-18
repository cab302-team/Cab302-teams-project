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
    private final PasswordHasher passwordHasher;

    private final ReadOnlyStringWrapper infoText = new ReadOnlyStringWrapper("");

    /**
     * Gets the info text property.
     * @return ReadonlyStringProperty.
     */
    public ReadOnlyStringProperty getWelcomeTextProperty(){
        return this.infoText;
    }

    /**
     * constructor.
     * @param session session to use for the game.
     * @param dao users database implementation.
     * @param passwordHasher password hasher for this login
     */
    public LoginModel(Session session, UsersDAO dao, PasswordHasher passwordHasher)
    {
        super(session);
        this.usersDAO = dao;
        this.passwordHasher = passwordHasher;
    }

    /**
     * @param username username
     * @param password password
     * @return returns value indicating if login is valid.
     */
    private boolean doesPasswordMatch(String username, String password)
    {
        var user = this.usersDAO.getUser(username);
        return this.passwordHasher.doesPasswordMatch(password, user.getPassword());
    }

    /**
     * @param username username.
     * @param password password.
     */
    public void onLoginClicked(String username, String password)
    {
        if (!this.usersDAO.doesUserExist(username)) {
            this.infoText.set("No account found. Sign up first.");
            return;
        }

        if (!this.doesPasswordMatch(username, password)) {
            this.infoText.set("Incorrect password.");
            return;
        }

        var user = this.usersDAO.getUser(username);
        this.session.setUser(user);
        SceneManager.getInstance().switchScene(GameScenes.LEVEL);
    }

    /**
     * returns value indicating if the input is legal. Illegal if blank.
     * @param username username.
     * @param password password.
     * @return value indicating if the input is legal.
     */
    private boolean isInputLegal(String username, String password)
    {
        if (username.isBlank() || password.isBlank()) {
            this.infoText.set("Fields cannot be empty.");
            return false;
        }

        if (username.length() < 3 || username.length() > 20) {
            this.infoText.set("Username must be between 3-20 characters long.");
            return false;
        }

        if (password.length() < 8) {
            this.infoText.set("Password must be at least 8 characters.");
            return false;
        }

        if (password.equals(username)) {
            this.infoText.set("Password must not match username.");
            return false;
        }

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
            this.infoText.set("Already signed up. Click login.");
            return;
        }

        this.usersDAO.addUser(new User(username, password, 0));
        this.infoText.set("User added successfully.");
    }
}
