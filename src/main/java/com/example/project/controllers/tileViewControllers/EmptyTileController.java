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
        modelToUse.getLetterTileSlotProperty().addListener((obs, oldVal, newVal) -> syncLetter(newVal));
    }

    /**
     * updates empty slot to have a letter tile in it.
     * @param letterController the letter tiles controller.
     */
    private void syncLetter(LetterTile letterController)
    {
        if (this.model == null)
        {
            throw new RuntimeException("model was null. call bind first.");
        }

//        this.model.setTile(letterController.getModel());
//
//        slotForLetterTile.getChildren().clear();
//        if (letterController != null){
//            slotForLetterTile.getChildren().add(letterController.getRoot());
//        }
    }

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
}
