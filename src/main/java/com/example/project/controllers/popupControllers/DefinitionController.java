package com.example.project.controllers.popupControllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import com.example.project.models.popups.DefinitionPopup;


/**
 * Controller for definition popup
 */
public class DefinitionController extends PopupController<DefinitionPopup>
{
    @FXML
    private StackPane defStack;

    @FXML
    Pane wordRow;

    @FXML
    Label definitionLabel;

    @FXML
    ImageView definitionImage;

    /**
     * @return returns Node
     */
    public Node getRoot() { return defStack; }

    public DefinitionPopup getModel(){
        return model;
    }

    @Override
    public void initialize(DefinitionPopup model)
    {
        this.model = model;
        model.getdefinition().addListener((obs, oldVal, newVal) -> syncdefinitionProperty(newVal));
        model.getword().addListener((obs, oldVal, newVal) -> syncwordProperty(newVal));
    }

}
