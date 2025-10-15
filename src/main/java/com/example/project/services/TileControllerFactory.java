package com.example.project.services;

import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTileModel;
import com.example.project.models.tiles.TileModel;
import com.example.project.models.tiles.UpgradeTileModel;
import javafx.scene.Node;

import java.io.IOException;

/**
 * class to load the tile model controller for that tile model type.
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
            loader.load(fxmlPath);
            C controller = loader.getController();
            controller.setModel(tileObject);
            controller.bind(tileObject);
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create tile controller: " + tileObject.getFXMLPath(), e);
        }
    }

    private void addHoverEffects(Node pane, Runnable onEnter)
    {
        pane.setOnMouseEntered(e -> {
            pane.setScaleX(1.1);
            pane.setScaleY(1.1);
            onEnter.run();
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
    private UpgradeTileController createUpgradeTileController(UpgradeTileModel upgradeTile)
    {
        UpgradeTileController upgradeTileController = createGenericTileController(upgradeTile);
        var pane = upgradeTileController.getRoot();
        addHoverEffects(pane, () -> upgradeTileController.getModel().getHoverSoundPlayer().replay());
        return upgradeTileController;
    }

    /**
     * Returns controller of type.
     * @param tile tile model.
     * @param controllerType controller type.
     * @return return controller.
     * @param <C> controller type.
     * @param <T> tile type.
     */
    public <C extends TileController<T>, T extends TileModel> C createTileController(T tile, Class<C> controllerType)
    {
        var controller = switch (tile)
        {
            case UpgradeTileModel upgradeTileModel -> createUpgradeTileController(upgradeTileModel);
            case LetterTileModel letterTile -> createLetterTileController(letterTile);
            case EmptyTileSlotModel emptyTileSlotModel -> createEmptyTileController(emptyTileSlotModel);
            default -> throw new IllegalArgumentException("Unsupported tile type: " + tile.getClass());
        };

        return controllerType.cast(controller);
    }

    /**
     * @param lt letter tile model.
     * @return returns letter tile controller.
     */
    public LetterTileController createLetterTileController(LetterTileModel lt)
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
    private EmptyTileSlotController createEmptyTileController(EmptyTileSlotModel emptyTile){
        return createGenericTileController(emptyTile);
    }
}
