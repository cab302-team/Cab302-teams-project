package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.GameScreenModel;
import com.example.project.models.gameScreens.LoginModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.services.Logger;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import com.example.project.services.Session;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the login scene.
 */
public class LoginController extends GameScreenController implements ModelObserver
{
    @FXML
    Pane titleRow;

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
        this.loginModel = new LoginModel(Session.getInstance(), this, new UsersDAO());
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
        this.loginModel = new LoginModel(ses, this, dao);
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Login page loaded.");
    }

    @FXML
    public void initialize()
    {
        ListProperty<LetterTile> lettersInWordPlayWord = new SimpleListProperty<>(FXCollections.observableArrayList());
        var letters = List.of('w', 'o', 'r', 'd', 'p', 'l', 'a','y');
        for (char letter : letters){
            lettersInWordPlayWord.add(new LetterTile(letter));
        }

        // TODO: put in model moethdo that retusn an unmodificable list reutrns
        // ObservableList<LetterTile> unmodifiableList = FXCollections.unmodifiableObservableList();


//        this.titleRow.getChildren().add();


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

    @Override
    public void onModelChanged() {
        System.out.println("TODO: login controller on model changed.");
    }
}