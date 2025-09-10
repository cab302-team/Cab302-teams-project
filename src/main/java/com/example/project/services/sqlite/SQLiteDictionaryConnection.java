package com.example.project.services.sqlite;

import java.sql.Connection;

/**
 * Connection to the dictionary.db
 */
public class SQLiteDictionaryConnection extends SQLiteConnection
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
        return "databases/English-Dictionary-Open-Source-main/sqlite3/dictionary.db";
    }
}
