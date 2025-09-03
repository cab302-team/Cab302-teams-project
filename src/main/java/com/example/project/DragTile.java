package com.example.project;

import javafx.scene.Node;

/**
 * A helper class to add dragging functionality to any Tile.
 */

public class DragTile {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggable(Node node) {
        // Handle for mouse pressed event
        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getSceneX();
            mouseAnchorY = mouseEvent.getSceneY();

        });
        // Handle for mouse dragged event
        node.setOnMouseDragged(mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);

        });
    }
}
