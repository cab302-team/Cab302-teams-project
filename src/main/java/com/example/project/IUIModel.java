package com.example.project;


import javafx.scene.Node;

/**
 * Interface for all classes that have a corresponding UI.
 */
public interface IUIModel
{
    /**
     * @return UI Model node.
     */
    public Node getUIElement();
}
