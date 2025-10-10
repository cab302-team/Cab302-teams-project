package com.example.project.services;

import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.TileModel;
import com.example.project.models.tiles.UpgradeTileModel;
import javafx.scene.Node;

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
    private <C extends TileController<T>, T extends TileModel> C createGenericTileController(T tileObject)
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
    private UpgradeTileController createTileController(UpgradeTileModel upgradeTile)
    {
        UpgradeTileController upgradeTileController = createGenericTileController(upgradeTile);

        var pane = upgradeTileController.getRoot();

        addHoverEffects(pane, () -> upgradeTileController.getModel().getHoverSoundPlayer().replay());
        return upgradeTileController;
    }

    /**
     * @param lt letter tile model.
     * @return returns letter tile controller.
     */
    private LetterTileController createTileController(LetterTile lt)
    {
        LetterTileController controller = createGenericTileController(lt);
        var pane = controller.getRoot();
        addHoverEffects(pane, () -> controller.getModel().getHoverSoundPlayer().replay());
        return controller;
    }

    /**
     * @param emptyTile empty tile model.
     * @return returns empty tile controller.
     */
    private EmptyTileSlotController createTileController(EmptyTileSlotModel emptyTile){
        return createGenericTileController(emptyTile);
    }


    /**
     * Creates tile controller of model type T.
     * @param tile tile model.
     * @param <T> Tile Type T.
     * @return controller.
     */
    public <T extends TileModel> TileController<T> createTileController(T tile)
    {
        return switch (tile) {
            case UpgradeTileModel upgradeTileModel -> (TileController<T>) createTileController(upgradeTileModel);
            case LetterTile letterTile -> (TileController<T>) createTileController(letterTile);
            case EmptyTileSlotModel emptyTileSlotModel -> (TileController<T>) createTileController(emptyTileSlotModel);
            default -> throw new IllegalArgumentException("Unsupported tile type: " + tile.getClass());
        };
    }
}
