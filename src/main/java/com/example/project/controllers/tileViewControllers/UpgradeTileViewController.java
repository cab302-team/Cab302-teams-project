package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javafx.scene.shape.Rectangle;

/**
 * Controlls the upgrade tile view fxml file.
 */
public class UpgradeTileViewController extends TileController<UpgradeTile>
{
    @FXML
    private StackPane root;

    @FXML
    private ImageView imageView;

    private UpgradeTile model;

    public Node getRoot()
    {
        return root;
    }

    public void bind(UpgradeTile tileModel)
    {
        var image = new Image(getClass().getResource(tileModel.getAbilityImagePath()).toExternalForm());
        this.imageView.setImage(image);

        Rectangle clip = new Rectangle();
        // set corner radii
        clip.setArcWidth(Tile.CORNER_RADIUS);
        clip.setArcHeight(Tile.CORNER_RADIUS);

        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());

        imageView.setClip(clip);

        model = tileModel;
    }
}
