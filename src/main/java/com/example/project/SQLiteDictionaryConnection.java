package com.example.project;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Connects to the SQLLite dictionary.db connection.
 */
public class SQLiteDictionaryConnection
{
    private static final String pathToDB = "/English-Dictionary-Open-Source-main/sqlite3/dictionary.db";
    private static Connection instance = null;
    private static final Logger logger = new Logger();

    private static Connection createConnection() {
        try
        {
            // NOTE: doing it this way makes it a read-only database.
            URL resourceURL = SQLiteDictionaryConnection.class.getResource(pathToDB);
            if(resourceURL == null){
                throw new IllegalArgumentException("Database file not found: " + pathToDB);
            }
            String dbUrl = "jdbc:sqlite:" + resourceURL.getPath();
            return DriverManager.getConnection(dbUrl);

        } catch (SQLException sqlEx)
        {
            logger.logError("Database connection failed: " + sqlEx.getMessage());
            logger.logError("Stack trace: " + Arrays.toString(sqlEx.getStackTrace()));
        }
        return null;
    }

    /**
     * @return returns the database connection instance.
     */
    public static Connection getInstance() {
        if (instance == null) {
            instance = createConnection();
        }
        return instance;
    }

}
