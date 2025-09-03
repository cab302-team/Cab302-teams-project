package com.example.project.Controllers.gameScreens;

import com.example.project.Logger;
import com.example.project.SceneManager;
import com.example.project.SceneTypes;
import com.example.project.User;
import com.example.project.sqlite.DAOs.UsersDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the login scene.
 */
public class LoginController extends gameScreenController
{
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    private final UsersDAO usersDAO = new UsersDAO();

    @Override
    public void onSceneChangedToThis() {
        // Do stuff
        this.logger.logMessage("Login page loaded.");
    }

    @FXML
    protected void onLoginButtonClick()
    {
        welcomeText.setText("Welcome!");

        var exist = usersDAO.doesUserExist(usernameTextField.getText());
        if (exist)
        {
            welcomeText.setText("logged in!");
            SceneManager.switchScene(SceneTypes.LEVEL);
        }
        else
        {
            welcomeText.setText("not a user you need to sign up.");
        }
    }

    @FXML
    protected void onSignupButtonClick()
    {
        this.usersDAO.addUser(new User(usernameTextField.getText(), passwordTextField.getText()));
        welcomeText.setText("Signed up!");
    }
}