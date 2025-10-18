package com.example.project.models.tileGroups;

import com.example.project.controllers.tiles.UpgradeTileController;
import com.example.project.models.tiles.UpgradeTileModel;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.layout.Pane;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui nodes for some game tiles.
 */
public class UpgradeTileGroup extends TileGroup<UpgradeTileModel, UpgradeTileController>
{
    /**
     * Constructor.
     * @param container container all tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     * @param onClickAction additional synchronisation actions that need to be called.
     */
    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList, Consumer<UpgradeTileController> onClickAction)
    {
        super(container, onClickAction, UpgradeTileController.class, observedList);
    }

    /**
     * Constructor with no additional sync action parameter.
     * @param container container tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     */
    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList)
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
