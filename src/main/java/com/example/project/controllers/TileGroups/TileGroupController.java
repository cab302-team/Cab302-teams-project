package com.example.project.controllers.TileGroups;

import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTileModel;
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
 * @param <controllerType> controller type.
 */
public class TileGroupController<modelType extends TileModel, controllerType extends TileController<modelType>>
{
    private final Pane container;
    private final List<controllerType> tileControllers = new ArrayList<>();
    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();
    private Consumer<controllerType> onClickAction = null;
    private final List<EmptyTileSlotController> tileSlots = new ArrayList<>();
    private int numberOfEmptyTileSlots = 0;

    /**
     * Constructor.
     * @param container container all tiles are in.
     * @param observedList the observed list that when changes this tile group will sync.
     * @param onClickAction action to call on tile clicked.
     */
    public TileGroupController(Pane container, ReadOnlyListProperty<modelType> observedList, Consumer<controllerType> onClickAction)
    {
        this.container = container;
        this.onClickAction = onClickAction;

        observedList.addListener((obs, oldVal, newVal) -> syncTiles(newVal));
        syncTiles(observedList);
    }

    /**
     * constructor.
     * @param numberOfEmptyTileSlots
     * @param container
     * @param observedList
     * @param onClickHandler
     * @param afterSyncActions
     */
    public TileGroupController(int numberOfEmptyTileSlots, Pane container,
                                     ReadOnlyListProperty<LetterTileModel> observedList,
                                     Consumer<controllerType> onClickHandler,
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
    public TileGroupController(int numberOfEmptyTileSlots, Pane container,
                                     ReadOnlyListProperty<LetterTileModel> observedList,
                                     Consumer<controllerType> onClickHandler)
    {
        this.container = container;
        this.numberOfEmptyTileSlots = numberOfEmptyTileSlots;
        this.onClickAction = onClickHandler;

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
        EmptyTileSlotController controller = tileControllerFactory.createTileController(emptyTile);
        container.getChildren().add(controller.getRoot());
        return controller;
    }

    /**
     * Regenerate letter tiles as observed from the model.
     */
    private void syncLetterTiles(ObservableList<LetterTileModel> modelList)
    {
        tileControllers.clear();

        for (LetterTileModel tile : modelList)
        {
            var controller = this.tileControllerFactory.createLetterTileController(tile);
            controller.getRoot().setOnMouseClicked(e -> onClickHandler.accept(controller));
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

    /**
     * Gets the controllers that this group stores.
     * @return list.
     */
    public List<controllerType> getControllers()
    {
        return tileControllers;
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

    private void syncTiles()
    {
        // TODO: Remove this tile controllers observe their models.
        container.getChildren().clear();
        tileControllers.clear();

        for (controllerType controller : tileControllers)
        {
            tileControllers.add(controller);
            var root = controller.getRoot();

            if (onClickAction != null)
            {
                root.setOnMouseClicked(e -> onClickAction.accept(controller));
            }

            container.getChildren().add(controller.getRoot());
        }
    }
}
