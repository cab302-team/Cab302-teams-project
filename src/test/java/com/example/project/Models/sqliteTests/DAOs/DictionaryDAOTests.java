package com.example.project.Models.sqliteTests.DAOs;

import com.example.project.Logger;
import com.example.project.Models.sqlite.DAOs.DictionaryDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DictionaryDAO}.
 */
public class DictionaryDAOTests
{
    private final Logger testLogger = new Logger();
    private static final Logger dictionaryLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
    private static final String productionDB = "databases/English-Dictionary-Open-Source-main/sqlite3/dictionary.db";
    private static final String tableName = "entries";

    private Connection getConnectionToMockProductionDB(){
        return SQLiteDBCreator.getConnectionToMockProductionDB(productionDB, tableName);
    }

    @Test
    void isWordInDictionary_True()
    {
        String sql = "INSERT INTO entries (word, definition) VALUES (?, ?)";
        var connection = getConnectionToMockProductionDB();
        var word = "valid";
        var definition = "valid for test.";
        this.addWordAndDefinition(word, definition, connection);
        DictionaryDAO sqlDictionary = new DictionaryDAO(connection, dictionaryLogger);
        var exists = sqlDictionary.isWordInDictionary(word);
        assertTrue(exists);
    }

    @Test
    void isWordInDictionary_False()
    {
        DictionaryDAO sqlDictionary = new DictionaryDAO(getConnectionToMockProductionDB(), dictionaryLogger);
        var exists = sqlDictionary.isWordInDictionary("z");
        assertFalse(exists);
    }

    /**
     * Test method helper
     */
    private void addWordAndDefinition(String word, String def, Connection connectionToDB)
    {
        String sql = "INSERT INTO entries (word, definition) VALUES (?, ?)";

        try (PreparedStatement statement = connectionToDB.prepareStatement(sql)) {
            statement.setString(1, word);
            statement.setString(2, def);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            Assertions.fail(String.format("isWordInDictionary_True failed. SQL Exception: %s", e.getMessage()));
        }
    }

    @Test
    void getWordDefinition_Success()
    {
        var connection = getConnectionToMockProductionDB();
        var word = "test";
        var expectedDefinition = "definition of the word test";
        this.addWordAndDefinition(word, expectedDefinition, connection);
        var sqlDictionary = new DictionaryDAO(connection, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));
        var foundDefinition = sqlDictionary.getWordDefinition(word);
        assertEquals(expectedDefinition, foundDefinition);
    }

    @Test
    void isWordInDictionary_ThrowsSQLException()
    {
        DictionaryDAO emptyDictionary = new DictionaryDAO(SQLiteDBCreator.getConnectionToEmptyDB(), dictionaryLogger);
        var result = emptyDictionary.isWordInDictionary("apple");
        assertFalse(result);
        var logs = dictionaryLogger.getErrorLogs();
        assertTrue(logs.contains("word not found due to exception"), String.format("Instead found: %s", String.format(logs)));
        assertTrue(logs.contains("Error message: [SQLITE_ERROR] SQL error or missing database (no such table: entries)"), String.format("Instead found: %s", String.format(logs)));
    }

    @Test
    void getWordDefinition_NotInDictionary()
    {
        var newLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
        DictionaryDAO dictionary =
                new DictionaryDAO(getConnectionToMockProductionDB(), newLogger);

        var wordToFind = "z";
        dictionary.getWordDefinition(wordToFind);

        var logs = newLogger.getErrorLogs();
        var logsContainExpectedError = logs.contains(String.format("No rows in database for word: %s", wordToFind));
        assertTrue(logsContainExpectedError, String.format("test getWordDefinition_NotInDictionary failed actual log was: %s", logs));
    }

    @Test
    void getWordDefinition_ThrowsSQLException()
    {
        DictionaryDAO emptyDictionary = new DictionaryDAO(SQLiteDBCreator.getConnectionToEmptyDB(), dictionaryLogger);
        String result = emptyDictionary.getWordDefinition("apple");
        assertEquals("No Definition", result);
        var logs = dictionaryLogger.getErrorLogs();
        assertTrue(logs.contains("Definition not found for word: apple"), String.format("Instead found: %s", String.format(logs)));
        assertTrue(logs.contains("Error message: [SQLITE_ERROR] SQL error or missing database (no such table: entries)"), String.format("Instead found: %s", String.format(logs)));
    }
}
