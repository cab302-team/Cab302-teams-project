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
        return "databases/oewn-2025-sqlite/sqlite-master/oewn-2025-sqlite-2.3.2.sqlite/oewn-2025-sqlite-2.3.2.sqlite";
    }
}
