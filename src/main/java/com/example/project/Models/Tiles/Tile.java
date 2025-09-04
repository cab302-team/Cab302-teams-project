package com.example.project.Models.Tiles;

import com.example.project.Models.IUIModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import com.example.project.Controllers.TileViewControllers.TileController;

/**
 * Parent tile class for objects in the scene that are tiles. Upgrades and tiles.
 */
public abstract class Tile implements IUIModel
{
    /**
     * Tile size.
     */
    public static int TILE_SIZE = 50;

    /**
     * @return returns FXML file name.
     */
    public abstract String getFXMLFile();

    @Override
    public Node getUIElement()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFXMLFile()));

        StackPane tilePane = null;
        try
        {
            tilePane = fxmlLoader.load();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        tilePane.setPrefSize(50, 50);
        TileController<Tile> controller = fxmlLoader.getController();
        if (controller == null){
            throw new RuntimeException(String.format("Couldnt get controller for %s", getFXMLFile()));
        }

        controller.setUIValues(this);
        return tilePane;
    }
}
