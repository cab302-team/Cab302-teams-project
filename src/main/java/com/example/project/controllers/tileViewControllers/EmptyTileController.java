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

    private EmptyTileSlot emptyTileModel;

    public void bind(EmptyTileSlot modelToUse)
    {
        this.emptyTileModel = modelToUse;
    }

    public LetterTileViewController letterTileController;

    public Node getRoot()
    {
        return root;
    }

    public void clearLetterTile()
    {
        if (emptyTileModel == null){
            throw new RuntimeException("model was null. call bind first.");
        }

        emptyTileModel.setTile(null);
        letterTileController = null;
        updateView();
    }

    public void setLetter(LetterTileViewController letterController)
    {
        // update the model
        emptyTileModel.setTile(letterController.getModel());
        letterTileController = letterController;
        updateView();
    }

    public LetterTileViewController getLetterTilesController()
    {
        return this.letterTileController;
    }

    private void updateView()
    {
        slotForLetterTile.getChildren().clear();

        if (this.letterTileController != null){
            slotForLetterTile.getChildren().add(this.letterTileController.getRoot());
        }
    }
}
