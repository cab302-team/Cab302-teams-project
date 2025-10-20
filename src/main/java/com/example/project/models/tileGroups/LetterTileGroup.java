package com.example.project.models.tileGroups;

import com.example.project.controllers.tiles.EmptyTileSlotController;
import com.example.project.controllers.tiles.LetterTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTileModel;
import com.example.project.controllers.tiles.TileControllerFactory;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui Tile Controller's nodes into their EmptyTileSlotController nodes.
 */
public class LetterTileGroup extends TileGroup<LetterTileModel, LetterTileController>
{
    private final List<EmptyTileSlotController> tileSlots = new ArrayList<>();
    private final int numberOfEmptyTileSlots;

    protected LetterTileGroup(int numberOfEmptyTileSlots, Pane container,
                              ReadOnlyListProperty<LetterTileModel> observedList,
                              Consumer<LetterTileController> onClickHandler,
                              List<Runnable> afterSyncActions,
                              TileControllerFactory factory)
    {
        super(container, onClickHandler, LetterTileController.class, observedList);
        this.tileControllerFactory = factory;
        observedList.addListener((obs, oldVal, newVal) -> afterSyncActions.forEach(Runnable::run));
        this.numberOfEmptyTileSlots = numberOfEmptyTileSlots;
        createEmptySlots();
    }

    /**
     * Constructor
     * @param numberOfEmptyTileSlots number of max tiles in group (empty slots)
     * @param container container to place all in.
     * @param observedList the observed list.
     * @param onClickHandler On tile click action.
     * @param afterSyncActions additional synchronisation actions that need to happen when this observed list changes.
     */
    public LetterTileGroup(int numberOfEmptyTileSlots, Pane container,
                           ReadOnlyListProperty<LetterTileModel> observedList,
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
    public LetterTileGroup(int numberOfEmptyTileSlots, Pane container,
                           ReadOnlyListProperty<LetterTileModel> observedList,
                           Consumer<LetterTileController> onClickAction)
    {
        super(container, onClickAction, LetterTileController.class, observedList);
        this.numberOfEmptyTileSlots = numberOfEmptyTileSlots;
        createEmptySlots();
    }

    /**
     * Create empty slots for both word area and tile rack
     */
    private void createEmptySlots()
    {
        tileSlots.clear();

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
        // TODO: this model should store empty tiles and the controller of this should observe those.
        var emptyTile = new EmptyTileSlotModel();
        EmptyTileSlotController controller = tileControllerFactory.createTileController(emptyTile, EmptyTileSlotController.class);
        container.getChildren().add(controller.getRoot());
        return controller;
    }

    @Override
    protected void updateVisuals()
    {
        // update visuals
        for (EmptyTileSlotController rowsEmptyTile : tileSlots) {
            rowsEmptyTile.setLetter(null);
        }

        for (int i = 0; i < tileControllers.size(); i++){
            tileSlots.get(i).setLetter(tileControllers.get(i));
        }
    }
}
