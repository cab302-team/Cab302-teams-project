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
    void getLogs()
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

        newLogger.logError("testing Log Error");
        assertEquals(String.format("testing Log Error%n"), byteArray.toString());
    }

    @Test
    void logMessage()
    {
        var capturedSystemStdOutByteArray = new ByteArrayOutputStream();
        Logger newLogger = new Logger(new ByteArrayOutputStream(), capturedSystemStdOutByteArray);
        newLogger.setPrintToConsole(true);
        newLogger.logMessage("test log message");
        assertEquals(String.format("test log message%n"), capturedSystemStdOutByteArray.toString());
    }
}
