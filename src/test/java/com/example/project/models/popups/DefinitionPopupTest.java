package com.example.project.models.popups;

import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test for Definition Popup class
 */
@ExtendWith(MockAudioSystemExtension.class)
class DefinitionPopupTest {

    @Test
    void testInitialisation() {
        DefinitionPopup pup = new DefinitionPopup();
        assertEquals("lorem ipsum", pup.definitionProperty().get(), "Initial definition should be 'lorem ipsum'");
        assertEquals("lorem", pup.wordProperty().get(), "Initial word should be 'lorem'");
        assertFalse(pup.getIsDefinitionActive().get(), "State should be inactive (false)");
        assertEquals("/com/example/project/PopUps/DefinitionView.fxml", pup.getFXMLPath(), "The FXML path doesn't match expected path.");

    }

    @Test
    void setIsDefinitionActive_toTrue() {
        DefinitionPopup pup = new DefinitionPopup();
        pup.setIsDefinitionActive(true);
        assertTrue(pup.getIsDefinitionActive().get(), "State should be inactive (true)");

    }

    @Test
    void setIsDefinitionActive_toFalse() {
        DefinitionPopup pup = new DefinitionPopup();
        pup.setIsDefinitionActive(false);
        assertFalse(pup.getIsDefinitionActive().get(), "State should be inactive (false)");
    }

    @Test
    void testSetPopup() {
        DefinitionPopup pup = new DefinitionPopup();
        final String inputWord = "Test";
        pup.setPopup(inputWord);
        assertEquals(inputWord, pup.wordProperty().get(), "Word property was not set correctly");
    }
}