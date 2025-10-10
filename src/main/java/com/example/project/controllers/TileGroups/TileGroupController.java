package com.example.project.controllers.TileGroups;

import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.models.tiles.TileModel;
import com.example.project.services.TileControllerFactory;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * Controller that shows the tiles in a row or column. use for the word row, tile rack and redraw window column.
 * May or may not have empty tile slots.?
 * @param <modelType> tile model.
 */
public abstract class TileGroupController<modelType extends TileModel>
{
    private final Pane container;
    private final List<TileController<modelType>> tileControllers = new ArrayList<>();
    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();
    private Consumer<modelType> onClickAction = null;

    /**
     * Constructor.
     * @param container container all tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     * @param onClickAction action to call on tile clicked.
     */
    public TileGroupController(Pane container, ReadOnlyListProperty<modelType> observedList, Consumer<modelType> onClickAction)
    {
        this.container = container;
        this.onClickAction = onClickAction;

        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));
        syncTiles(observedList);
    }

    /**
     * Constructor with no additional sync action parameter.
     * @param container container tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     */
    public TileGroupController(Pane container, ReadOnlyListProperty<modelType> observedList)
    {
        this.container = container;
        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));
        syncTiles(observedList);
    }

    private void syncTiles(ObservableList<modelType> modelList)
    {
        // TODO: Remove this tile controllers observe their models.

        container.getChildren().clear();
        tileControllers.clear();

        for (modelType tile : modelList)
        {
            var controller = tileControllerFactory.createTileController(tile);
            tileControllers.add(controller);
            var root = controller.getRoot();

            if (onClickAction != null)
            {
                root.setOnMouseClicked(e -> onClickAction.accept(tile));
            }

            container.getChildren().add(controller.getRoot());
        }
    }
}
