package com.example.project.services.sqlite;

import com.example.project.services.Logger;

import java.io.File;
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
    private static final Logger logger = new Logger();

    protected abstract Connection getSQLiteInstance();

    protected abstract void setSQLiteInstance(Connection newInstance);

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
        if (getSQLiteInstance() != null){
            return getSQLiteInstance();
        }

        if (!Files.exists(Paths.get(getDatabasePath())))
        {
            logger.logError(String.format("Database connection failed: Database file not found: %s", getDatabasePath()));
            throw new RuntimeException("Cannot initialize connection");
        }

        setSQLiteInstance(createConnection());
        return getSQLiteInstance();
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
