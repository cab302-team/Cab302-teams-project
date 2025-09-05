package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.EmptyTileSlot;
import javafx.fxml.FXML;
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

    public void setLetter(LetterTileViewController letterController)
    {
        // update the model
        emptyTileModel.setTile(letterController.getModel());
        updateView(letterController);
    }

    private void updateView(LetterTileViewController letterController)
    {
        slotForLetterTile.getChildren().clear();
        slotForLetterTile.getChildren().add(letterController.getroot()); //  but models dont know about fxml?
    }
}
