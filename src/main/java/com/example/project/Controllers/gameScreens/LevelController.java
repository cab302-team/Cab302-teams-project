package com.example.project.Controllers.gameScreens;

import com.example.project.Models.Tiles.LetterTile;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.Random;

/**
 * Controller for the level view screen.
 */
public class LevelController extends gameScreenController
{
    @FXML
    HBox tileRackContainer;

    /**
     * The hand size to start with at the start of the round in the player's tile rack.
     */
    private static Integer handSize = 9;

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");

        for (var i = 0; i < handSize; i++)
        {
            var newTile = new LetterTile(getRandomLetter());
            this.addTileToUILayout(newTile);
        }
    }

    private static final Random random = new Random();

    private Character getRandomLetter() {
        return (char) ('A' + random.nextInt(26));
    }

    private void addTileToUILayout(LetterTile newTile)
    {
        var tileNode = newTile.getUIElement();
        tileRackContainer.getChildren().add(tileNode);
    }
}
