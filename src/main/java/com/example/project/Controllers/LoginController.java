package com.example.project.Controllers;

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
public class LoginController
{
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    // These and DictioanryDAO will probably be in every controller
    private final UsersDAO usersDAO = new UsersDAO();

    @FXML
    protected void onLoginButtonClick()
    {
        welcomeText.setText("Welcome!");
        System.out.printf("username: %s", usernameTextField.getText());

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