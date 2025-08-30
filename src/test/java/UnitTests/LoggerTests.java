package UnitTests;

import com.example.project.Logger;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Logger class.
 */
public class LoggerTests
{
    @Test
    void clearLogs()
    {
        var bytesWithInfoAlready = new ByteArrayOutputStream();
        var stringToLog = String.format("log this%n");
        bytesWithInfoAlready.writeBytes(stringToLog.getBytes());
        Logger newLogger = new Logger(bytesWithInfoAlready, new ByteArrayOutputStream());
        newLogger.clearLogs();
        assertEquals("", newLogger.getErrorLogs());
    }

    @Test
    void getErrorLogs()
    {
        var bytesWithInfoAlready = new ByteArrayOutputStream();
        var stringToLog = String.format("log this%n");
        bytesWithInfoAlready.writeBytes(stringToLog.getBytes());
        Logger newLogger = new Logger(bytesWithInfoAlready, new ByteArrayOutputStream());
        assertEquals(String.format("log this%n"), newLogger.getErrorLogs());
    }

    @Test
    void logError()
    {
        var byteArray = new ByteArrayOutputStream();
        Logger newLogger = new Logger(byteArray, new ByteArrayOutputStream());
        newLogger.setPrintToConsole(true);

        var test = "testing logging errors to console success.";
        newLogger.logError(test);
        assertEquals(String.format("%s%n", test), byteArray.toString());
    }

    @Test
    void logMessage()
    {
        var capturedSystemStdOutByteArray = new ByteArrayOutputStream();
        Logger newLogger = new Logger(new ByteArrayOutputStream(), capturedSystemStdOutByteArray);
        newLogger.setPrintToConsole(true);
        var testMsg = "testing logging to console success.";
        newLogger.logMessage(testMsg);
        assertEquals(String.format("%s%n", testMsg), capturedSystemStdOutByteArray.toString());
    }

    @Test
    void getLogs()
    {
        var stdOutBytesWithMsg = new ByteArrayOutputStream();
        var stringToLog = String.format("log this%n");
        stdOutBytesWithMsg.writeBytes(stringToLog.getBytes());
        Logger newLogger = new Logger(new ByteArrayOutputStream(), stdOutBytesWithMsg);
        assertEquals(String.format("log this%n"), newLogger.getLogs());
    }
}
