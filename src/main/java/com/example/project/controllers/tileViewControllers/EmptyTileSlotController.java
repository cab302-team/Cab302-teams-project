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

        updateView(); // Initialize view
    }

    /**
     * Returns the root node for this controller (used by FXML loaders).
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Sets a click handler that is called when the tile in this slot is clicked.
     *
     * @param handler A consumer that receives the current LetterTileController.
     */
    public void setOnTileClicked(Consumer<LetterTileController> handler) {
        this.onTileClicked = handler;
    }

    /**
     * Sets the letter tile controller directly (used to preserve animations).
     *
     * @param controller the controller to display in this slot.
     */
    public void setLetter(LetterTileController controller) {
        if (this.model == null)
            throw new RuntimeException("model was null. call bind() first.");

        this.model.setTile(controller.getModel()); // Still updates the model
        this.letterTileController = controller;

        slotForLetterTile.getChildren().clear();
        slotForLetterTile.getChildren().add(controller.getRoot());
    }

    /**
     * Clears the letter tile visually and in the model.
     */
    public void clearLetterTile() {
        if (this.model == null) {
            throw new RuntimeException("Model was null. Call bind() first.");
        }

        this.model.setTile(null); // This triggers updateView()
        letterTileController = null;
    }

    /**
     * Updates the view when the model's tile changes.
     * Reuses the controller for animation compatibility and sets up click behavior.
     */
    private void updateView() {
        slotForLetterTile.getChildren().clear();
        LetterTile tile = model.getTile();

        if (tile != null) {
            // Reuse controller if it’s for the same tile
            if (letterTileController == null || letterTileController.getModel() != tile) {
                letterTileController = tileControllerFactory.createLetterTileController(tile);
            }

            // Make sure the tile is clickable
            if (onTileClicked != null) {
                letterTileController.getRoot().setOnMouseClicked(e -> onTileClicked.accept(letterTileController));
            }

            slotForLetterTile.getChildren().add(letterTileController.getRoot());
        } else {
            letterTileController = null;
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