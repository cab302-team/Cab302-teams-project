package com.example.project.Tiles;

import javafx.scene.Node;
import jdk.jshell.spi.ExecutionControl;

import javax.print.attribute.standard.PrinterMoreInfoManufacturer;

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
