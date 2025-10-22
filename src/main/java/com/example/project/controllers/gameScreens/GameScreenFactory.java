package com.example.project.controllers.gameScreens;

import com.example.project.controllers.RootLayoutController;
import com.example.project.services.*;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * create game screen controllers.
 */
public class GameScreenFactory
{
    private final Session session;
    private SceneManager sceneManager;
    private final Map<GameScene, Parent> pages = new HashMap<>();
    private final Map<GameScene, GameScreenController> controllers = new HashMap<>();

    /**
     * Constructor.
     * @param session session.
     */
    public GameScreenFactory(Session session)
    {
        this.session = session;
    }

    /**
     * Load all main game screens.
     * @param rootController root.
     * @param loader loader.
     */
    public void loadGameScreens(RootLayoutController rootController, FXMLPageLoader loader)
    {
        this.sceneManager = new SceneManager(rootController, controllers, pages);
        this.loadPages(loader);
        sceneManager.switchScene(GameScene.LOGIN);
    }

    private void loadPages(PageLoader loader)
    {
        preloadPage(GameScene.LOGIN, "/com/example/project/gameScreens/login-view.fxml", loader);
        preloadPage(GameScene.MAINMENU, "/com/example/project/gameScreens/main-menu-view.fxml", loader);
        preloadPage(GameScene.DAILY_REWARD, "/com/example/project/gameScreens/dailyReward-view.fxml", loader);
        preloadPage(GameScene.LEVEL, "/com/example/project/gameScreens/level-view.fxml", loader);
        preloadPage(GameScene.SHOP, "/com/example/project/gameScreens/shop-view.fxml", loader);
    }

    private void preloadPage(GameScene type, String fxmlPath, PageLoader loader)
    {
        Parent page;
        try{
            page = loader.load(fxmlPath);
        }
        catch (IOException e){
            throw new RuntimeException(String.format("Exception when loading page: %s. Message: %s, cause: %s", fxmlPath, e.getMessage(), e.getCause()));
        }
        catch (IllegalStateException e){
            throw new RuntimeException(String.format("game scene fxml path for %s not correct: %s, cause: %s", fxmlPath, e.getMessage(), e.getCause()));
        }

        if (page == null)
        {
            throw new IllegalArgumentException("Page not loaded: " + type);
        }

        GameScreenController controller = loader.getController();
        if (controller == null){
            throw new RuntimeException("must have a controller on the game screen that is a gameScreenController");
        }

        controller.setup(this.session, this.sceneManager);
        controllers.put(type, controller);
        pages.put(type, page);
    }
}
