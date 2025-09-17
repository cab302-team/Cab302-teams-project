package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.LetterTile;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Controls the layout of the letterUIModel. Which is a letter tile.
 */
public class LetterTileController extends TileController<LetterTile>
{
    public LetterTileController() { super(); }

    public LetterTileController(LetterTile tile)
    {
        super(tile);

        var pane = this.getRoot();

        pane.setOnMouseEntered(e -> {
            pane.setScaleX(1.1);
            pane.setScaleY(1.1);
            this.getModel().getHoverSoundPlayer().play();
        });

        pane.setOnMouseExited(e -> {
            pane.setScaleX(1.0);
            pane.setScaleY(1.0);
        });
    }

    @FXML
    private StackPane root;

    @FXML
    ImageView tileImage;

    @FXML
    Label letterLabel;

    @FXML
    Label valueLabel;

    public Node getRoot()
    {
        return root;
    }

    public LetterTile getModel(){
        return model;
    }

    @Override
    public void bind(LetterTile model)
    {
        this.model = model;
        letterLabel.setText(String.valueOf(model.getLetter()));
        valueLabel.setText(String.valueOf(model.getValue()));

        letterLabel.setCache(true);
        letterLabel.setCacheHint(CacheHint.QUALITY);

        valueLabel.setCache(true);
        valueLabel.setCacheHint(CacheHint.QUALITY);

        tileImage.setCache(true);
        tileImage.setCacheHint(CacheHint.QUALITY);
    }
}
