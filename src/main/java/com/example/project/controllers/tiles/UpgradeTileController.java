package com.example.project.controllers.tiles;

import com.example.project.models.tiles.TileModel;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.controllers.gameScreens.TooltipSetup;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javafx.scene.shape.Rectangle;

/**
 * Controls the upgrade tile view fxml file.
 */
public class UpgradeTileController extends TileController<UpgradeTileModel>
{
    @FXML
    protected StackPane root;

    @FXML
    protected ImageView imageView;

    @FXML
    protected Label countText;

    public Node getRoot()
    {
        return root;
    }

    public void bind(UpgradeTileModel tileModel)
    {
        var path = getClass().getResource(tileModel.getAbilityImagePath());
        if (path == null) { throw new RuntimeException("path null"); }
        var url = path.toExternalForm();
        var image = new Image(url);
        this.imageView.setImage(image);

        Rectangle clip = new Rectangle();
        // set corner radii
        clip.setArcWidth(TileModel.CORNER_RADIUS);
        clip.setArcHeight(TileModel.CORNER_RADIUS);

        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());

        imageView.setClip(clip);

        TooltipSetup tooltip = new TooltipSetup();
        tooltip.setupTooltip(this.root, TooltipSetup.Element.UPGRADE, model);

        this.countText.setText("");
        this.countText.setVisible(false);
    }

    /**
     * updates upgrade # subscript
     * @param count the number of duplicate upgrades
     */
    public void updateCount(int count)
    {
        if (count > 1)
        {
            this.countText.setText(String.valueOf(count));
            this.countText.setVisible(true);
        }
        else
        {
            this.countText.setText("");
            this.countText.setVisible(false);
        }
    }
}
