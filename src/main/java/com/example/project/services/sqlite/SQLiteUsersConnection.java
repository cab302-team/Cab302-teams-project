package com.example.project.services.sqlite;

/**
 * Connection to the users.db
 */
public class SQLiteUsersConnection extends SQLiteConnection
{
    @Override
    protected String getDatabasePath()
    {
        return "databases/users.db";
    }
}
