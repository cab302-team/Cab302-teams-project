package com.example.project.controllers.gameScreens;

import com.example.project.controllers.tileViewControllers.LetterTileController;
import com.example.project.controllers.tileViewControllers.TileController;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.tiles.LetterTile;
import com.sun.glass.ui.Window;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelControllerTest
{
    @Test
    void initializeTest(){

    }

    @Test
    void onSceneChangedToThisTest(){

    }

    @Mock private Button playButton;
    @Mock private LevelModel levelModel;
    @Mock private LetterTileGroup wordRow;
    @Mock private Label comboCountLabel;
    @Mock private Label comboMultiplierLabel;
    @Mock private Label currentScoreLabel;
    @Mock private javafx.scene.control.Label playerTotalPointsLabel;

    @InjectMocks
    private LevelController controller;

    @BeforeEach
    void setUp() {
//        setField(controller, "playButton", playButton);
//        setField(controller, "levelModel", levelModel);
//        setField(controller, "wordRow", wordRow);
//        setField(controller, "comboCountLabel", comboCountLabel);
//        setField(controller, "comboMultiplierLabel", comboMultiplierLabel);
//        setField(controller, "currentScoreLabel", currentScoreLabel);
    }

    @Test
    void testOnPlayButton_DisablesButtonAndGetsInitialScore()
    {
        controller = new LevelController();

        var wordRowControllers = Arrays.asList(
                new LetterTileController(),
                new LetterTileController()
        );

        // Arrange
        int startScore = 1;
        IntegerProperty playersTotalPoints = new SimpleIntegerProperty(3);

        when(levelModel.getPlayersTotalPoints()).thenReturn(playersTotalPoints);
        when(wordRow.getControllers()).thenReturn(wordRowControllers);

        // Act
        controller.onPlayButton();

        // Assert
        verify(playButton).setDisable(true);
        verify(levelModel).getPlayersTotalPoints();
//        verify(playersTotalPoints).get();
        verify(wordRow).getControllers();
    }

}