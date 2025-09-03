package com.example.project.Controllers;

import com.example.project.Logger;
import com.example.project.Tiles.LetterTile;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.Random;

public class LevelController extends gameScreenController
{
    private final Logger logger = new Logger();

    @FXML
    HBox container;

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
        container.getChildren().add(tileNode);
    }
}
