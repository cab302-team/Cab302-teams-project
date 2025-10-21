package com.example.project.controllers.gameScreens;

import com.example.project.services.Logger;
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

    protected record SidebarLoaded(Parent node, SidebarController controller){}

    protected SidebarLoaded loadSidebar(){
        // TODO: put in parent return a sidebar node return a sidebarController. OR put static load method into controller?. or make it a singleton.
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
