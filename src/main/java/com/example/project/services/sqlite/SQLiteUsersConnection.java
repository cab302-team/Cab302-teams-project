package com.example.project.services.sqlite;

import java.sql.Connection;

/**
 * Connection to the users.db
 */
public class SQLiteUsersConnection extends SQLiteConnection
{
    private static Connection instance;

    @Override
    protected Connection getSQLiteInstance() {
        return instance;
    }

    @Override
    protected void setSQLiteInstance(Connection newInstance) {
        instance = newInstance;
    }

    @Override
    protected String getDatabasePath()
    {
        return "databases/users.db";
    }
}
