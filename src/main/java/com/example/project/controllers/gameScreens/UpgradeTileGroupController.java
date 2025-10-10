package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.UpgradeTileController;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.TileControllerFactory;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui nodes for some game tiles.
 */
public class UpgradeTileGroupController
{
    private final Pane container;
    private final List<UpgradeTileController> tileControllers = new ArrayList<>();
    private Consumer<UpgradeTileModel> onClickAction = null;
    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();

    /**
     * Constructor.
     * @param container container all tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     * @param onClickAction action called on click.
     */
    public UpgradeTileGroupController(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList, Consumer<UpgradeTileModel> onClickAction)
    {
        this.container = container;
        this.onClickAction = onClickAction;
        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));
        syncTiles(observedList);
    }

    /**
     * Constructor with no additional parameter.
     * @param container container tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     */
    public UpgradeTileGroupController(Pane container, ReadOnlyListProperty<UpgradeTileModel> observedList)
    {
        this.container = container;
        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));
        syncTiles(observedList);
    }

    private void syncTiles(ObservableList<UpgradeTileModel> modelList)
    {
//        container.getChildren().clear();
//        tileControllers.clear();
//
//        for (UpgradeTileModel tile : modelList)
//        {
//            var controller = tileControllerFactory.createUpgradeTileController(tile);
//            tileControllers.add(controller);
//            var root = controller.getRoot();
//
//            if (onClickAction != null){
//                root.setOnMouseClicked(e -> onClickAction.accept(tile));
//            }
//
//            container.getChildren().add(controller.getRoot());
//        }
    }
}
