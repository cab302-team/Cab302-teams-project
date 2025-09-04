package UnitTests.Models.sqliteTests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * For usage in databaes tests to compare connections.
 */
public class ConnectionComparer
{
    /**
     * Compares 2 connections data not their pointers to the databse in memory.
     * @param a Connection 1
     * @param b Connection 2
     * @return returns true if equal.
     */
    public static boolean equalData(Connection a, Connection b)
    {
        try
        {
            var urlEqual = Objects.equals(a.getMetaData().getURL(), b.getMetaData().getURL());
            var schemaEqual = Objects.equals(a.getSchema(), b.getSchema());
            var tablesEqual = Objects.equals(getTables(a), getTables(b));
            return tablesEqual && urlEqual && schemaEqual;
        } catch (SQLException e) {
            return false;
        }
    }

    private static List<String> getTables(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<>();
        var meta = conn.getMetaData();

        try (ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        }
        return tables;
    }
}
