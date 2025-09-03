package com.example.project.Tiles;

import com.example.project.Controllers.LetterTileViewController;
import com.example.project.IUIModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import org.w3c.dom.Node;

/**
 * Parent tile class for objects in the scene that are tiles. Upgrades and tiles.
 */
public abstract class Tile implements IUIModel
{
    /**
     * @return returns FXML file name.
     */
    public abstract String getFXMLFile();
}
