package sqliteTests;

import com.example.project.Logger;
import com.example.project.sqlite.SQLiteDictionary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SQLiteDictionary}.
 */
public class SQLiteDictionaryTest
{
    private static final ByteArrayOutputStream mockByteArray = new ByteArrayOutputStream();
    private static final Logger dictionaryLogger = new Logger(mockByteArray, new ByteArrayOutputStream());
    final SQLiteDictionary sqlDictionary = new SQLiteDictionary(dictionaryLogger);
    private final String stringNotInDictionary = "z";

    /**
     * In memory database connection that's empty to throw Exceptions for testing.
     */
    private static Connection inMemoryDBConnectionEmpty;

    /**
     * In memory dictionary that's empty to throw Exceptions for testing.
     */
    private static SQLiteDictionary emptyDictionary;

    @BeforeAll
    static void beforeAll()
    {
        try
        {
            inMemoryDBConnectionEmpty = DriverManager.getConnection("jdbc:sqlite::memory:");
        }
        catch (Exception e)
        {
            Assertions.fail(e.getMessage());
        }

        emptyDictionary = new SQLiteDictionary(inMemoryDBConnectionEmpty, dictionaryLogger);
    }

    @AfterAll
    static void afterAll(){
        try
        {
            inMemoryDBConnectionEmpty.close();
        }
        catch (Exception e){
            Assertions.fail(e.getMessage());
        }
    }

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
    void isWordInDictionaryThrowsSQLException()
    {
        // This line throws sql exception
        var result = emptyDictionary.isWordInDictionary("apple");
        // Assert sql exception was thrown.
        assertFalse(result);
        var logs = dictionaryLogger.getErrorLogs();
        assertTrue(logs.contains("word not found due to exception"), String.format("Instead found: %s", String.format(logs)));
        assertTrue(logs.contains("Error message: [SQLITE_ERROR] SQL error or missing database (no such table: entries)"), String.format("Instead found: %s", String.format(logs)));
    }

    @Test
    void getWordDefinitionWhenItsNotInDictionary()
    {
        var result = sqlDictionary.getWordDefinition(stringNotInDictionary);
        assertEquals("No Definition", result);
        String expected = String.format("No rows in database for word: %s%n", stringNotInDictionary);
        var errorContentString = dictionaryLogger.getErrorLogs();
        assertEquals(expected, errorContentString, String.format("Error not as expected. was: %s", errorContentString));
    }

    @Test
    void getWordDefinitionThrowsSQLException()
    {
        // This line throws sql exception
        String result = emptyDictionary.getWordDefinition("apple");
        // Assert sql exception was thrown.
        assertEquals("No Definition", result);
        var logs = dictionaryLogger.getErrorLogs();
        assertTrue(logs.contains("Definition not found for word: apple"), String.format("Instead found: %s", String.format(logs)));
        assertTrue(logs.contains("Error message: [SQLITE_ERROR] SQL error or missing database (no such table: entries)"), String.format("Instead found: %s", String.format(logs)));
    }
}
