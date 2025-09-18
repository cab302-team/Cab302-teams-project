package com.example.project.models.popups;
import com.example.project.services.sqlite.dAOs.DictionaryDAO;
import javafx.beans.property.*;

/**
 * Represents Definition window called when a word is played
 */
public class DefinitionPopup extends Popup{

    private final ReadOnlyStringWrapper definition = new ReadOnlyStringWrapper("lorem ipsum");

    private final ReadOnlyStringWrapper word = new ReadOnlyStringWrapper("lorem");

    private final ReadOnlyBooleanWrapper isDefinitionActive = new ReadOnlyBooleanWrapper(false);

    /**
     * @return returns property of definition
     */
    public ReadOnlyStringProperty definitionProperty(){
        return definition.getReadOnlyProperty();
    }

    /**
     * @return returns property of word
     */
    public ReadOnlyStringProperty wordProperty(){
        return word.getReadOnlyProperty();
    }

    /**
     * @return returns boolean
     */
    public ReadOnlyBooleanProperty getIsDefinitionActive()
    {
        return isDefinitionActive.getReadOnlyProperty();
    }

    /**
     * toggle isDefinitionActive
     * @param newState current state of definition window
     */
    public void setIsDefinitionActive(boolean newState)
    {
        this.isDefinitionActive.set(newState);
    }

    /**
     * @param currentWord Word, when OnPlayButton is passed
     */
    public DefinitionPopup(String currentWord){
        word.set(currentWord);
        DictionaryDAO dictionary = new DictionaryDAO();
        definition.set(dictionary.getWordDefinition(currentWord));
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/letterTileView.fxml";
    }
}
