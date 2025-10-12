package com.example.project.controllers.tileViewControllers;

import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Controls the upgrade tile view fxml file.
 */
public class UpgradeTileController extends TileController<UpgradeTile>
{
    @FXML
    protected StackPane root;

    @FXML
    protected ImageView imageView;

    public Node getRoot()
    {
        return root;
    }

    public void bind(UpgradeTile tileModel)
    {
        var path = getClass().getResource(tileModel.getAbilityImagePath());
        if (path == null) { throw new RuntimeException("path null"); }
        var url = path.toExternalForm();
        var image = new Image(url);
        this.imageView.setImage(image);

        Rectangle clip = new Rectangle();
        // set corner radii
        clip.setArcWidth(Tile.CORNER_RADIUS);
        clip.setArcHeight(Tile.CORNER_RADIUS);

        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());

        imageView.setClip(clip);

        Tooltip tooltip = new Tooltip(formatUpgradeTooltipText());
        tooltip.setStyle("-fx-font-size: 16px; -fx-font-family: Arial;"); // TODO: to go in upgrade-tile-styles.css
        tooltip.setShowDelay(Duration.seconds(0));
        Tooltip.install(this.root, tooltip);
    }

    private String formatUpgradeTooltipText()
    {
        return String.format("%s: %s %n $%.2f", model.getName(), model.getDescription(),
                model.getCost());
    }
}
