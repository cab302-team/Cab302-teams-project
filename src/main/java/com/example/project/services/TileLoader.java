package com.example.project.services;

import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.models.tiles.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class TileLoader
{
    /**
     * create tile controller.
     * @param tileObject tile object.
     * @param <C> class of tile controller.
     * @param <T> class of tile.
     * @return returns controller of the tile.
     */
    public static <C extends TileController<T>, T extends Tile> C createTileController(T tileObject) {
        try
        {
            String fxmlPath = tileObject.getFXMLPath();
            FXMLLoader loader = new FXMLLoader(TileLoader.class.getResource(fxmlPath));
            Node node = loader.load();
            C controller = loader.getController();
            controller.bind(tileObject);
            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tile controller: " + tileObject.getFXMLPath(), e);
        }
    }
}
