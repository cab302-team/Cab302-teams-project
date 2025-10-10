package com.example.project.controllers.TileGroups;

import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.UpgradeTileModel;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.layout.Pane;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui nodes for some game tiles.
 */
public class UpgradeTileGroupController extends TileGroupController<UpgradeTileModel, UpgradeTileController>
{
    /**
     * Constructor.
     * @param container container all tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     * @param afterSyncActions additional synchronisation actions that need to be called.
     */
    public UpgradeTileGroupController(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList, Consumer<UpgradeTileController> afterSyncActions)
    {
        super(container, afterSyncActions, UpgradeTileController.class, observedList);
    }

    /**
     * Constructor with no additional sync action parameter.
     * @param container container tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     */
    public UpgradeTileGroupController(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList)
    {
        super(container, UpgradeTileController.class, observedList);
    }

    @Override
    protected void updateVisuals()
    {
        container.getChildren().clear();

        // update visuals
        for (var controller : tileControllers)
        {
            container.getChildren().add(controller.getRoot());
        }
    }
}
