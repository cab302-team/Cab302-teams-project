package com.example.project.controllers.gameScreens;

import com.example.project.controllers.gameScreens.animations.InfiniteFloatingAnimation;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LoginModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import com.example.project.services.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the login scene.
 */
public class LoginController extends GameScreenController
{
    @FXML
    Pane root;

    @FXML
    Pane titleRow;

    @FXML
    ImageView imageBG;

    @FXML
    Pane backgroundContainer;

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
        super();
        this.loginModel = new LoginModel(Session.getInstance(), new UsersDAO());
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Login page loaded.");
    }

    /**
     * FXML initialise function called once when the .fxml is loaded on application launch.
     */
    @FXML
    public void initialize()
    {
        var newIm = new Image(Objects.requireNonNull(getClass().getResource("/com/example/project/gameScreens/loginBgImage.jpg")).toExternalForm());
        imageBG.setImage(newIm);
        imageBG.fitWidthProperty().bind(backgroundContainer.widthProperty());
        imageBG.fitHeightProperty().bind(backgroundContainer.heightProperty());

        List<LetterTile> lettersInWordPlayWord = new ArrayList<>();
        var letters = List.of('w', 'o', 'r', 'd', 'p', 'l', 'a','y');
        for (char letter : letters){
            lettersInWordPlayWord.add(new LetterTile(letter));
        }

        for (var t : lettersInWordPlayWord)
        {
            var ltController = new LetterTileController(t);
            titleRow.getChildren().add(ltController.getRoot());
            InfiniteFloatingAnimation fa = new InfiniteFloatingAnimation();
            fa.apply(ltController.getRoot(), 4);
        }
    }

    @FXML
    protected void onLoginButtonClick()
    {
        if (!loginModel.isSignedUp(usernameTextField.getText()))
        {
            welcomeText.setText("Not signed up. Sign up first.");
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