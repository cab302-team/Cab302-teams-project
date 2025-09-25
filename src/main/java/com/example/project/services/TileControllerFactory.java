package com.example.project.services;

import com.example.project.controllers.tileViewControllers.EmptyTileController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 * class to load the tile and bind model to the controller.
 */
public class TileControllerFactory
{
    private FXMLPageLoader loader = new FXMLPageLoader();

    /**
     * Creates instance of {@link TileControllerFactory}.
     */
    public TileControllerFactory() {}

    protected TileControllerFactory(FXMLPageLoader loader)
    {
        this.loader = loader;
    }

    /**
     * create tile controller.
     * @param tileObject tile object.
     * @param <C> class of tile controller.
     * @param <T> class of tile.
     * @return returns controller of the tile.
     */
    private <C extends TileController<T>, T extends Tile> C createTileController(T tileObject)
    {
        try
        {
            String fxmlPath = tileObject.getFXMLPath();
            Node node = loader.load(fxmlPath);
            C controller = loader.getController();
            controller.setModel(tileObject);
            controller.bind(tileObject);
            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tile controller: " + tileObject.getFXMLPath(), e);
        }
    }

    private void addHoverEffects(Node pane, Runnable onEnter)
    {
        pane.setOnMouseEntered(e -> {
            pane.setScaleX(1.1);
            pane.setScaleY(1.1);
            if (onEnter != null) onEnter.run();
        });
        pane.setOnMouseExited(e -> {
            pane.setScaleX(1.0);
            pane.setScaleY(1.0);
        });
    }

    /**
     * @param upgradeTile upgrade tile model.
     * @return new upgrade tile controller.
     */
    public UpgradeTileController createUpgradeTileController(UpgradeTile upgradeTile)
    {
        UpgradeTileController upgradeTileController = createTileController(upgradeTile);

        var pane = upgradeTileController.getRoot();

        addHoverEffects(pane, () -> upgradeTileController.getModel().getHoverSoundPlayer().replay());
        return upgradeTileController;
    }

    /**
     * @param lt letter tile model.
     * @return returns letter tile controller.
     */
    public LetterTileController createLetterTileController(LetterTile lt)
    {
        LetterTileController controller = createTileController(lt);
        var pane = controller.getRoot();
        addHoverEffects(pane, () -> controller.getModel().getHoverSoundPlayer().replay());
        return controller;
    }

    /**
     * @param emptyTile empty tile model.
     * @return returns empty tile controller.
     */
    public EmptyTileController createEmptyTileController(EmptyTileSlot emptyTile){
        return createTileController(emptyTile);
    }
}
