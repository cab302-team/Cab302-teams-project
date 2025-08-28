package com.example.project.sqlite;

import com.example.project.Logger;

import java.sql.*;

/**
 * The SQLite Dictionary.
 */
public class SQLiteDictionary
{
    private final Connection connection;
    private final Logger logger;
    private static final String pathToDB = "/English-Dictionary-Open-Source-main/sqlite3/dictionary.db";

    /**
     * Constructor for this class SQLLiteDictionary.
     * @param logger logger to use.
     */
    public SQLiteDictionary(Logger logger)
    {
        this.connection = new SQLiteConnection(pathToDB, new Logger()).getConnection();
        this.logger = logger;
    }

    /**
     * constructor with injection for tests.
     *
     * @param connection mock connection.
     * @param logger a mock logger.
     */
    public SQLiteDictionary(Connection connection, Logger logger) {
        this.connection = connection;
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
                this.logger.logError(String.format("No rows in database for word: %s", wordToFind));
            }
        }
        catch (SQLException e)
        {
            this.logger.logError(String.format("Definition not found for word: %s", wordToFind));
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
        catch (SQLException e)
        {
            this.logger.logError("word not found due to exception");
            this.logger.logError("Error message:" + e.getMessage());
        }

        return false;
    }
}
