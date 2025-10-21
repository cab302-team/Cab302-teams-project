package com.example.project.services;

import com.example.project.controllers.popupControllers.PopupController;
import com.example.project.models.popups.PopupModel;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * class to load the popup and initialize it with a controller
 */
public class PopupLoader
{
    private final FXMLPageLoader loader;

    protected PopupLoader(FXMLPageLoader loader)
    {
        this.loader = loader;
    }

    /**
     * Constructor.
     */
    public PopupLoader()
    {
        this.loader = new FXMLPageLoader();
    }

    /**
     * create popup controller.
     * @param popupModel popup object.
     * @param <C> class of popup controller.
     * @param <P> class of popup.
     * @return returns controller of the tile.
     */
    public <C extends PopupController<P>, P extends PopupModel> C createPopupController(P popupModel)
    {
        try
        {
            // In this case not using javafx Popup because of its limitations.
            // Might refactor in case we use actual Popups.
            String fxmlPath = popupModel.getFXMLPath();
            Parent root = this.loader.load(fxmlPath);
            C controller = loader.getController();
            controller.initialize(popupModel);
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create popup controller: " + popupModel.getFXMLPath(), e);
        }
    }
}
