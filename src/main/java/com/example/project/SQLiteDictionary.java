package com.example.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The SQLite Dictionary.
 */
public class SQLiteDictionary
{
    private final Connection connection;

    private final Logger logger;

    /**
     * Constructor for this class SQLLiteDictionary.
     */
    public SQLiteDictionary(Logger logger)
    {
        this.connection = SQLiteDictionaryConnection.getInstance();
        this.logger = logger;
    }

    /**
     * @param wordToFind Word to get definition of.
     * @return Returns the definition.
     */
    public String getWordDefinition(String wordToFind)
    {
        try
        {
            PreparedStatement query = connection.prepareStatement("SELECT word, wordtype, definition FROM entries " +
                    "WHERE word =" +
                    " ? LIMIT 1");

            query.setString(1, wordToFind);
            ResultSet result = query.executeQuery();

            if (result.next())
            {
                return result.getString("definition");
            }
            else
            {
                this.logger.logError("No rows in database for word: %s", wordToFind);
            }
        }
        catch (SQLException e)
        {
            this.logger.logError("Definition not found for word: %s", wordToFind);
            this.logger.logError("Error message: " + e.getMessage());
        }

        return "No Definition";
    }

    /**
     * @param wordToCheck Word to check.
     * @return returns the boolean value indicating whether the word exists in our database.
     */
    public boolean isWordInDictionary(String wordToCheck)
    {
        try
        {
            PreparedStatement doesWordExistQuery = connection.prepareStatement("SELECT 1 FROM entries WHERE word = ? LIMIT 1");
            doesWordExistQuery.setString(1, wordToCheck);
            ResultSet doesWordExistResult = doesWordExistQuery.executeQuery();
            return doesWordExistResult.next();
        }
        catch (Exception e)
        {
            this.logger.logError("Definition not found");
            this.logger.logError("Error message:" + e.getMessage());
        }

        return false;
    }
}
