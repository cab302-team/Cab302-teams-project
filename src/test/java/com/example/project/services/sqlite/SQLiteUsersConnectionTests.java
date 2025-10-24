package com.example.project.services.sqlite;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * unit tests.
 */
public class SQLiteUsersConnectionTests
{
    private Connection getSameDBConnection()
    {
        var usersConnection = new SQLiteUsersConnection();

        String dbUrl = "jdbc:sqlite:" + usersConnection.getDatabasePath();
        try
        {
            return DriverManager.getConnection(dbUrl);
        } catch (SQLException sqlEx)
        {
            throw new RuntimeException("Cannot initialize connection", sqlEx);
        }
    }

    @Test
    void getInstance()
    {
        var connection = new SQLiteUsersConnection();
        var instance = connection.getInstance();

        ConnectionComparer.equalData(getSameDBConnection(), instance);
    }
}
