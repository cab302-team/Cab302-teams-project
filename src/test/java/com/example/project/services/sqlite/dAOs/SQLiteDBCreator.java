package com.example.project.services.sqlite.dAOs;

import com.example.project.services.Logger;
import org.junit.jupiter.api.Assertions;

import java.sql.*;

/**
 * For using in tests only. To create mock databses of the production ones to test.
 */
class SQLiteDBCreator
{
    private static final Logger testLogger = new Logger();

    /**
     * Test helper method.
     */
    public static Connection getConnectionToMockProductionDB(String dbPath, String tableName)
    {
        String inMemoryDbUrl = "jdbc:sqlite::memory:";
        Connection memoryConn = null;

        try {
            memoryConn = DriverManager.getConnection(inMemoryDbUrl);

            // Attach the production DB temporarily
            Statement stmt = memoryConn.createStatement();
            stmt.executeUpdate("ATTACH DATABASE '" + dbPath + "' AS prod_db");

            // Query all table creation SQL statements from production DB
            ResultSet rs = stmt.executeQuery("SELECT sql FROM prod_db.sqlite_master WHERE type='table' AND name NOT " +
                    "LIKE 'sqlite_%';");

            while (rs.next()) {
                String createTableSql = rs.getString("sql");
                if (createTableSql != null && !createTableSql.isBlank()) {
                    stmt.executeUpdate(createTableSql);
                }
            }

            stmt.executeUpdate("DETACH DATABASE prod_db");
        } catch (SQLException e)
        {
            Assertions.fail(String.format("Failed to create in-memory DB with production schema: %s", e.getMessage()));
        }

        return memoryConn;
    }

    /**
     * Test helper method.
     */
    public static Connection getConnectionToEmptyDB()
    {
        String inMemoryDatabaseString = "jdbc:sqlite::memory:";

        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(inMemoryDatabaseString);
        } catch (SQLException sqlEx)
        {
            testLogger.logError("Database connection failed: " + sqlEx.getMessage());
            Assertions.fail();
        }

        return connection;
    }
}
