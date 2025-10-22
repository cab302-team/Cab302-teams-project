package com.example.project.controllers.tiles;

import com.example.project.models.tiles.EmptyTileSlotModel;
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

    public void bind(EmptyTileSlotModel modelToUse)
    {
        this.model = modelToUse;
    }

    private LetterTileController letterTileController;

    public Node getRoot()
    {
        return root;
    }

    /**
     * updates the tile slot with a new letter tile controller.
     * @param letterController the letter tiles controller.
     */
    public void setLetter(LetterTileController letterController)
    {
        if (this.model == null){
            throw new RuntimeException("model was null. call bind first.");
        }

        this.model.setTile(letterController == null ? null : letterController.getModel());
        letterTileController = letterController;
        updateView();
    }

    private void updateView()
    {
        slotForLetterTile.getChildren().clear();

        if (this.letterTileController != null){
            slotForLetterTile.getChildren().add(this.letterTileController.getRoot());
        }
    }
}
