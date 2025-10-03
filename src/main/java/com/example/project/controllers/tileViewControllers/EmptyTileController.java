package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Empty Tile controller for EmptyTile view.
 */
public class EmptyTileController extends TileController<EmptyTileSlot>
{
    @FXML
    protected StackPane root;

    @FXML
    protected StackPane slotForLetterTile;


    public void bind(EmptyTileSlot modelToUse)
    {
        this.model = modelToUse;
    }

    private LetterTileController letterTileController;

    public Node getRoot()
    {
        return root;
    }

    /**
     * Unit Test for EmptyTileControllerTests
     * @param root injecting Stackpane manually
     */
    protected void setRoot(StackPane root) { this.root = root; }

    protected void setSlotForLetterTile(StackPane slot) { this.slotForLetterTile = slot; }


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
