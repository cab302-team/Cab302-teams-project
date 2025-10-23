package com.example.project.models.tileGroups;

import com.example.project.controllers.tiles.EmptyTileSlotController;
import com.example.project.controllers.tiles.UpgradeTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.controllers.tiles.TileControllerFactory;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the upgrade tile group class.
 */
public class UpgradeTileGroupTests
{
    @Test
    void TestLargestConstructor()
    {
        Pane container = Mockito.mock(Pane.class); // mock the Pane
        ObservableList<Node> mockList = mock(ObservableList.class);
        // make container.getChildren() return the mocked list
        Mockito.when(container.getChildren()).thenReturn(mockList);

        ReadOnlyListWrapper<UpgradeTileModel> observedList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

        AtomicBoolean action1Executed;
        action1Executed = new AtomicBoolean(false);
        AtomicBoolean action2Executed;
        action2Executed = new AtomicBoolean(false);

        Consumer<UpgradeTileController> onMouseCLickedActions = controller -> {
            action1Executed.set(true);
            action2Executed.set(true);
        };

        var tileController = Mockito.mock(UpgradeTileController.class);

        Pane root = mock(Pane.class);
        Mockito.when(tileController.getRoot()).thenReturn(root);

        var mockEmptyTileController = Mockito.mock(EmptyTileSlotController.class);

        var mockFactory = Mockito.mock(TileControllerFactory.class);

        Mockito.when(mockFactory.createTileController(Mockito.any(EmptyTileSlotModel.class), Mockito.any())).thenReturn(mockEmptyTileController);
        Mockito.when(mockFactory.createTileController(Mockito.any(UpgradeTileModel.class), Mockito.any())).thenReturn(tileController);

        // Create the LetterTileGroup
        var group = new UpgradeTileGroup(container, observedList.getReadOnlyProperty(), onMouseCLickedActions);
        group.tileControllerFactory = mockFactory;

        var addedTile = mock(UpgradeTileModel.class);
        observedList.add(addedTile);

        // Assert that on mouse clicked action was set.
//        verify(root, times(1)).setOnMouseClicked(e -> onMouseCLickedActions.accept(tileController));
        verify(root, times(1)).setOnMouseClicked(any());
    }

    @Test
    void syncTilesTest()
    {
        Pane container = Mockito.mock(Pane.class); // mock the Pane
        ObservableList<Node> mockList = mock(ObservableList.class); // mock the children list
        // make container.getChildren() return the mocked list
        Mockito.when(container.getChildren()).thenReturn(mockList);

        ReadOnlyListWrapper<UpgradeTileModel> observedList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

        AtomicBoolean action1Executed;
        action1Executed = new AtomicBoolean(false);
        AtomicBoolean action2Executed;
        action2Executed = new AtomicBoolean(false);

        Consumer<UpgradeTileController> afterSyncActions = controller -> {
            action1Executed.set(true);
            action2Executed.set(true);
        };

        var mockController = Mockito.mock(UpgradeTileController.class);

        Pane mockRoot = new Pane();
        when(mockController.getRoot()).thenReturn(mockRoot);

        var mockFactory = mock(TileControllerFactory.class);

        when(mockFactory.createTileController(Mockito.any(), Mockito.any())).thenReturn(mockController);

        // Create the LetterTileGroup
        var group = new UpgradeTileGroup(container, observedList.getReadOnlyProperty(), afterSyncActions);
        group.tileControllerFactory = mockFactory;

        // Add a model to trigger controller creation
        var addedTile = mock(UpgradeTileModel.class);
        observedList.add(addedTile);

        // test method
        group.syncTiles();

        // Assert recreateControllers and updateVisuals happened.
        // Verify that tileControllers were populated
        assertEquals(1, group.getControllers().size(), "TileControllers should have one item after sync");

        // Verify that LetterTileGroups implementation of update visuals worked.
        // once for null and once for the setting
        verify(container, times(4)).getChildren();

        verify(mockList, times(2)).clear();
        verify(mockList, times(2)).clear();

        // verify factory methods called
        verify(mockFactory, times(2)).createTileController(any(UpgradeTileModel.class), any());
    }

    @Test
    void getControllersTest(){
        Pane container = Mockito.mock(Pane.class); // mock the Pane
        ObservableList<Node> mockList = Mockito.mock(ObservableList.class); // mock the children list
        // make container.getChildren() return the mocked list
        Mockito.when(container.getChildren()).thenReturn(mockList);

        ReadOnlyListWrapper<UpgradeTileModel> observedList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        AtomicBoolean action1Executed;
        action1Executed = new AtomicBoolean(false);
        AtomicBoolean action2Executed;
        action2Executed = new AtomicBoolean(false);

        Consumer<UpgradeTileController> afterSyncActions = controller -> {
            action1Executed.set(true);
            action2Executed.set(true);
        };

        var mockController = Mockito.mock(UpgradeTileController.class);

        Pane mockRoot = new Pane();
        when(mockController.getRoot()).thenReturn(mockRoot);

        var mockFactory = mock(TileControllerFactory.class);

        when(mockFactory.createTileController(Mockito.any(UpgradeTileModel.class), Mockito.any())).thenReturn(mockController);

        // Create the LetterTileGroup
        var group = new UpgradeTileGroup(container, observedList.getReadOnlyProperty(), afterSyncActions);
        group.tileControllerFactory = mockFactory;

        // Add a model to trigger controller creation
        var addedTile = mock(UpgradeTileModel.class);
        observedList.add(addedTile);

        // test method
        var expected = List.of(mockController);
        var controllers = group.getControllers();

        assertEquals(expected.size(), controllers.size());
        assertEquals(expected, controllers);
    }
}
