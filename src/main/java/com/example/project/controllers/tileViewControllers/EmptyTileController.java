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
    public EmptyTileController() {super();}

    public EmptyTileController(EmptyTileSlot tileObject)
    {
        super(tileObject);
    }

    @Override
    public void setRoot(StackPane root){
        this.root = root;
    }

    @FXML
    private StackPane root;

    @FXML
    private StackPane slotForLetterTile;

    public void bind()
    {
    }

    private LetterTileController letterTileController;

    public Node getRoot()
    {
        return root;
    }

    /**
     * clears the letter tile on this empty slot.
     */
    public void clearLetterTile()
    {
        if (this.model == null){
            throw new RuntimeException("model was null. call bind first.");
        }

        this.model.setTile(null);
        letterTileController = null;
        updateView();
    }

    /**
     * updates empty slot to have a letter tile in it.
     * @param letterController the letter tiles controller.
     */
    public void setLetter(LetterTileController letterController)
    {
        // update the model
        this.model.setTile(letterController.getModel());
        letterTileController = letterController;
        updateView();
    }

    private void updateView()
    {
        if (slotForLetterTile == null) { return; }
        slotForLetterTile.getChildren().clear();

        if (this.letterTileController != null){
            slotForLetterTile.getChildren().add(this.letterTileController.getRoot());
        }
    }
}
