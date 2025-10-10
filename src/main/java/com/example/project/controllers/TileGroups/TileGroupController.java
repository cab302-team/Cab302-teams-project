package com.example.project.controllers.TileGroups;

import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.models.tiles.TileModel;
import com.example.project.services.TileControllerFactory;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Tile Group abstract class holds a row or column of tiles.
 * @param <modelType> tile model type.
 * @param <controllerType> controller type.
 */
public abstract class TileGroupController<modelType extends TileModel, controllerType extends TileController<modelType>>
{
    protected Pane container;
    protected final List<controllerType> tileControllers = new ArrayList<>();
    protected final TileControllerFactory tileControllerFactory = new TileControllerFactory();
    protected Consumer<controllerType> onClickAction = null;


    TileGroupController(Pane container,  Consumer<controllerType> onClickAction )
    {
        this(container);
        this.onClickAction = onClickAction;
    }

    TileGroupController(Pane container)
    {
        this.container = container;
    }

    protected void recreateControllers(ObservableList<modelType> modelList)
    {
        tileControllers.clear();

        for (modelType tile : modelList)
        {
            controllerType controller = tileControllerFactory.createTileController(tile);
            tileControllers.add(controller);

            if (onClickAction != null){
                controller.getRoot().setOnMouseClicked(e -> onClickAction.accept(controller));
            }
        }
    }

    /**
     * Gets the groups tile controllers.
     * @return the letter tile groups controllers
     */
    public List<controllerType> getControllers(){
        return tileControllers;
    }
}
