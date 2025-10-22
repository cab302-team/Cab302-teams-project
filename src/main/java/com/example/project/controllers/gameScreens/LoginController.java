package com.example.project.controllers.gameScreens;

import com.example.project.controllers.gameScreens.animations.InfiniteFloatingAnimation;
import com.example.project.models.gameScreens.LoginModel;
import com.example.project.models.tiles.LetterTileModel;
import com.example.project.services.GameScenes;
import com.example.project.services.PasswordHasher;
import com.example.project.controllers.tiles.TileControllerFactory;
import com.example.project.services.SceneManager;
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
    private Label infoText;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    private LoginModel loginModel;
    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();

    /**
     * No arg constructor.
     */
    public LoginController() { }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("Login page loaded.");

        loginModel.getWelcomeTextProperty().addListener((obs, oldVal, newVal) ->
                this.infoText.setText(newVal));
    }

    @Override
    public void setup(Session session, SceneManager sceneManager)
    {
        this.loginModel = new LoginModel(session, sceneManager, new UsersDAO(), new PasswordHasher());

        var newIm = new Image(Objects.requireNonNull(getClass().getResource("/com/example/project/gameScreens/loginBgImage.jpg")).toExternalForm());
        imageBG.setImage(newIm);
        imageBG.fitWidthProperty().bind(backgroundContainer.widthProperty());
        imageBG.fitHeightProperty().bind(backgroundContainer.heightProperty());

        List<LetterTileModel> lettersInWordPlayWord = new ArrayList<>();
        var letters = List.of('w', 'o', 'r', 'd', 'p', 'l', 'a','y');
        for (char letter : letters){
            lettersInWordPlayWord.add(new LetterTileModel(letter));
        }

        for (var t : lettersInWordPlayWord){
            var ltController = tileControllerFactory.createLetterTileController(t);
            titleRow.getChildren().add(ltController.getRoot());
            InfiniteFloatingAnimation fa = new InfiniteFloatingAnimation();
            fa.apply(ltController.getRoot(), 4);
        }
    }

    @FXML
    protected void onLoginButtonClick()
    {
        loginModel.onLoginClicked(usernameTextField.getText(), passwordTextField.getText());
    }

    @FXML
    protected void onSignupButtonClick()
    {
        this.loginModel.onSignUpClicked(usernameTextField.getText(), passwordTextField.getText());
    }

    // TODO: remove later
    @FXML
    private void skipLogin()
    {
        loginModel.getSceneManager().switchScene(GameScenes.LEVEL);
    }
}