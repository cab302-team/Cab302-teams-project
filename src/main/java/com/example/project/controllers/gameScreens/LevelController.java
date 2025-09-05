package com.example.project.controllers.gameScreens;

import com.example.project.Logger;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.Random;

/**
 * Controller for the level view screen.
 */
public class LevelController extends GameScreenController
{
    @FXML
    HBox tileRackContainer;

    @FXML
    HBox upgradeTileRackAtTop;

    /**
     * The hand size to start with at the start of the round in the player's tile rack.
     */
    private static Integer handSize = 9;

    /**
     * no arg contstructor.
     */
    public LevelController() {}

    /**
     * Constructor with injection for tests.
     * @param logger logger to use.
     */
    public LevelController(Logger logger)
    {
        super(logger);
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");

        for (var i = 0; i < handSize; i++)
        {
            var newTile = new LetterTile(getRandomLetter());
            this.addTileToUILayout(newTile);
        }

        // adds a single tile to the top rack for now.
        AddUpgradeTilesToUI();
    }

    private void AddUpgradeTilesToUI()
    {
        var newUpgradeTileTest = new UpgradeTile();
        var upgradeTileNode = newUpgradeTileTest.getUIElement();
        upgradeTileRackAtTop.getChildren().add(upgradeTileNode);
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
