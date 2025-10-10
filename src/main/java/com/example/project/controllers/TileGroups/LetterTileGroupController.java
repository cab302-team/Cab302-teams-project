package com.example.project.controllers.TileGroups;

import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTile;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui Tile Controller's nodes into their EmptyTileSlotController nodes.
 */
public class LetterTileGroupController extends TileGroupController<LetterTile, LetterTileController>
{
    private final List<EmptyTileSlotController> tileSlots = new ArrayList<>();
    private final int numberOfEmptyTileSlots;

    /**
     * Constructor
     * @param numberOfEmptyTileSlots number of max tiles in group (empty slots)
     * @param container container to place all in.
     * @param observedList the observed list.
     * @param onClickHandler On tile click action.
     * @param afterSyncActions additional synchronisation actions that need to happen when this observed list changes.
     */
    public LetterTileGroupController(int numberOfEmptyTileSlots, Pane container,
                                     ReadOnlyListProperty<LetterTile> observedList,
                                     Consumer<LetterTileController> onClickHandler,
                                     List<Runnable> afterSyncActions)
    {
        this(numberOfEmptyTileSlots, container, observedList, onClickHandler);
        observedList.addListener((obs, oldVal, newVal) -> afterSyncActions.forEach(Runnable::run));
    }

    /**
     * Constructor
     * @param numberOfEmptyTileSlots number of max tiles in group (empty slots)
     * @param container container to place all in.
     * @param observedList the observed list.
     * @param onClickAction On tile click action.
     */
    public LetterTileGroupController(int numberOfEmptyTileSlots, Pane container,
                                     ReadOnlyListProperty<LetterTile> observedList,
                                     Consumer<LetterTileController> onClickAction)
    {
        super(container, onClickAction);
        this.numberOfEmptyTileSlots = numberOfEmptyTileSlots;

        createEmptySlots();
        observedList.addListener((obs, oldVal, newVal) -> syncLetterTiles(newVal));
        syncLetterTiles(observedList);
    }

    /**
     * Create empty slots for both word area and tile rack
     */
    private void createEmptySlots()
    {
        for (var i = 0; i < numberOfEmptyTileSlots; i++)
        {
            var emptyTileController = loadEmptySlotIntoContainer();
            tileSlots.add(emptyTileController);
        }
    }

    /**
     * Load a new empty slot into a container
     */
    private EmptyTileSlotController loadEmptySlotIntoContainer()
    {
        var emptyTile = new EmptyTileSlotModel();
        EmptyTileSlotController controller = tileControllerFactory.createEmptyTileController(emptyTile);
        container.getChildren().add(controller.getRoot());
        return controller;
    }

    /**
     * Regenerate letter tiles as observed from the model.
     * TODO: use observer pattern on tiles instead.
     */
    private void syncLetterTiles(ObservableList<LetterTile> modelList)
    {
        tileControllers.clear();

        for (LetterTile tile : modelList)
        {
            var controller = this.tileControllerFactory.createLetterTileController(tile);
            controller.getRoot().setOnMouseClicked(e -> onClickAction.accept(controller));
            tileControllers.add(controller);
        }

        // Clear all word area slots
        for (EmptyTileSlotController rowsEmptyTile : tileSlots) {
            rowsEmptyTile.setLetter(null);
        }

        for (int i = 0; i < tileControllers.size(); i++){
            tileSlots.get(i).setLetter(tileControllers.get(i));
        }
    }
}
