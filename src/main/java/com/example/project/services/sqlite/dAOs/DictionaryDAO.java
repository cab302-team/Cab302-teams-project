package com.example.project.services.sqlite.dAOs;

import com.example.project.services.Logger;
import com.example.project.services.sqlite.SQLiteDictionaryConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The SQLite Dictionary. The connection returned from SQLiteDictionaryConnection().getInstance() is always the same.
 */
public class DictionaryDAO
{
    private final Connection connection;
    private final Logger logger;

    /**
     * Constructor for this class SQLLiteDictionary.
     */
    public DictionaryDAO()
    {
        this.connection = new SQLiteDictionaryConnection().getInstance();
        this.logger = new Logger();
    }

    /**
     * constructor with injection for tests.
     * @param connection mock connection.
     * @param logger a mock logger.
     */
    public DictionaryDAO(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    /**
     * gets the words definition.
     * @param wordToFind Word to get definition of.
     * @return Returns the definition.
     */
    public String getWordDefinition(String wordToFind)
    {
        List<String> definitions = new ArrayList<>();
        try
        {
            wordToFind = wordToFind.trim();
            PreparedStatement query = connection.prepareStatement("SELECT wordtype, definition FROM entries WHERE word = ? COLLATE NOCASE");
            query.setString(1, wordToFind);
            ResultSet result = query.executeQuery();

            while (result.next())
            {
                definitions.add("Wordtype: " +
                        result.getString("wordtype") +
                        System.lineSeparator() +
                        "Definition: " +
                        result.getString("definition"));
            }
            if (definitions.isEmpty())
            {
                this.logger.logError(String.format("No rows in database for word: %s", wordToFind));
                definitions.add("No Definition");
            }
        }
        catch (SQLException e)
        {
            this.logger.logError(String.format("Definition not found for word: %s", wordToFind));
            this.logger.logError("Error message: " + e.getMessage());
        }
        String doubleLineSeparator = System.lineSeparator() + System.lineSeparator();
        return String.join(doubleLineSeparator, definitions);
    }

    /**
     * gets if word is in dictionary.
     * @param wordToCheck Word to check.
     * @return returns the boolean value indicating whether the word exists in our database.
     */
    public boolean isWordInDictionary(String wordToCheck)
    {
        wordToCheck = wordToCheck.toLowerCase();

        try
        {
            PreparedStatement doesWordExistQuery = connection.prepareStatement("SELECT 1 FROM entries WHERE word = ? LIMIT 1");
            doesWordExistQuery.setString(1, wordToCheck);
            ResultSet doesWordExistResult = doesWordExistQuery.executeQuery();
            return doesWordExistResult.next();
        }
        catch (SQLException e)
        {
            this.logger.logError("word not found due to exception");
            this.logger.logError("Error message:" + e.getMessage());
        }

        return false;
    }
}
