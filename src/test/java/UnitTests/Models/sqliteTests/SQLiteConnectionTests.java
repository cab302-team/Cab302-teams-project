//package sqliteTests;
//
//import com.example.project.Logger;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayOutputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * SQLite dictionary database Connection tests. Testing sqlite connection logic.
// */
//public class SQLiteConnectionTests
//{
//    private final String testDBPath = "/test.db";
//
//    @Test
//    void getInstanceNewConnection()
//    {
//        String dbUrl = "jdbc:sqlite:" + testDBPath;
//        Connection expected = null;
//        try{
//            expected = DriverManager.getConnection(dbUrl);
//        }
//        catch (Exception e){
//            Assertions.fail(e.getMessage());
//        }
//
//        var connectionResult = new SQLiteConnection(testDBPath, new Logger()).getConnection();
//        var equal = ConnectionComparer.equalData(expected, connectionResult);
//        assertTrue(equal);
//    }
//
//    @Test
//    void getInstanceExistingConnection()
//    {
//        SQLiteConnection sqliteConnector = new SQLiteConnection(testDBPath, new Logger());
//        Connection conn = sqliteConnector.getConnection();
//
//        var connectionInitial = sqliteConnector.getConnection();
//        // get connection
//
//        // get connection again
//        var secondTimeConnection = sqliteConnector.getConnection();
//
//        // should be the same pointers in memory
//        assertEquals(connectionInitial, secondTimeConnection);
//    }
//
//    @Test
//    void GetConnectionNoFileFound()
//    {
//        Logger connectionLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
//        var nullSQLiteConnection = new SQLiteConnection("not here", connectionLogger);
//
//        var expectedMessage = String.format("Database connection failed: Database file not found: not here%n");
//        assertThrows(RuntimeException.class, nullSQLiteConnection::getConnection, "Test GetConnectionNoFileFound failed getConnection did not" +
//                " " +
//                "throw.");
//        assertEquals(expectedMessage, connectionLogger.getErrorLogs(), "Test GetConnectionNoFileFound failed expected error message not " +
//                "matching.");
//    }
//
//    @Test
//    void getConnectionFailedToConnect()
//    {
//        Logger connectionLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
//        var nullSQLiteConnection = new SQLiteConnection("not here", connectionLogger);
//
//        var expectedMessage = String.format("Database connection failed: Database file not found: not here%n");
//        assertThrows(RuntimeException.class, nullSQLiteConnection::getConnection);
//        assertEquals(expectedMessage, connectionLogger.getErrorLogs());
//    }
//}
