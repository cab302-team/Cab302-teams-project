package com.example.project.controllers.TileGroups;

import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.models.tiles.TileModel;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Tile Group abstract class.
 * @param <modelType> tile model type.
 * @param <controllerType> controller type.
 */
public abstract class TileGroup<modelType extends TileModel, controllerType extends TileController<modelType>>
{
    protected Pane container;
    protected final List<controllerType> tileControllers = new ArrayList<>();

    TileGroup(Pane container)
    {
        this.container = container;
    }

    /**
     * Gets the groups tile controllers.
     * @return the letter tile groups controllers
     */
    public List<controllerType> getControllers(){
        return tileControllers;
    }
}
