package com.example.project.controllers.tiles;

import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTileModel;
import com.example.project.controllers.tiles.TileControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Empty Tile controller for EmptyTile view.
 */
public class EmptyTileSlotController extends TileController<EmptyTileSlotModel>
{
    @FXML
    protected StackPane root;

    @FXML
    protected StackPane slotForLetterTile;

    private final TileControllerFactory tileFactory = new TileControllerFactory();
    private LetterTileController letterTileController;

    public Node getRoot() {
        return root;
    }

    @Override
    public void bind(EmptyTileSlotModel modelToUse) {
        this.model = modelToUse;

        // Observe model's tile property
        this.model.currentTileProperty().addListener((obs, oldTile, newTile) -> {
            if (newTile == null) {
                this.letterTileController = null;
            } else {
                LetterTileModel tile = (LetterTileModel) newTile;
                this.letterTileController = tileFactory.createLetterTileController(tile);
                this.letterTileController.bind(tile);
            }
            updateView();
        });

        // Initial render
        if (model.getTile() != null) {
            LetterTileModel tile = model.getTile();
            this.letterTileController = tileFactory.createLetterTileController(tile);
            this.letterTileController.bind(tile);
        }
        updateView();
    }

    private void updateView() {
        slotForLetterTile.getChildren().clear();
        if (letterTileController != null) {
            slotForLetterTile.getChildren().add(letterTileController.getRoot());
        }
    }
}
