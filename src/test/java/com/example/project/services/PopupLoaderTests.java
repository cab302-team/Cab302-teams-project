package com.example.project.services;

import com.example.project.controllers.popupControllers.DefinitionController;
import com.example.project.models.popups.DefinitionPopup;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class.
 */
public class PopupLoaderTests
{
    @Test
    void test_createPopupController_success()
    {
        var mockPageLoader = mock(FXMLPageLoader.class);
        var loader = new PopupLoader(mockPageLoader);
        var mockParent = mock(Pane.class);
        var mockController = mock(DefinitionController.class);

        when(mockPageLoader.getController()).thenReturn(mockController);

        try{
            when(mockPageLoader.load(anyString())).thenReturn(mockParent);
        }
        catch (IOException e){
            fail("didn't expect this exception in the unit test.");
        }

        var popupObject = mock(DefinitionPopup.class);
        when(popupObject.getFXMLPath()).thenReturn("path");
        var result = loader.createPopupController(popupObject);

        // Assert
        verify(mockController, times(1)).initialize(popupObject);
        assertEquals(mockController, result);
    }

    @Test
    void test_createPopupController_throwsRuntimeException()
    {
        var mockPageLoader = mock(FXMLPageLoader.class);
        var popupLoader = new PopupLoader(mockPageLoader);
        var mockParent = mock(Pane.class);
        var mockController = mock(DefinitionController.class);

        when(mockPageLoader.getController()).thenReturn(mockController);

        try{
            when(mockPageLoader.load(anyString())).thenThrow(new IOException("exception"));
        }
        catch (IOException e){
            fail("didn't expect this exception in the unit test.");
        }

        var popupObject = mock(DefinitionPopup.class);
        when(popupObject.getFXMLPath()).thenReturn("path");

        var expected = "asdf";
        // Assert
        assertThrows(RuntimeException.class, () -> popupLoader.createPopupController(popupObject), expected);
    }
}
