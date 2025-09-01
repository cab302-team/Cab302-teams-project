package com.example.project.sqlite;

/**
 * Connection to the dictionary.db
 */
public class SQLiteDictionaryConnection extends SQLiteConnection
{

    @Override
    protected String getDatabasePath()
    {
        return "databases/English-Dictionary-Open-Source-main/sqlite3/dictionary.db";
    }
}
