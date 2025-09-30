package com.example.project.services;

import com.example.project.controllers.popupControllers.DefinitionController;
import com.example.project.controllers.popupControllers.PopupController;
import com.example.project.models.popups.DefinitionPopup;
import com.example.project.models.popups.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * class to load the popup and initialize it with a controller
 */
public class PopupLoader
{

    /**
     * create popup controller.
     * @param popupObject popup object.
     * @param <C> class of popup controller.
     * @param <P> class of popup.
     * @return returns controller of the tile.
     */
    private static <C extends PopupController<P>, P extends Popup> C createPopupController(P popupObject)
    {
        try
        {
            String fxmlPath = popupObject.getFXMLPath();
            FXMLLoader loader = new FXMLLoader(PopupLoader.class.getResource(fxmlPath));
            Parent root = loader.load();
            C controller = loader.getController();
            controller.initialize(popupObject);
            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create popup controller: " + popupObject.getFXMLPath(), e);
        }
    }

    /**
     * @param pup popup model
     * @return returns definition popup controller
     */
    public static DefinitionController createDefinitionPopup(DefinitionPopup pup)
    {
        return createPopupController(pup);
    }

}
