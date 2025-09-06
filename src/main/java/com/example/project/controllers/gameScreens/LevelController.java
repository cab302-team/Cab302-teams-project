package com.example.project.controllers.gameScreens;

import com.example.project.services.Logger;
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
     * no arg contstructor.
     */
    public LevelController()
    {
        levelModel = new LevelModel(9, 9);

        // for testing
        var up = new UpgradeTile("calm", "description", "/com/example/project/upgradeTileImages/Monk_29.png");
        levelModel.addUpgrade(up);
    }

    @Override
    public GameScreenModel getModel() {
        return levelModel;
    }

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
    private final List<EmptyTileController> wordWindowTileSlots = new ArrayList<>();

    /**
     * Empty tile slots in the tile rack row.
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
            var emptyTileController = loadEmptySlotIntoLevelUI(wordViewHBox);
            wordWindowTileSlots.add(emptyTileController);
        }
    }

    private void loadLettersInTileRackSlots()
    {
        for (LetterTile lt : levelModel.getLetterTiles())
        {
            var emptyTileController = loadEmptySlotIntoLevelUI(tileRackContainer);
            tileRackTileSlots.add(emptyTileController);

            var letterController = loadNewLetterIntoLevel(lt);
            emptyTileController.setLetter(letterController);
        }
    }

    private void loadUpgradeTiles()
    {
        for (UpgradeTile upgradeTile: levelModel.getUpgrades())
        {
            var newUpgrade = loadUpgradeTileIntoLevelUI(upgradeTile);
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

    private UpgradeTileViewController loadUpgradeTileIntoLevelUI(UpgradeTile upgradeTile)
    {
        var fmxl = "/com/example/project/SingleTiles/upgradeTileView.fxml";
        return loadTile(fmxl, upgradeTileRackAtTop, upgradeTile);
    }

    private LetterTileViewController loadNewLetterIntoLevel(LetterTile lt)
    {
        var fmxl = "/com/example/project/SingleTiles/letterTileView.fxml";
        LetterTileViewController controller = loadTile(fmxl, tileRackContainer, lt);
        controller.getRoot().setOnMouseClicked(e -> {
            onLetterTileClicked(controller);
        });
        return controller;
    }

    private EmptyTileController loadEmptySlotIntoLevelUI(HBox container)
    {
        var fmxl = "/com/example/project/SingleTiles/emptyTileSlot.fxml";
        var emptyTile = new EmptyTileSlot();
        return loadTile(fmxl, container, emptyTile);
    }

    private void onLetterTileClicked(LetterTileViewController tile)
    {
        // find empty slot that contains letter tile
        EmptyTileController slotTileWasIn = tryGetSlotFromWordWindow(tile);
        boolean tileClickedWasInWordWindow = true;

        if (slotTileWasIn == null){
            slotTileWasIn = tryGetSlotFromTileRack(tile);
            tileClickedWasInWordWindow = false; // its in tile rack
        }

        EmptyTileController newSlot = getEmptyTileController(slotTileWasIn, tileClickedWasInWordWindow);

        if (newSlot == null)
        {
            this.logger.logMessage("no empty tile slot to use.");
            return;
        }

        newSlot.setLetter(tile);
        slotTileWasIn.clearLetterTile();
    }

    private EmptyTileController getEmptyTileController(EmptyTileController slotTileWasIn, boolean tileClickedIsInWordWindow)
    {
        if (slotTileWasIn == null){
            throw new RuntimeException("tile slot not found for tile clicked");
        }

        var rowToGoTo = tileClickedIsInWordWindow ? wordWindowTileSlots : tileRackTileSlots;

        // find leftmost empty slot in new row
        EmptyTileController newSlot = null;
        for (EmptyTileController potentialSlot : rowToGoTo)
        {
            if (potentialSlot.getLetterTilesController() == null){
                newSlot = potentialSlot;
            }
        }
        return newSlot;
    }

    private EmptyTileController tryGetSlotFromTileRack(LetterTileViewController tile)
    {
        for (EmptyTileController wordSlot : wordWindowTileSlots)
        {
            if (wordSlot.getLetterTilesController() == tile)
            {
                return wordSlot;
            }
        }

        return null;
    }

    private EmptyTileController tryGetSlotFromWordWindow(LetterTileViewController tile)
    {
        for (EmptyTileController slot : tileRackTileSlots)
        {
            if (slot.getLetterTilesController() == tile)
            {
                return slot;
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
