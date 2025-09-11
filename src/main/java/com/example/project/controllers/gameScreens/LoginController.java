package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.GameScreenModel;
import com.example.project.models.gameScreens.LoginModel;
import com.example.project.services.Logger;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import com.example.project.services.Session;
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

    private final LoginModel loginModel;

    /**
     * No arg constructor.
     */
    public LoginController()
    {
        super(new Logger());
        this.loginModel = new LoginModel(Session.getInstance(), new UsersDAO());
    }

    /**
     * Constructor with injection for tests.
     * @param logger logger to use.
     * @param dao UsersDAO to use.
     * @param usernameField username text field.
     * @param injectwelcomeText welcome label.
     * @param ses game session.
     */
    public LoginController(Logger logger, UsersDAO dao, TextField usernameField, Label injectwelcomeText, Session ses)
    {
        super(logger);
        usernameTextField = usernameField;
        welcomeText = injectwelcomeText;
        this.loginModel = new LoginModel(ses, dao);
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Login page loaded.");
    }

    @FXML
    protected void onLoginButtonClick()
    {
        if (!loginModel.isSignedUp(usernameTextField.getText()))
        {
            welcomeText.setText("Not signed up. Signup first.");
            return;
        }

        if (loginModel.isValidLogin(usernameTextField.getText(), passwordTextField.getText()))
        {
            loginModel.loginUser(usernameTextField.getText(), passwordTextField.getText());
        }
        else
        {
            welcomeText.setText("incorrect password");
        }
    }

    @FXML
    protected void onSignupButtonClick()
    {
        if (loginModel.isSignedUp(usernameTextField.getText()))
        {
            welcomeText.setText("Already signed up. can login.");
            return;
        }

        this.loginModel.signUp(usernameTextField.getText(), passwordTextField.getText());
        welcomeText.setText("Signed up!");
    }
}