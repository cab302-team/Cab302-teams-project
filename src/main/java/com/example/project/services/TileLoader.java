package com.example.project.services;

import com.example.project.Application;
import com.example.project.controllers.tileViewControllers.EmptyTileController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * class to load the tile and bind model to the controller.
 */
public class TileLoader
{
    /**
     * create tile controller.
     * @param tileObject tile object.
     * @param <C> class of tile controller.
     * @param <T> class of tile.
     * @return returns controller of the tile.
     */
    private static <C extends TileController<T>, T extends Tile> C createTileController(T tileObject)
    {
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

    /**
     * @param upgradeTile upgrade tile model.
     * @return new upgrade tile controller.
     */
    public static UpgradeTileViewController createUpgradeTile(UpgradeTile upgradeTile)
    {
        UpgradeTileViewController upgradeTileController = createTileController(upgradeTile);
        var tooltip = new Tooltip(String.format("%s: %s %n $%.2f", upgradeTile.getName(), upgradeTile.getDescription(),
         upgradeTile.getCost()));

        tooltip.setStyle("-fx-font-size: 16px; -fx-font-family: Arial;"); // TODO: to go in upgrade-tile-styles.css
        tooltip.setShowDelay(Duration.seconds(0));
        Tooltip.install(upgradeTileController.getRoot(), tooltip);

        var pane = upgradeTileController.getRoot();

        pane.setOnMouseEntered(e -> {
            pane.setScaleX(1.1);
            pane.setScaleY(1.1);
            upgradeTileController.getModel().getHoverSoundPlayer().play();
        });

        pane.setOnMouseExited(e -> {
            pane.setScaleX(1.0);
            pane.setScaleY(1.0);
        });

        return upgradeTileController;
    }

    /**
     * @param lt letter tile model.
     * @return returns letter tile controller.
     */
    public static LetterTileController createLetterTile(LetterTile lt)
    {
        LetterTileController controller = createTileController(lt);

        var pane = controller.getRoot();

        pane.setOnMouseEntered(e -> {
            pane.setScaleX(1.1);
            pane.setScaleY(1.1);
            controller.getModel().getHoverSoundPlayer().play();
        });

        pane.setOnMouseExited(e -> {
            pane.setScaleX(1.0);
            pane.setScaleY(1.0);
        });

        return controller;
    }

    /**
     * @param emptyTile empty tile model.
     * @return returns empty tile controller.
     */
    public static EmptyTileController createEmptyTileController(EmptyTileSlot emptyTile){
        return createTileController(emptyTile);
    }
}
