package com.example.project.models.tiles;

import com.example.project.models.gameScreens.LevelModel;
import com.example.project.testHelpers.MockAudioSystemExtension;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;


/**
 * Test class for {@link UpgradeEffects}
 */
@ExtendWith(MockAudioSystemExtension.class)
class UpgradeEffectsTest
{
    @Test
    void glassesEffect()
    {
        var model = mock(LevelModel.class);
        var wordMulti = 1;
        var mockWordMulti = mock(ReadOnlyIntegerProperty.class);
        when(mockWordMulti.get()).thenReturn(wordMulti);
        when(model.getWordMultiProperty()).thenReturn(mockWordMulti);

        var wordWindowTiles = mock(ReadOnlyListProperty.class);
        ObservableList<LetterTileModel> list = FXCollections.observableArrayList(
                new LetterTileModel('A'), new LetterTileModel('A')
        );

        when(wordWindowTiles.get()).thenReturn(list);
        when(wordWindowTiles.size()).thenReturn(2);
        when(wordWindowTiles.get(0)).thenReturn(list.get(0));
        when(wordWindowTiles.get(1)).thenReturn(list.get(1));

        //noinspection unchecked
        when(model.getWordWindowTilesProperty()).thenReturn(wordWindowTiles);
        UpgradeEffects.glassesEffect(model);

        verify(model).setWordMulti(wordMulti + 2);
    }
}