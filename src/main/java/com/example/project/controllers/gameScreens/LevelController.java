package com.example.project.controllers.gameScreens;

import com.example.project.Logger;
import com.example.project.controllers.tileViewControllers.EmptyTileController;
import com.example.project.controllers.tileViewControllers.LetterTileViewController;
import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller for the level view screen.
 */
public class LevelController extends GameScreenController
{
    @FXML
    HBox tileRackContainer;

    @FXML
    HBox wordViewHBox;

    @FXML
    HBox upgradeTileRackAtTop;

    /**
     * The hand size to start with at the start of the round in the player's tile rack.
     */
    private static Integer handSize = 9;

    private static Integer maxWordSize = 10;

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

    /**
     * Empty tile slots in the word window.
     */
    private List<EmptyTileController> wordWindowTileSlots = new ArrayList<>();

    /**
     * Empty tile slots in the tile rack row.
     */
    private List<EmptyTileController> tileRackTileSlots = new ArrayList<>();

    private List<UpgradeTileViewController> upgradeTiles = new ArrayList<>();

    /**
     * all letter tiles in the level scene.
     */
    private List<LetterTile> letterTiles = new ArrayList<>();

    private static final Random random = new Random();

    private Character getRandomLetter() {
        return (char) ('A' + random.nextInt(26));
    }

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");

        for (var i = 0; i < maxWordSize; i++)
        {
            var emptyTileController = addEmptySlot(wordViewHBox);
            wordWindowTileSlots.add(emptyTileController);
        }

        for (var i = 0; i < handSize; i++)
        {
            // add empty slot
            var emptyTileController = addEmptySlot(tileRackContainer);
            tileRackTileSlots.add(emptyTileController);

            // Load letter node
            var letterController = loadNewLetterIntoLevel();

            // put letter node into empty tile slot.
            emptyTileController.setLetter(letterController);
        }

        // adds a single tile to the top rack for now.
        addUpgradeTilesToUI();
    }

    private UpgradeTileViewController addUpgradeTilesToUI()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/SingleTiles" +
                    "/upgradeTileView.fxml"));

            Node upgradeTileNode = loader.load();
            UpgradeTileViewController controller = loader.getController();

            var upgrade = new UpgradeTile();

            controller.bind(upgrade);
            upgradeTileRackAtTop.getChildren().add(upgradeTileNode);
            return controller;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private LetterTileViewController loadNewLetterIntoLevel()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/SingleTiles" +
                    "/letterTileView.fxml"));
            Node letterTileNode = loader.load();
            var newLetter = new LetterTile(getRandomLetter());
            LetterTileViewController controller = loader.getController();
            controller.bind(newLetter);

            letterTileNode.setOnMouseClicked(e -> {
                onLetterTileClicked(controller);
            });

            return controller;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private EmptyTileController addEmptySlot(HBox container)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/SingleTiles/emptyTileSlot.fxml"));
            Node node = loader.load();
            container.getChildren().add(node);
            EmptyTileController controller = loader.getController();
            controller.bind(new EmptyTileSlot());
            return controller;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void onLetterTileClicked(LetterTileViewController tile)
    {
        // remove from tile slot its in and put in left most empty slot in word rack or tile view. = the other than
        // what its in right now.
    }
}
