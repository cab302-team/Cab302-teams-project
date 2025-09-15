package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.LetterTile;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import javafx.animation.ScaleTransition;
import javafx.util.Duration;
/**
 * Controls the layout of the letterUIModel. Which is a letter tile.
 */
public class LetterTileController extends TileController<LetterTile>
{
    @FXML
    private StackPane root;

    @FXML
    private Label letterLabel;

    @FXML
    private Label valueLabel;

    public Node getRoot() {
        return root;
    }

    public LetterTile getModel() {
        return model;
    }

    @Override
    public void bind(LetterTile model) {
        this.model = model;
        letterLabel.setText(String.valueOf(model.getLetter()));
        valueLabel.setText(String.valueOf(model.getValue()));

        // Enable caching for performance
        letterLabel.setCache(true);
        letterLabel.setCacheHint(CacheHint.QUALITY);

        valueLabel.setCache(true);
        valueLabel.setCacheHint(CacheHint.QUALITY);
    }

    /** Instantly set tile scale */
    public void setScale(double scale) {
        root.setScaleX(scale);
        root.setScaleY(scale);
    }

    /** Animate scaling smoothly */
    public void animateScale(double targetScale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(250), root);
        st.setToX(targetScale);
        st.setToY(targetScale);
        st.play();
    }
}