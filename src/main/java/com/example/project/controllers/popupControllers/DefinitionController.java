package com.example.project.controllers.popupControllers;

import com.example.project.controllers.gameScreens.animations.InfiniteFloatingAnimation;
import com.example.project.controllers.tiles.TileControllerFactory;
import com.example.project.models.tiles.LetterTileModel;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import com.example.project.models.popups.DefinitionPopup;
import java.util.ArrayList;
import java.util.List;


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
    public Node getStack() { return defStack; }

    public DefinitionPopup getModel(){
        return model;
    }

    private final TileControllerFactory tileControllerFactory = new TileControllerFactory();

    @Override
    public void initialize(DefinitionPopup model)
    {
        this.model = model;
        wordRow.setMouseTransparent(true);
        model.definitionProperty().addListener((obs, oldStr, newStr) -> syncdefinitionProperty(newStr));
        model.wordProperty().addListener((obs, oldStr, newStr) -> syncwordProperty(newStr));
    }

    private void syncdefinitionProperty(String newStr){
        this.definitionLabel.setText(String.format("%s", newStr));
    }

    private void syncwordProperty(String newStr){
        // TODO: turn this into a reusable external function (same used in LoginController)
        wordRow.getChildren().clear();
        List<LetterTileModel> lettersInWordPlayed = new ArrayList<>();
        char[] charArray = newStr.toCharArray();

        for (char letter : charArray){
            lettersInWordPlayed.add(new LetterTileModel(letter));
        }

        for (var t : lettersInWordPlayed){
            var ltController = tileControllerFactory.createLetterTileController(t);
            ltController.getRoot().setScaleX(-(wordRow.getMaxWidth())/1.5);
            ltController.getRoot().setScaleY(-(wordRow.getMaxWidth())/1.5);
            wordRow.getChildren().add(ltController.getRoot());
            InfiniteFloatingAnimation fa = new InfiniteFloatingAnimation();
            fa.apply(ltController.getRoot(), 4);
        }
    }

}
