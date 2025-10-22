package com.example.project.models.popups;

import com.example.project.services.sound.GameSoundPlayer;
import com.example.project.testHelpers.MockAudioSystemExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PopupModel}.
 */
@ExtendWith(MockAudioSystemExtension.class)
class PopupModelTest {

    /**
     * here is a simple concrete subclass for testing abstract Popup.
     */
    static class TestPopup extends PopupModel {
        @Override
        public String getFXMLPath() {
            return "/test/path.fxml";
        }
    }

    @Test
    void getHoverSoundPlayer_ShouldReturnNonNullPlayer() {
        PopupModel pup = new TestPopup();

        GameSoundPlayer player = pup.getPaperSoundPlayer();

        assertNotNull(player, "getPaperSoundPlayer() should return a non-null GameSoundPlayer.");
    }

    @Test
    void getClackSoundPlayer_ShouldReturnNonNullPlayer() {
        PopupModel pup = new TestPopup();

        GameSoundPlayer player = pup.getReversePaperSoundPlayer();

        assertNotNull(player, "getReversePaperSoundPlayer() should return a non-null GameSoundPlayer.");
    }

    @Test
    void getFXMLPath_ShouldReturnOverriddenValue() {
        PopupModel pup = new TestPopup();

        assertEquals("/test/path.fxml", pup.getFXMLPath(),
                "getFXMLPath() should return the overridden value.");
    }
}