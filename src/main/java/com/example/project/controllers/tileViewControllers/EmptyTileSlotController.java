package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.services.TileControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

/**
 * Controller for an empty tile slot, used to display and update the visual state of a LetterTile.
 * It observes changes in the model and updates the view automatically using the observer pattern.
 */
public class EmptyTileSlotController extends TileController<EmptyTileSlotModel>
{
    @FXML
    protected StackPane root;

    @FXML
    protected StackPane slotForLetterTile;

    private LetterTileController letterTileController;

    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();

    private Consumer<LetterTileController> onTileClicked;

    /**
     * Binds the model to this controller and listens for tile changes.
     * When the model's tile changes, the view updates automatically.
     *
     * @param modelToUse The model to observe.
     */
    public void bind(EmptyTileSlotModel modelToUse) {
        this.model = modelToUse;

        model.tileProperty().addListener((obs, oldTile, newTile) -> updateView());

        updateView(); // Initialize view based on initial tile
    }

    public Node getRoot() {
        return root;
    }

    /**
     * Sets a handler to run when the tile is clicked.
     * This is typically passed down from LetterTileGroup to enable game interaction logic.
     *
     * @param handler A consumer function that accepts a LetterTileController when clicked.
     */
    public void setOnTileClicked(Consumer<LetterTileController> handler) {
        this.onTileClicked = handler;
    }

    /**
     * Clears the letter tile visually and in the model.
     */
    public void clearLetterTile() {
        if (this.model == null) {
            throw new RuntimeException("Model was null. Call bind() first.");
        }

        this.model.setTile(null); // Triggers view update via listener
        letterTileController = null;
    }

    /**
     * Updates the view based on the current tile in the model.
     * If a tile is present, it creates and binds a LetterTileController.
     */
    private void updateView() {
        slotForLetterTile.getChildren().clear();

        LetterTile tile = model.getTile();

        if (tile != null) {
            // Reuse if we already have a controller for this tile
            if (letterTileController == null || letterTileController.getModel() != tile) {
                // Use the factory to create controller only once
                this.letterTileController = tileControllerFactory.createLetterTileController(tile);

                // Set click behavior
                letterTileController.getRoot().setOnMouseClicked(e -> {
                    if (onTileClicked != null) {
                        onTileClicked.accept(letterTileController);
                    }
                });
            }

            slotForLetterTile.getChildren().add(letterTileController.getRoot());
        } else {
            this.letterTileController = null;
        }
    }

    // ---------- Unit test helpers ----------

    protected void setRoot(StackPane root) {
        this.root = root;
    }

    protected void setSlotForLetterTile(StackPane slot) {
        this.slotForLetterTile = slot;
    }
}