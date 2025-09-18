package com.example.project.controllers.gameScreens;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.services.SceneManager;
import com.example.project.services.GameScenes;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class LevelControllerTest
{
//    @InjectMocks
//    private LevelController levelController;
//
//    @Mock
//    private LevelModel levelModelMock;
//
//    @Mock
//    private SceneManager sceneManagerMock;
//
//    @Mock
//    private HBox tileRackContainer, wordViewHBox, upgradeTilesContainer;
//
//    @Mock
//    private VBox redrawContainer;
//
//    @Mock
//    private Button playButton, redrawButton, confirmRedrawButton;
//
//    @Mock
//    private Label comboCountLabel, comboMultiplierLabel, currentScoreLabel, levelWonLostText, scoreToBeatLabel, playsLeftLabel, redrawsLeftLabel;
//
//    @BeforeEach
//    void setUp()
//    {
//        MockitoAnnotations.openMocks(this);
//
//        // Manually inject FXML fields (since no real FXMLLoader is used)
//        levelController.tileRackContainer = tileRackContainer;
//        levelController.wordViewHBox = wordViewHBox;
//        levelController.upgradeTilesContainer = upgradeTilesContainer;
//        levelController.redrawContainer = redrawContainer;
//
//        levelController.playButton = playButton;
//        levelController.redrawButton = redrawButton;
//        levelController.confirmRedrawButton = confirmRedrawButton;
//
//        levelController.comboCountLabel = comboCountLabel;
//        levelController.comboMultiplierLabel = comboMultiplierLabel;
//        levelController.currentScoreLabel = currentScoreLabel;
//        levelController.levelWonLostText = levelWonLostText;
//        levelController.scoreToBeatLabel = scoreToBeatLabel;
////        levelController.playsLeftLabel = playsLeftLabel;
////        levelController.redrawsLeftLabel = redrawsLeftLabel;
//
//        // Replace SceneManager singleton with mock
//        var sceneManagerInstance = new SceneManager(sceneManagerMock);
    ////        SceneManager.setInstance(sceneManager);
//    }

    @Test
    void testOnSkipButton_switchesToShop()
    {
        var sceneManagerMock = mock(SceneManager.class);
        var sceneManager = new SceneManager(sceneManagerMock);

        var modelMock = mock(LevelModel.class);
        var levelController = new LevelController(modelMock);

        levelController.onSkipButton();

        verify(sceneManagerMock).switchScene(GameScenes.SHOP);
    }

    @Test
    void testOnRedrawButton_togglesRedrawActive()
    {

        var sceneManagerMock = mock(SceneManager.class);
        var sceneManager = new SceneManager(sceneManagerMock);

        var modelMock = mock(LevelModel.class);
        var levelController = new LevelController(modelMock);

        when(modelMock.getIsRedrawActive()).thenReturn(new javafx.beans.property.SimpleBooleanProperty(false));

        levelController.onRedrawButton();

        verify(modelMock).setIsRedrawActive(true);
        verify(modelMock).returnRedrawTilesToTheRack();
    }

    @Test
    void testOnConfirmRedrawButton_redrawsTiles()
    {
        var sceneManagerMock = mock(SceneManager.class);
        var sceneManager = new SceneManager(sceneManagerMock);

        var modelMock = mock(LevelModel.class);
        var levelController = new LevelController(modelMock);

        when(modelMock.getIsRedrawActive()).thenReturn(new javafx.beans.property.SimpleBooleanProperty(false));

        levelController.onConfirmRedrawButton();

        verify(modelMock).setIsRedrawActive(true);
        verify(modelMock).redrawTiles();
    }

}