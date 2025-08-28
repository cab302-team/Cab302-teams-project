package sqliteTests;

import com.example.project.Logger;
import com.example.project.sqlite.SQLiteConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SQLite dictionary database Connection tests. Testing sqlite connection logic.
 */
public class SQLiteConnectionTests
{
    private final String testDBPath = "/test.db";

    @Test
    void getInstanceNewConnectionFromResource()
    {
        // NOTE: doing it this way makes it a read-only database.
        URL resourceURL = SQLiteConnection.class.getResource(testDBPath);
        assertNotNull(resourceURL, "resource URL null");
        String dbUrl = "jdbc:sqlite:" + resourceURL.getPath();
        Connection expected = null;
        try{
            expected = DriverManager.getConnection(dbUrl);
        }
        catch (Exception e){
            Assertions.fail(e.getMessage());
        }

        var connectionResult = new SQLiteConnection(testDBPath, new Logger()).getConnection();
        var equal = ConnectionComparer.equalData(expected, connectionResult);
        assertTrue(equal);
    }

    @Test
    void getInstanceExistingConnection()
    {
        SQLiteConnection sqliteConnector = new SQLiteConnection(testDBPath, new Logger());
        Connection conn = sqliteConnector.getConnection();

        var connectionInitial = sqliteConnector.getConnection();
        // get connection

        // get connection again
        var secondTimeConnection = sqliteConnector.getConnection();

        // should be the same pointers in memory
        assertEquals(connectionInitial, secondTimeConnection);
    }

    @Test
    void GetConnectionNoFileFound()
    {
        Logger connectionLogger = new Logger();
        var nullSQLiteConnection = new SQLiteConnection("not here", connectionLogger);

        // call the method that will throw
        System.out.println("Testing SQL Error:");
        System.out.flush(); // to log above first for test logs.
        var expectedMessage = String.format("Database connection failed: Database file not found: not here%n");
        assertThrows(RuntimeException.class, nullSQLiteConnection::getConnection);
        assertEquals(expectedMessage, connectionLogger.getErrorLogs());
        System.err.flush();
    }

    @Test
    void getConnectionFailedToConnect()
    {
        // Mock driverManager
        // DriverManager.getConnection().throw(new SQLException)

        Logger connectionLogger = new Logger();
        var nullSQLiteConnection = new SQLiteConnection("not here", connectionLogger);

        // call the method that will throw
        System.out.println("Testing SQL Error:");
        System.out.flush(); // to log above first for test logs.
        var expectedMessage = String.format("Database connection failed: Database file not found: not here%n");

        assertThrows(RuntimeException.class, nullSQLiteConnection::getConnection);
        assertEquals(expectedMessage, connectionLogger.getErrorLogs());
    }
}
