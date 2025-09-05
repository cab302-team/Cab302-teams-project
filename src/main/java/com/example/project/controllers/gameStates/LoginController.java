package com.example.project.controllers.gameStates;

import com.example.project.Logger;
import com.example.project.SceneManager;
import com.example.project.GameScenes;
import com.example.project.models.User;
import com.example.project.models.sqlite.dAOs.UsersDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the login scene.
 */
public class LoginController extends GameScreenController
{
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    private final UsersDAO usersDAO;

    /**
     * No arg constructor.
     */
    public LoginController() {
        super(new Logger());
        usersDAO = new UsersDAO();
    }

    /**
     * Constructor with injection for tests.
     * @param logger logger to use.
     * @param dao UsersDAO to use.
     * @param usernameField username text field.
     * @param injectwelcomeText welcome label.
     */
    public LoginController(Logger logger, UsersDAO dao, TextField usernameField, Label injectwelcomeText)
    {
        super(logger);
        usersDAO = dao;
        usernameTextField = usernameField;
        welcomeText = injectwelcomeText;
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Login page loaded.");
    }

    @FXML
    protected void onLoginButtonClick()
    {
        var exist = usersDAO.doesUserExist(usernameTextField.getText());
        if (exist)
        {
            welcomeText.setText("logged in!");
            SceneManager.getInstance().switchScene(GameScenes.LEVEL);
        }
        else
        {
            welcomeText.setText("not a user you need to sign up.");
        }
    }

    @FXML
    protected void onSignupButtonClick()
    {
        this.usersDAO.addUser(new User(usernameTextField.getText(), passwordTextField.getText(), 0));
        welcomeText.setText("Signed up!");
    }
}