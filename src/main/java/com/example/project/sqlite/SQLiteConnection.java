package com.example.project.sqlite;

import com.example.project.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract SQLiteConnection class.
 */
public abstract class SQLiteConnection
{
    private static Connection instance = null;
    private static final Logger logger = new Logger();

    /**
     * Each subclass provides its database path.
     */
    protected abstract String getDatabasePath();

    /**
     * Gets the database connection from an existing readonly resource .db
     * @return returns the connection is already made or creates new one.
     */
    public Connection getInstance()
    {
        if (instance != null){
            return instance;
        }

        if (!Files.exists(Paths.get(getDatabasePath())))
        {
            logger.logError(String.format("Database connection failed: Database file not found: %s", getDatabasePath()));
            throw new RuntimeException("Cannot initialize connection");
        }

        instance = createConnection();
        return instance;
    }

    private Connection createConnection()
    {
        String dbUrl = "jdbc:sqlite:" + getDatabasePath();
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException sqlEx)
        {
            logger.logError("Database connection failed: " + sqlEx.getMessage());
            throw new RuntimeException("Cannot initialize connection", sqlEx);
        }

        return connection;
    }
}
