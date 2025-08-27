import com.example.project.Logger;
import com.example.project.SQLiteDictionary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SQLiteDictionary}. Not a mockup. This is testing the actual database.
 */
public class SQLiteDictionaryTest
{
    private final Logger testLogger = new Logger(true);
    final SQLiteDictionary sqlDictionary = new SQLiteDictionary(testLogger);
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
    void getWordDefinitionNotInDictionary()
    {
        var result = sqlDictionary.getWordDefinition(stringNotInDictionary);
        assertEquals("No Definition", result);
        String expected = String.format("No rows in database for word: %s%n", stringNotInDictionary);

        var errorContentString = this.testLogger.getLogs();
        assertEquals(expected, errorContentString, String.format("Error not as expected. was: %s", errorContentString));
    }
}
