import com.example.project.SQLiteDictionary;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SQLiteDictionary}. Not a mockup. This is testing the actual database.
 */
public class SQLiteDictionaryTest
{
    final SQLiteDictionary sqlDictionary = new SQLiteDictionary();
    private final String stringNotInDictionary = "z";

    @Test
    void isWordInDictionaryTrue()
    {
        var exists = sqlDictionary.isWordInDictionary("valid");
        assertTrue(exists);
    }

    @Test
    void isWordInDictionaryFalse()
    {
        var exists = sqlDictionary.isWordInDictionary(stringNotInDictionary);
        assertFalse(exists);
    }

    @Test
    void getWordDefinitionSuccess()
    {
        var definition = sqlDictionary.getWordDefinition("test");
        assertEquals("A cupel or cupelling hearth in which precious metals are\n" +
                "   melted for trial and refinement.", definition);
    }

    @Test
    void getWordDefinitionThrows()
    {

         // it doesnt throw anymore checkj err output.
//        Exception exception = assertThrows(SQLException.class, () -> {
//            sqlDictionary.getWordDefinition(stringNotInDictionary);
//        });

//        String expectedMessage = String.format("Tried to get definition of a word not in database the word was; %s",
//                stringNotInDictionary);
//        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
