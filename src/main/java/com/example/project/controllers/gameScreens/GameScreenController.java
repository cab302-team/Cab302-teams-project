package com.example.project.controllers.gameScreens;

import com.example.project.services.Logger;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Game screen controller that has some startup on screen thing todo. Level, shop controllers.
 */
public abstract class GameScreenController
{
    protected Logger logger = new Logger();

    protected GameScreenController() {}

    /**
     * Called when the game scene is changed to one of the GameScenes Login, Level, Shop etc.
     */
    public abstract void onSceneChangedToThis();

    /**
     * create models and inject session and scene manager.
     * @param session session.
     * @param sceneManager scene manager.
     */
    public abstract void setup(Session session, SceneManager sceneManager);

    protected record SidebarLoaded(Parent node, SidebarController controller) {}

    protected SidebarLoaded loadSidebar()
    {
        var sidebarPath = "/com/example/project/gameScreens/sidebar.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sidebarPath));
        Parent sidebarNode = null;
        try{
            sidebarNode = loader.load();
        }
        catch (IOException exception){
            this.logger.logError("failed to load sidebar fxml file.");
        }

        SidebarLoaded side = new SidebarLoaded(sidebarNode, loader.getController());
        return side;
    }
}
