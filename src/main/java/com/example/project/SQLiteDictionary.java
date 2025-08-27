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

    /**
     * Constructor for this class SQLLiteDictionary.
     */
    public SQLiteDictionary()
    {
        this.connection = SQLiteDictionaryConnection.getInstance();
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
                Logger.logError("No rows in database for word: %s", wordToFind);
            }
        }
        catch (SQLException e)
        {
            Logger.logError("Definition not found for word: %s", wordToFind);
            Logger.logError("Error message: " + e.getMessage());
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
            System.out.printf("does word %s exist? = %b \n", wordToCheck, doesWordExistResult.next());
        }
        catch (Exception e)
        {
            Logger.logError("Definition not found");
            Logger.logError("Error message:" + e.getMessage());
        }

        return false;
    }
}
