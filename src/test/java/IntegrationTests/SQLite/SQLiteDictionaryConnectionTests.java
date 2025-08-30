package IntegrationTests.SQLite;

import UnitTests.sqliteTests.ConnectionComparer;
import com.example.project.sqlite.SQLiteDictionaryConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to test {@link SQLiteDictionaryConnection}.
 */
public class SQLiteDictionaryConnectionTests
{
    private final String testDBPath = "databases/English-Dictionary-Open-Source-main/sqlite3/dictionary.db";

    @Test
    void getInstance_Initial()
    {
        String dbUrl = "jdbc:sqlite:" + testDBPath;

        if (!Files.exists(Paths.get(testDBPath)))
        {
            Assertions.fail("Database connection failed: Database file not found");
        }

        Connection expected = null;
        try{
            expected = DriverManager.getConnection(dbUrl);
        }
        catch (Exception e){
            Assertions.fail(e.getMessage());
        }

        var connectionResult = new SQLiteDictionaryConnection().getInstance();
        var equal = ConnectionComparer.equalData(expected, connectionResult);
        assertTrue(equal);
    }


    @Test
    void getInstance_SecondTime()
    {
        var initialConnection = new SQLiteDictionaryConnection().getInstance();
        var secondConnection = new SQLiteDictionaryConnection().getInstance();
        assertEquals(initialConnection, secondConnection);
    }

    @Test
    void getInstance_Initial_NoFileFound()
    {
//        Logger connectionLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
//
//        var nullSQLiteConnection = new SQLiteDictionaryConnection("not here", connectionLogger);
//
//        var expectedMessage = String.format("Database connection failed: Database file not found: not here%n");
//        assertThrows(RuntimeException.class, nullSQLiteConnection::getConnection, "Test GetConnectionNoFileFound failed getConnection did not" +
//                " " +
//                "throw.");
//        assertEquals(expectedMessage, connectionLogger.getErrorLogs(), "Test GetConnectionNoFileFound failed expected error message not " +
//                "matching.");
    }

    @Test
    void getInstance_ThrowsException()
    {
//        Logger connectionLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
//        var nullSQLiteConnection = new SQLiteConnection("not here", connectionLogger);
//
//        var expectedMessage = String.format("Database connection failed: Database file not found: not here%n");
//        assertThrows(RuntimeException.class, nullSQLiteConnection::getConnection);
//        assertEquals(expectedMessage, connectionLogger.getErrorLogs());
    }
}
