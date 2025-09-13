package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.TileLoader;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui nodes for some game tiles.
 */
public class UpgradeTileGroup
{
    private final Pane container;
    private final List<UpgradeTileViewController> tileControllers = new ArrayList<>();
    Consumer<UpgradeTile> afterSyncActions = null;

    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTile> observedList, Consumer<UpgradeTile> afterSyncActions)
    {
        this.container = container;
        this.afterSyncActions = afterSyncActions;

        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));

        syncTiles(observedList);
    }

    public UpgradeTileGroup(Pane container, ReadOnlyListProperty<UpgradeTile> observedList)
    {
        this.container = container;

        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));

        syncTiles(observedList);
    }

    private void syncTiles(ObservableList<UpgradeTile> modelList)
    {
        container.getChildren().clear();
        tileControllers.clear();

        for (UpgradeTile tile : modelList)
        {
            var controller = TileLoader.createUpgradeTile(tile);
            tileControllers.add(controller);
            var root = controller.getRoot();

            if (afterSyncActions != null){
                root.setOnMouseClicked(e -> afterSyncActions.accept(tile));
            }

            container.getChildren().add(controller.getRoot());
        }
    }
}
