package com.example.project.controllers.popupControllers;

import com.example.project.models.popups.Popup;
import javafx.fxml.FXML;
import javafx.scene.Node;

/**
 * Parent class for PopupControllers
 * @param <P> Popup model type
 */
public abstract class PopupController<P extends Popup>
{
    /**
     * root ui node.
     * @return root ui Node
     */
    public abstract Node getRoot();

    /**
     * sets elements of the ui
     * @param popup popup to initialize
     */
    @FXML
    public abstract void initialize(P popup);

    protected P model;

    /**
     * gets the popup model.
     * @return model.
     */
    public P getModel()
    {
        return this.model;
    }
}
