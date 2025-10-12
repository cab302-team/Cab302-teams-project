package com.example.project.models.tileGroups;

import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.models.tiles.TileModel;
import com.example.project.services.TileControllerFactory;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Tile Group abstract class holds a row or column of tiles.
 * @param <modelType> tile model type.
 * @param <controllerType> controller type.
 */
public abstract class TileGroup<modelType extends TileModel, controllerType extends TileController<modelType>>
{
    protected Pane container;
    protected final List<controllerType> tileControllers = new ArrayList<>();
    protected TileControllerFactory tileControllerFactory = new TileControllerFactory();
    protected Consumer<controllerType> onClickAction = null;
    private final Class<controllerType> controllerClass;
    private final ReadOnlyListProperty<modelType> observedModels;

    TileGroup(Pane container, Consumer<controllerType> onClickAction, Class<controllerType> tileControllerClass, ReadOnlyListProperty<modelType> observedList)
    {
        this(container, tileControllerClass, observedList);
        this.onClickAction = onClickAction;
    }

    TileGroup(Pane container, Class<controllerType> tileControllerClass, ReadOnlyListProperty<modelType> observedList)
    {
        this.container = container;
        this.controllerClass = tileControllerClass;
        this.observedModels = observedList;
        this.observedModels.addListener((obs, oldVal, newVal) -> syncTiles());
    }

    private void recreateControllers()
    {
        tileControllers.clear();
        for (modelType tile : this.observedModels)
        {
            controllerType controller = tileControllerFactory.createTileController(tile, controllerClass);
            tileControllers.add(controller);

            if (onClickAction != null){
                controller.getRoot().setOnMouseClicked(e -> onClickAction.accept(controller));
            }
        }
    }

    protected abstract void updateVisuals();

    /**
     * Sync tiles in this tile groups controller.
     */
    public void syncTiles()
    {
        recreateControllers();
        updateVisuals();
    }

    /**
     * Gets the groups tile controllers.
     * @return the letter tile groups controllers
     */
    public List<controllerType> getControllers(){
        return tileControllers;
    }
}
