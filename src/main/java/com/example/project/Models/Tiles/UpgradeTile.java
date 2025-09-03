package com.example.project.Models.Tiles;

import javafx.scene.Node;

/**
 * Represents the upgrade tiles sold at the shop.
 */
public class UpgradeTile extends Tile
{
    // TODO: the upgrade tile stuff like description its FXML (see Letter Tile).

    @Override
    public String getFXMLFile()
    {
        throw new RuntimeException("not implemented an fxml tile layout for upgrade Tile.");
    }

    @Override
    public Node getUIElement() {
        return null;
    }
}
