package com.example.project.models.tiles;


/**
 * Represents the letter tiles the player plays holds, redraws.
 */
public class LetterTileModel extends TileModel
{
    private final Character letter;

    private final Integer value;

    private final ScrabbleTileProvider provider = new ScrabbleTileProvider();

    /**
     * @param newLetter letter
     */
    public LetterTileModel(Character newLetter)
    {
        this.letter = Character.toUpperCase(newLetter);
        this.value = provider.getValue(letter);
    }

    /**
     * @return returns the tiles character.
     */
    public Character getLetter() {
        return letter;
    }

    /**
     * @return base value of scrabble letter score.
     */
    public Integer getValue(){
        return this.value;
    }

    @Override
    public String getFXMLPath() {
        return "/com/example/project/SingleTiles/letterTileView.fxml";
    }
}
