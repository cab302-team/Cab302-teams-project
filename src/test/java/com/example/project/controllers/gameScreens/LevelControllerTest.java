package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.tiles.LetterTile;
import com.example.project.services.GameScenes;
import com.example.project.services.SceneManager;
import com.example.project.services.Session;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnit.;
import org.testfx.framework.junit5.ApplicationExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class LevelControllerTest {

    @Mock
    private LevelModel mockLevelModel;

    @Mock
    private Session mockSession;

    @Mock
    private SceneManager mockSceneManager;

    @Mock
    private LetterTileController mockTileController;

    @Mock
    private LetterTile mockTileModel;

    // UI Components
    @Mock
    private Label levelWonLostText;
    @Mock
    private HBox tileRackContainer;
    @Mock
    private HBox wordViewHBox;
    @Mock
    private HBox upgradeTilesContainer;
    @Mock
    private Button playButton;
    @Mock
    private Button redrawButton;
    @Mock
    private Label scoreToBeatLabel;
    @Mock
    private Label playerTotalPointsLabel;
    @Mock
    private Label comboCountLabel;
    @Mock
    private Label comboMultiplierLabel;
    @Mock
    private VBox redrawContainer;
    @Mock
    private Button confirmRedrawButton;
    @Mock
    private StackPane gameStack;
    @Mock
    private ImageView backgroundImage;
    @Mock
    private ImageView tileRackImage;
    @Mock
    private Label playsLeftLabel;
    @Mock
    private Label redrawsLeftLabel;

    // Observable Properties
    private IntegerProperty playersTotalPoints;
    private IntegerProperty wordPoints;
    private IntegerProperty wordMulti;
    private IntegerProperty currentRedraws;
    private IntegerProperty currentPlays;
    private BooleanProperty isRedrawActive;
    private ListProperty<LetterTileModel> tileRackRowTiles;
    private ListProperty<LetterTileModel> wordRowTiles;
    private ListProperty<LetterTileModel> redrawRowTiles;
    private ListProperty<Object> upgradeTiles;

    private LevelController levelController;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize observable properties
        playersTotalPoints = new SimpleIntegerProperty(100);
        wordPoints = new SimpleIntegerProperty(10);
        wordMulti = new SimpleIntegerProperty(2);
        currentRedraws = new SimpleIntegerProperty(3);
        currentPlays = new SimpleIntegerProperty(5);
        isRedrawActive = new SimpleBooleanProperty(false);
        tileRackRowTiles = new SimpleListProperty<>(FXCollections.observableArrayList());
        wordRowTiles = new SimpleListProperty<>(FXCollections.observableArrayList());
        redrawRowTiles = new SimpleListProperty<>(FXCollections.observableArrayList());
        upgradeTiles = new SimpleListProperty<>(FXCollections.observableArrayList());

        // Mock LevelModel behavior
        when(mockLevelModel.getPlayersTotalPoints()).thenReturn(playersTotalPoints);
        when(mockLevelModel.wordPointsProperty()).thenReturn(wordPoints);
        when(mockLevelModel.wordMultiProperty()).thenReturn(wordMulti);
        when(mockLevelModel.getCurrentRedraws()).thenReturn(currentRedraws);
        when(mockLevelModel.getCurrentPlays()).thenReturn(currentPlays);
        when(mockLevelModel.getIsRedrawActive()).thenReturn(isRedrawActive);
        when(mockLevelModel.getTileRackRowTilesProperty()).thenReturn(tileRackRowTiles);
        when(mockLevelModel.getWordRowTilesProperty()).thenReturn(wordRowTiles);
        when(mockLevelModel.getRedrawRowTilesProperty()).thenReturn(redrawRowTiles);
        when(mockLevelModel.getUpgradeTilesProprety()).thenReturn(upgradeTiles);
        when(mockLevelModel.getHandSize()).thenReturn(7);
        when(mockLevelModel.getMaxWordSize()).thenReturn(10);
        when(mockLevelModel.getRedrawWindowSize()).thenReturn(5);
        when(mockLevelModel.getLevelRequirement()).thenReturn(500);
        when(mockLevelModel.isWordValid()).thenReturn(true);
        when(mockLevelModel.calcTotalScore()).thenReturn(50);

        // Mock UI component properties
        ReadOnlyDoubleProperty widthProperty = mock(ReadOnlyDoubleProperty.class);
        ReadOnlyDoubleProperty heightProperty = mock(ReadOnlyDoubleProperty.class);
        when(gameStack.widthProperty()).thenReturn(widthProperty);
        when(gameStack.heightProperty()).thenReturn(heightProperty);

        DoubleProperty fitWidthProperty = mock(DoubleProperty.class);
        DoubleProperty fitHeightProperty = mock(DoubleProperty.class);
        when(backgroundImage.fitWidthProperty()).thenReturn(fitWidthProperty);
        when(backgroundImage.fitHeightProperty()).thenReturn(fitHeightProperty);

        // Create controller and inject mocked fields
        levelController = new LevelController();
        injectMockedFields();
    }

    private void injectMockedFields() throws Exception {
        // Inject all the @FXML fields using reflection
        setField("levelWonLostText", levelWonLostText);
        setField("tileRackContainer", tileRackContainer);
        setField("wordViewHBox", wordViewHBox);
        setField("upgradeTilesContainer", upgradeTilesContainer);
        setField("playButton", playButton);
        setField("redrawButton", redrawButton);
        setField("scoreToBeatLabel", scoreToBeatLabel);
        setField("playerTotalPointsLabel", playerTotalPointsLabel);
        setField("comboCountLabel", comboCountLabel);
        setField("comboMultiplierLabel", comboMultiplierLabel);
        setField("redrawContainer", redrawContainer);
        setField("confirmRedrawButton", confirmRedrawButton);
        setField("gameStack", gameStack);
        setField("backgroundImage", backgroundImage);
        setField("tileRackImage", tileRackImage);
        setField("playsLeftLabel", playsLeftLabel);
        setField("redrawsLeftLabel", redrawsLeftLabel);

        // Inject the static levelModel
        Field levelModelField = LevelController.class.getDeclaredField("levelModel");
        levelModelField.setAccessible(true);
        levelModelField.set(null, mockLevelModel);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = LevelController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(levelController, value);
    }

    @Test
    void testOnSceneChangedToThis() {
        // Act
        levelController.onSceneChangedToThis();

        // Assert
        verify(scoreToBeatLabel).setText("required: 500");
        verify(mockLevelModel).setupNewLevel();
        verify(levelWonLostText).setText("");
        verify(comboCountLabel).setText("10");
        verify(comboMultiplierLabel).setText("2");
        verify(playerTotalPointsLabel).setText("100");
    }

    @Test
    void testSyncTotalScoreProperty() {
        // Act
        playersTotalPoints.set(250);

        // Assert
        verify(playerTotalPointsLabel).setText("250");
    }

    @Test
    void testSyncWordPointsProperty() {
        // Act
        wordPoints.set(25);

        // Assert
        verify(comboCountLabel).setText("25");
    }

    @Test
    void testSyncWordMultiProperty() {
        // Act
        wordMulti.set(3);

        // Assert
        verify(comboMultiplierLabel).setText("3");
    }

    @Test
    void testSyncRedrawButton_WithRedrawsAvailable() {
        // Arrange
        currentRedraws.set(2);
        isRedrawActive.set(false);

        // Act - trigger the listener
        currentRedraws.set(1);

        // Assert
        verify(redrawButton).setDisable(false);
        verify(redrawsLeftLabel).setText("1");
        verify(redrawButton).setText("⟳");
    }

    @Test
    void testSyncRedrawButton_NoRedrawsLeft() {
        // Arrange
        currentRedraws.set(1);

        // Act
        currentRedraws.set(0);

        // Assert
        verify(redrawButton).setDisable(true);
        verify(redrawsLeftLabel).setText("0");
    }

    @Test
    void testSyncRedrawButton_RedrawActiveCancel() {
        // Arrange
        currentRedraws.set(2);
        isRedrawActive.set(true);

        // Act - trigger the listener
        currentRedraws.set(1);

        // Assert
        verify(redrawButton).setText("⟳ cancel");
    }

    @Test
    void testSyncPlayButton_ValidConditions() {
        // Arrange
        currentPlays.set(3);
        wordRowTiles.add(mock(LetterTileModel.class)); // Add a tile to make it non-empty
        when(mockLevelModel.isWordValid()).thenReturn(true);
        isRedrawActive.set(false);

        // Act - trigger the listener
        currentPlays.set(2);

        // Assert
        verify(playButton).setDisable(false);
        verify(playsLeftLabel).setText("2");
        verify(playButton).setText("▶");
    }

    @Test
    void testSyncPlayButton_NoPlaysLeft() {
        // Arrange
        currentPlays.set(1);

        // Act
        currentPlays.set(0);

        // Assert
        verify(playButton).setDisable(true);
        verify(playsLeftLabel).setText("0");
    }

    @Test
    void testSyncPlayButton_InvalidWord() {
        // Arrange
        currentPlays.set(3);
        wordRowTiles.add(mock(LetterTileModel.class));
        when(mockLevelModel.isWordValid()).thenReturn(false);
        isRedrawActive.set(false);

        // Act
        currentPlays.set(2);

        // Assert
        verify(playButton).setDisable(true);
    }

    @Test
    void testSyncPlayButton_EmptyWordRow() {
        // Arrange
        currentPlays.set(3);
        // wordRowTiles is empty by default
        when(mockLevelModel.isWordValid()).thenReturn(true);
        isRedrawActive.set(false);

        // Act
        currentPlays.set(2);

        // Assert
        verify(playButton).setDisable(true);
    }

    @Test
    void testSyncPlayButton_RedrawActive() {
        // Arrange
        currentPlays.set(3);
        wordRowTiles.add(mock(LetterTileModel.class));
        when(mockLevelModel.isWordValid()).thenReturn(true);
        isRedrawActive.set(true);

        // Act
        currentPlays.set(2);

        // Assert
        verify(playButton).setDisable(true);
    }

    @Test
    void testSyncConfirmRedrawButton_EmptyRedrawRow() {
        // Act - redrawRowTiles is empty by default
        redrawRowTiles.clear();

        // Assert
        verify(confirmRedrawButton).setDisable(true);
    }

    @Test
    void testSyncConfirmRedrawButton_HasTiles() {
        // Act
        redrawRowTiles.add(mock(LetterTileModel.class));

        // Assert
        verify(confirmRedrawButton).setDisable(false);
    }

    @Test
    void testOnLetterTileClicked_SuccessfulMove() {
        // Arrange
        when(mockTileController.getModel()).thenReturn(mockTileModel);
        when(mockLevelModel.tryMoveTile(mockTileModel)).thenReturn(true);

        // Act
        levelController.onLetterTileClicked(mockTileController);

        // Assert
        verify(mockLevelModel).tryMoveTile(mockTileModel);
    }

    @Test
    void testOnLetterTileClicked_FailedMove() {
        // Arrange
        when(mockTileController.getModel()).thenReturn(mockTileModel);
        when(mockLevelModel.tryMoveTile(mockTileModel)).thenReturn(false);

        // Act
        levelController.onLetterTileClicked(mockTileController);

        // Assert
        verify(mockLevelModel).tryMoveTile(mockTileModel);
        // Would need to verify logging, but logger is not easily testable without dependency injection
    }

    @Test
    void testOnPlayButton() {
        // Arrange
        playersTotalPoints.set(100);
        when(mockLevelModel.calcTotalScore()).thenReturn(50);
        when(mockLevelModel.hasWon()).thenReturn(false);
        when(mockLevelModel.hasLost()).thenReturn(false);

        // Act
        levelController.onPlayButton();

        // Assert
        verify(playButton).setDisable(true);
        // Animation testing would require more complex setup
    }

    @Test
    void testCheckLevelState_Won() throws Exception {
        // Arrange
        when(mockLevelModel.hasWon()).thenReturn(true);
        when(mockLevelModel.hasLost()).thenReturn(false);

        // Use reflection to access private method
        var method = LevelController.class.getDeclaredMethod("checkLevelState");
        method.setAccessible(true);

        // Act
        method.invoke(levelController);

        // Assert
        verify(levelWonLostText).setText("YOU WON!");
        // Animation completion would be tested separately
    }

    @Test
    void testCheckLevelState_Lost() throws Exception {
        // Arrange
        when(mockLevelModel.hasWon()).thenReturn(false);
        when(mockLevelModel.hasLost()).thenReturn(true);

        // Use reflection to access private method
        var method = LevelController.class.getDeclaredMethod("checkLevelState");
        method.setAccessible(true);

        // Act
        method.invoke(levelController);

        // Assert
        verify(mockLevelModel).onLostLevel();
    }

    @Test
    void testOnSkipButton() {
        // Arrange
        try (MockedStatic<SceneManager> mockedStatic = mockStatic(SceneManager.class)) {
            mockedStatic.when(SceneManager::getInstance).thenReturn(mockSceneManager);

            // Act
            levelController.onSkipButton();

            // Assert
            verify(mockSceneManager).switchScene(GameScenes.SHOP);
        }
    }

    @Test
    void testOnRedrawButton() {
        // Act
        levelController.onRedrawButton();

        // Assert
        verify(mockLevelModel).toggleRedrawState();
    }

    @Test
    void testOnConfirmRedrawButton() {
        // Act
        levelController.onConfirmRedrawButton();

        // Assert
        verify(mockLevelModel).toggleRedrawState();
        verify(mockLevelModel).redrawTiles();
    }

    @Test
    void testSyncRedrawWindow_ActivateRedraw() throws Exception {
        // Use reflection to access private method
        var method = LevelController.class.getDeclaredMethod("syncRedrawWindow", boolean.class);
        method.setAccessible(true);

        // Act
        method.invoke(levelController, true);

        // Assert - mainly testing that the method doesn't throw exceptions
        // Animation testing would require JavaFX Application Thread
        assertDoesNotThrow(() -> method.invoke(levelController, true));
    }

    @Test
    void testSyncRedrawWindow_DeactivateRedraw() throws Exception {
        // Use reflection to access private method
        var method = LevelController.class.getDeclaredMethod("syncRedrawWindow", boolean.class);
        method.setAccessible(true);

        // Act & Assert
        assertDoesNotThrow(() -> method.invoke(levelController, false));
    }

    @Test
    void testConstructor() {
        // Arrange & Act
        LevelController controller = new LevelController();

        // Assert - mainly testing that constructor doesn't throw
        assertNotNull(controller);
    }
}