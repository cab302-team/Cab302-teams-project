package com.example.project.sqlite;

import com.example.project.Logger;
import org.sqlite.SQLiteException;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Connects to any SQLite dictionary that already exists in the project's resources.
 */
public class SQLiteConnection
{
    private Connection instance = null;
    private Logger logger = new Logger();
    private final String databasePath;

    /**
     * @param pathToDB path to database in projects resources.
     */
    public SQLiteConnection(String pathToDB, Logger logger)
    {
        this.logger = logger;
        databasePath = pathToDB;
    }

    /**
     * @return returns the connection is already made or creates new one.
     */
    public Connection getConnection()
    {
        if (instance != null){
            return instance;
        }

        try
        {
            URL resourceURL = SQLiteConnection.class.getResource(databasePath);
            if (resourceURL == null){
                throw new SQLException("Database file not found: " + databasePath);
            }

            String dbUrl = "jdbc:sqlite:" + resourceURL.getPath();
            instance = DriverManager.getConnection(dbUrl);
        } catch (SQLException sqlEx)
        {
            logger.logError("Database connection failed: " + sqlEx.getMessage());
            throw new RuntimeException("Cannot initialize dictionary", sqlEx);
        }

        return instance;
    }
}
