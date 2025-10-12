package com.example.project.models.tileGroups;


import com.example.project.controllers.tileViewControllers.EmptyTileSlotController;
import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.tiles.EmptyTileSlotModel;
import com.example.project.models.tiles.LetterTileModel;
import com.example.project.services.TileControllerFactory;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Letter Tile group tests.
 */
@ExtendWith(MockAudioSystemExtension.class)
public class LetterTileGroupTests
{
    @Test
    void TestLargestConstructor()
    {
        Pane container = new Pane();
        ReadOnlyListWrapper<LetterTileModel> observedList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

        Consumer<LetterTileController> onClickHandler = controller -> {
            // empty for test
        };

        // Create flags to check if Runnables were executed
        AtomicBoolean action1Executed = new AtomicBoolean(false);
        AtomicBoolean action2Executed = new AtomicBoolean(false);

        List<Runnable> afterSyncActions = List.of(
                () -> action1Executed.set(true),
                () -> action2Executed.set(true)
        );

        // Create the LetterTileGroup
        var group = new LetterTileGroup(3, container, observedList.getReadOnlyProperty(), onClickHandler, afterSyncActions);

        var mockLetterTileController = Mockito.mock(LetterTileController.class);

        Pane root = new Pane();
        Mockito.when(mockLetterTileController.getRoot()).thenReturn(root);

        var mockEmptyTileController = Mockito.mock(EmptyTileSlotController.class);

        var mockFactory = Mockito.mock(TileControllerFactory.class);

        Mockito.when(mockFactory.createLetterTileController(Mockito.any(LetterTileModel.class))).thenReturn(mockLetterTileController);
        Mockito.when(mockFactory.createEmptyTileController(Mockito.any(EmptyTileSlotModel.class))).thenReturn(mockEmptyTileController);
        Mockito.when(mockFactory.createTileController(Mockito.any(LetterTileModel.class), Mockito.any())).thenReturn(mockLetterTileController);

        group.tileControllerFactory = mockFactory;

        // Trigger the listener by changing the list
        observedList.add(new LetterTileModel('a'));

        // Assert that both Runnables were executed
        assertTrue(action1Executed.get(), "Action 1 should have run");
        assertTrue(action2Executed.get(), "Action 2 should have run");
    }

    @Test
    void syncTilesTest()
    {
        Pane container = Mockito.mock(Pane.class); // mock the Pane
        ObservableList<Node> mockList = mock(ObservableList.class); // mock the children list
        // make container.getChildren() return the mocked list
        Mockito.when(container.getChildren()).thenReturn(mockList);

        ReadOnlyListWrapper<LetterTileModel> observedList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

        Consumer<LetterTileController> onClickHandler = controller -> { };

        AtomicBoolean action1Executed = new AtomicBoolean(false);
        AtomicBoolean action2Executed = new AtomicBoolean(false);

        List<Runnable> afterSyncActions = List.of(
                () -> action1Executed.set(true),
                () -> action2Executed.set(true)
        );

        var mockLetterTileController = Mockito.mock(LetterTileController.class);

        Pane mockRoot = new Pane();
        when(mockLetterTileController.getRoot()).thenReturn(mockRoot);

        var mockEmptyTileController = mock(EmptyTileSlotController.class);
        when(mockLetterTileController.getRoot()).thenReturn(mockRoot);

        var mockFactory = mock(TileControllerFactory.class);

        when(mockFactory.createLetterTileController(Mockito.any(LetterTileModel.class))).thenReturn(mockLetterTileController);
        when(mockFactory.createEmptyTileController(Mockito.any(EmptyTileSlotModel.class))).thenReturn(mockEmptyTileController);
        when(mockFactory.createTileController(Mockito.any(LetterTileModel.class), Mockito.any())).thenReturn(mockLetterTileController);

        // Create the LetterTileGroup
        var group = new LetterTileGroup(3, container, observedList.getReadOnlyProperty(), onClickHandler, afterSyncActions, mockFactory);

        // Add a model to trigger controller creation
        LetterTileModel model = new LetterTileModel('a');
        observedList.add(model);

        // test method
        group.syncTiles();

        // Assert recreateControllers and updateVisuals happened.
        // Verify that tileControllers were populated
        assertEquals(1, group.getControllers().size(), "TileControllers should have one item after sync");

        // Verify that LetterTileGroups implementation of update visuals worked.
        // once for null and once for the setting
        verify(mockEmptyTileController, times(6)).setLetter(null);
        verify(mockEmptyTileController, times(2)).setLetter(mockLetterTileController);

        // verify factory methods called
        verify(mockFactory, times(2)).createTileController(any(LetterTileModel.class), any());
        verify(mockFactory, times(3)).createEmptyTileController(any());
    }

    @Test
    void getControllersTest(){
        Pane container = Mockito.mock(Pane.class); // mock the Pane
        ObservableList<Node> mockList = mock(ObservableList.class); // mock the children list
        // make container.getChildren() return the mocked list
        Mockito.when(container.getChildren()).thenReturn(mockList);

        ReadOnlyListWrapper<LetterTileModel> observedList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

        Consumer<LetterTileController> onClickHandler = controller -> { };

        AtomicBoolean action1Executed = new AtomicBoolean(false);
        AtomicBoolean action2Executed = new AtomicBoolean(false);

        List<Runnable> afterSyncActions = List.of(
                () -> action1Executed.set(true),
                () -> action2Executed.set(true)
        );

        var mockLetterTileController = Mockito.mock(LetterTileController.class);

        Pane mockRoot = new Pane();
        when(mockLetterTileController.getRoot()).thenReturn(mockRoot);

        var mockEmptyTileController = mock(EmptyTileSlotController.class);
        when(mockLetterTileController.getRoot()).thenReturn(mockRoot);

        var mockFactory = mock(TileControllerFactory.class);

        when(mockFactory.createLetterTileController(Mockito.any(LetterTileModel.class))).thenReturn(mockLetterTileController);
        when(mockFactory.createEmptyTileController(Mockito.any(EmptyTileSlotModel.class))).thenReturn(mockEmptyTileController);
        when(mockFactory.createTileController(Mockito.any(LetterTileModel.class), Mockito.any())).thenReturn(mockLetterTileController);

        // Create the LetterTileGroup
        var group = new LetterTileGroup(3, container, observedList.getReadOnlyProperty(), onClickHandler, afterSyncActions, mockFactory);

        // Add a model to trigger controller creation
        LetterTileModel model = new LetterTileModel('a');
        observedList.add(model);

        // test method
        var expected = List.of(mockLetterTileController);
        var controllers = group.getControllers();

        assertEquals(expected.size(), controllers.size());
        assertEquals(expected, controllers);
    }
}
