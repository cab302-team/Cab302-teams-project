package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.services.TileControllerFactory;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * tile group that observes an observable list and updates the ui Tile Controller's nodes into their EmptyTileSlotController nodes.
 */
public class LetterTileGroup
{
    private final List<EmptyTileSlotController> tileSlots = new ArrayList<>();
    private final List<LetterTileController> tileControllers = new ArrayList<>();
    private final Pane container;
    private final int numberOfEmptyTileSlots;
    private final Consumer<LetterTileController> onClickHandler;
    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();

    /**
     * Gets the groups tile controllers.
     * @return the letter tile groups controllers
     */
    public List<LetterTileController> getControllers(){
        return tileControllers;
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
                           ReadOnlyListProperty<LetterTile> observedList,
                           Consumer<LetterTileController> onClickHandler,
                           List<Runnable> afterSyncActions)
    {
        this(numberOfEmptyTileSlots, container, observedList, onClickHandler);

        observedList.addListener((obs, oldVal, newVal) -> {
            afterSyncActions.forEach(Runnable::run);
        });
    }

    /**
     * Constructor
     * @param numberOfEmptyTileSlots number of max tiles in group (empty slots)
     * @param container container to place all in.
     * @param observedList the observed list.
     * @param onClickHandler On tile click action.
     */
    public LetterTileGroup(int numberOfEmptyTileSlots, Pane container,
                           ReadOnlyListProperty<LetterTile> observedList,
                           Consumer<LetterTileController> onClickHandler)
    {
        this.container = container;
        this.numberOfEmptyTileSlots = numberOfEmptyTileSlots;
        this.onClickHandler = onClickHandler;

        createEmptySlots();

        observedList.addListener((obs, oldVal, newVal) -> {
            syncLetterTiles(newVal);
        });

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

        controller.setOnTileClicked(onClickHandler);

        container.getChildren().add(controller.getRoot());
        return controller;
    }

    /**
     * Regenerate letter tiles as observed from the model.
     */
    private void syncLetterTiles(ObservableList<LetterTile> modelList)
    {
        tileControllers.clear();

        for (LetterTile tile : modelList)
        {
            var controller = this.tileControllerFactory.createLetterTileController(tile);
            controller.getRoot().setOnMouseClicked(e -> onClickHandler.accept(controller));
            tileControllers.add(controller);
        }

        // Clear all word area slots
        for (EmptyTileSlotController rowsEmptyTile : tileSlots) {
            rowsEmptyTile.clearLetterTile();
        }

        for (int i = 0; i < tileControllers.size(); i++){
            tileSlots.get(i).setLetter(tileControllers.get(i));
        }
    }
}
