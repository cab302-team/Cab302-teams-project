package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.EmptyTileSlot;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Empty Tile controller for EmptyTile view.
 */
public class EmptyTileController extends TileController<EmptyTileSlot>
{
    @FXML
    private StackPane root;

    @FXML
    private StackPane slotForLetterTile;

    public void bind(EmptyTileSlot modelToUse)
    {
        this.model = modelToUse;
    }

    public LetterTileController letterTileController;

    public Node getRoot()
    {
        return root;
    }

    public void clearLetterTile()
    {
        if (this.model == null){
            throw new RuntimeException("model was null. call bind first.");
        }

        this.model.setTile(null);
        letterTileController = null;
        updateView();
    }

    public void setLetter(LetterTileController letterController)
    {
        // update the model
        this.model.setTile(letterController.getModel());
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
