package com.example.project.Tiles;

import com.example.project.Controllers.LetterTileViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;


/**
 * Represents the letter tiles the player plays holds, redraws.
 */
public class LetterTile extends Tile
{
    private final Character letter;

    private final Integer value;

    /**
     * @param newLetter letter
     */
    public LetterTile(Character newLetter)
    {
        this.letter = Character.toUpperCase(newLetter);
        this.value = ScrabbleLettersValues.GetValue(letter);
    }

    @Override
    public Node getUIElement()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFXMLFile()));

        StackPane tilePane = null;
        try
        {
            tilePane = fxmlLoader.load();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        tilePane.setPrefSize(50, 50);
        LetterTileViewController controller = fxmlLoader.getController();
        controller.setUIValues(this);
        return tilePane;
    }

    public String getFXMLFile(){
        return "/com/example/project/letterTileView.fxml";
    }

    public Character getLetter() {
        return letter;
    }

    /**
     * @return base value of scrabble letter score.
     */
    public Integer getValue(){
        return this.value;
    }

}
