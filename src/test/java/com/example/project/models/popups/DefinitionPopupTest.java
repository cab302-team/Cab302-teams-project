package com.example.project.models.popups;

import com.example.project.models.tiles.LetterTileModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefinitionPopupTest {
    @Test
    void getFXMLPathTest() {
        DefinitionPopup pup = new DefinitionPopup();
        assertEquals("/com/example/project/PopUps/DefinitionView.fxml", pup.getFXMLPath(), "The FXML path doesn't match expected path.");
    }
}