package com.example.project.models.tiles;


/**
 * Represents the letter tiles the player plays holds, redraws.
 */
public class LetterTile extends Tile
{
    private final Character letter;

    private final Integer value;

    /**
     * @param newLetter letter
     */
    public LetterTile(Character newLetter)
    {
        this.letter = Character.toUpperCase(newLetter);
        this.value = ScrabbleLettersValues.getValue(letter);
    }

    public String getFXMLFile(){
        return "/com/example/project/SingleTiles/letterTileView.fxml";
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

}
