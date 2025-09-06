package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.EmptyTileController;
import com.example.project.controllers.tileViewControllers.LetterTileViewController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.controllers.tileViewControllers.UpgradeTileViewController;
import com.example.project.models.gameScreens.GameScreenModel;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.tiles.EmptyTileSlot;
import com.example.project.models.tiles.LetterTile;
import com.example.project.models.tiles.Tile;
import com.example.project.models.tiles.UpgradeTile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

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

    @FXML
    Button playButton;

    @FXML
    Button redrawButton;

    private static LevelModel levelModel;

    /**
     * no arg constructor.
     */
    public LevelController()
    {
        levelModel = new LevelModel(9, 9);

        // TODO actual implementation of upgrade tiles then remove.
        var tileExample = new UpgradeTile("calm", "description", "/com/example/project/upgradeTileImages/Monk_29.png");
        levelModel.addUpgrade(tileExample);
    }

    @Override
    public GameScreenModel getModel() {
        return levelModel;
    }

    /**
     * tile slots in the word window.
     */
    private final List<EmptyTileController> wordWindowTileSlots = new ArrayList<>();

    /**
     * tile slots in the tile rack row.
     */
    private final List<EmptyTileController> tileRackTileSlots = new ArrayList<>();

    private final List<UpgradeTileViewController> upgradeTiles = new ArrayList<>();

    @Override
    public void onSceneChangedToThis()
    {
        this.logger.logMessage("level page loaded.");
        loadWordViewEmptySlots();
        loadLettersInTileRackSlots();
        loadUpgradeTiles();
    }

    private void loadWordViewEmptySlots()
    {
        for (var i = 0; i < levelModel.getMaxWordSize(); i++)
        {
            var emptyTileController = loadEmptySlotIntoRow(wordViewHBox);
            wordWindowTileSlots.add(emptyTileController);
        }
    }

    private void loadLettersInTileRackSlots()
    {
        for (LetterTile lt : levelModel.getLetterTiles())
        {
            var emptyTileController = loadEmptySlotIntoRow(tileRackContainer);
            tileRackTileSlots.add(emptyTileController);

            var fmxl = "/com/example/project/SingleTiles/letterTileView.fxml";
            LetterTileViewController letterController = loadTile(fmxl, tileRackContainer, lt);
            letterController.getRoot().setOnMouseClicked(e -> {
                onLetterTileClicked(letterController);
            });
            emptyTileController.setLetter(letterController);
        }
    }

    private void loadUpgradeTiles()
    {
        for (UpgradeTile upgradeTile: levelModel.getUpgrades())
        {
            var fmxl = "/com/example/project/SingleTiles/upgradeTileView.fxml";
            UpgradeTileViewController newUpgrade = loadTile(fmxl, upgradeTileRackAtTop, upgradeTile);
            upgradeTiles.add(newUpgrade);
        }
    }

    private <C extends TileController<T>, T extends Tile> C loadTile(String fxmlPath, HBox container, T tileObject) {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();
            C controller = loader.getController(); // This returns the controller, not the tile
            controller.bind(tileObject);
            container.getChildren().addFirst(controller.getRoot());
            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load tile: " + fxmlPath, e);
        }
    }

    private EmptyTileController loadEmptySlotIntoRow(HBox container)
    {
        var fmxl = "/com/example/project/SingleTiles/emptyTileSlot.fxml";
        var emptyTile = new EmptyTileSlot();
        return loadTile(fmxl, container, emptyTile);
    }

    private void onLetterTileClicked(LetterTileViewController tile)
    {
        EmptyTileController slotTileWasIn = tryGetSlotFromRow(tile, wordWindowTileSlots);
        var rowTileWillGoTo = tileRackTileSlots; // tile will go to the other row currently in.

        if (slotTileWasIn == null){ // tile was in tile rack. so it goes to word window.
            slotTileWasIn = tryGetSlotFromRow(tile, tileRackTileSlots);
            rowTileWillGoTo = wordWindowTileSlots;
        }

        if (slotTileWasIn == null) { throw new RuntimeException("tile not found in either row."); }

        EmptyTileController slotToMoveTileTo = getLeftMostEmptyTileInRow(slotTileWasIn, rowTileWillGoTo);

        if (slotToMoveTileTo == null) { // can't move, no empty tile to move to.
            this.logger.logMessage("no empty tile slot to use.");
            return;
        }

        slotToMoveTileTo.setLetter(tile);
        slotTileWasIn.clearLetterTile();
    }

    private EmptyTileController getLeftMostEmptyTileInRow(EmptyTileController slotTileWasIn, List<EmptyTileController> rowToGoTo)
    {
        if (slotTileWasIn == null){
            throw new RuntimeException("tile slot not found for tile clicked");
        }

        EmptyTileController newSlot = null;
        for (EmptyTileController potentialSlot : rowToGoTo)
        {
            if (potentialSlot.getLetterTilesController() == null){
                newSlot = potentialSlot;
            }
        }
        return newSlot;
    }

    private EmptyTileController tryGetSlotFromRow(LetterTileViewController tile, List<EmptyTileController> rowOfEmptySlotsToCheck)
    {
        for (EmptyTileController wordSlot : rowOfEmptySlotsToCheck)
        {
            if (wordSlot.getLetterTilesController() == tile)
            {
                return wordSlot;
            }
        }

        return null;
    }

    @FXML
    private void onPlayButton(){

    }

    @FXML
    private void onRedrawButton(){

    }
}
