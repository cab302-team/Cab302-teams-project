package com.example.project.controllers.TileGroups;

import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.TileControllerFactory;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui nodes for some game tiles.
 */
public class UpgradeTileGroup extends TileGroup<UpgradeTile, UpgradeTileController>
{
    private Consumer<UpgradeTile> afterSyncActions = null;
    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();

    /**
     * Constructor.
     * @param container container all tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     * @param afterSyncActions additional synchronisation actions that need to be called.
     */
    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTile> observedList, Consumer<UpgradeTile> afterSyncActions)
    {
        this(container, observedList);
        this.afterSyncActions = afterSyncActions;
    }

    /**
     * Constructor with no additional sync action parameter.
     * @param container container tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     */
    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTile> observedList)
    {
        super(container);
        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));
        syncTiles(observedList);
    }

    private void syncTiles(ObservableList<UpgradeTile> modelList)
    {
        container.getChildren().clear();
        tileControllers.clear();

        for (UpgradeTile tile : modelList)
        {
            var controller = tileControllerFactory.createUpgradeTileController(tile);
            tileControllers.add(controller);
            var root = controller.getRoot();

            if (afterSyncActions != null){
                root.setOnMouseClicked(e -> afterSyncActions.accept(tile));
            }

            container.getChildren().add(controller.getRoot());
        }
    }
}
