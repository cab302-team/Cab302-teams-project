package com.example.project.models.popups;

/**
 * Represents Definition window called when a word is played
 */
public class DefinitionPopup extends Popup{
    private final String word;

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/letterTileView.fxml";
    }
}
