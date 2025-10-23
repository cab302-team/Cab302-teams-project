package com.example.project.controllers.gameScreens;

import com.example.project.controllers.gameScreens.animations.InfiniteFloatingAnimation;
import com.example.project.controllers.tiles.TileControllerFactory;
import com.example.project.models.gameScreens.MainMenuModel;
import com.example.project.models.tiles.LetterTileModel;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.example.project.services.GameScene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * controller for the main menu.
 */
public class MainMenuController extends GameScreenController
{

    @FXML
    Pane root;

    @FXML
    Pane titleRow;

    @FXML
    ImageView imageBG;

    @FXML
    Pane backgroundContainer;

    @FXML private Button dailyRewardButton;
    @FXML private Button loadButton;

    private MainMenuModel mainMenuModel;
    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();
    private final UsersDAO usersDAO = new UsersDAO();

    /**
     * No arg constructor.
     */
    public MainMenuController() { }

    private VBox loadButtonParent;

    @Override
    public void onSceneChangedToThis()
    {
        var haveData = this.usersDAO.hasSaveData(this.mainMenuModel.getSession().getUser());
        var containsButton = loadButtonParent.getChildrenUnmodifiable().contains(loadButton);
        var hideButton = !haveData && containsButton;
        if (hideButton) {
            loadButtonParent.getChildren().remove(loadButton);
        }
        else if (!containsButton) {
            loadButtonParent.getChildren().add(loadButton);
        }

        updateDailyRewardUI();
    }

    @Override
    public void setup(Session session, SceneManager sceneManager)
    {
        loadButtonParent = (VBox) this.loadButton.getParent();

        this.mainMenuModel = new MainMenuModel(session, sceneManager);

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

        TooltipSetup tooltip = new TooltipSetup();
        tooltip.setupTooltip(dailyRewardButton, TooltipSetup.Element.DAILY, mainMenuModel);
    }

    @FXML
    protected void onStartButtonClick()
    {
        mainMenuModel.onStartClicked();
    }

    @FXML
    protected void onLoadButtonClick()
    {
        this.mainMenuModel.getSession().load();
        this.mainMenuModel.getSceneManager().switchScene(GameScene.LEVEL);
    }

    @FXML
    protected void onLogoutButtonClick()
    {
        mainMenuModel.onLogoutClicked();
    }


    @FXML
    protected void onDailyRewardClicked() { mainMenuModel.getSceneManager().switchScene(GameScene.DAILY_REWARD); }

    @FXML
    protected void onFastForwardClick() {
        // Fast-forward by clearing reward state and resetting money for testing
        var session = mainMenuModel.getSession();
        session.setLastRewardDate(LocalDate.now().minusDays(1));
        session.resetMoney();
        updateDailyRewardUI();

        logger.logMessage("Fast forwarded to the next day, to next reward date and reset money.");
    }

    /**
     * Updates the UI for the daily reward button and label.
     * Hides the button if already claimed today.
     */
    private void updateDailyRewardUI()
    {
        dailyRewardButton.setVisible(!mainMenuModel.getSession().hasClaimedRewardToday());
    }
}