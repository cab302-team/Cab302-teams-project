import com.example.project.Logger;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Logger class.
 */
public class LoggerTests
{
    @Test
    void clearLogs()
    {
        Logger newLogger = new Logger(true);
        newLogger.logError("Error");
        assertEquals(String.format("Error%n"), newLogger.getLogs());

        newLogger.clearLogs();
        assertEquals("", newLogger.getLogs());
    }

    @Test
    void getLogs()
    {
        // TODO: how to mock logger stream so that only the get is tested?

        var capture = new PrintStream(new ByteArrayOutputStream());
        capture.printf("Error" + "%n"); // capture in memory

        // TODO: the stream is not in the logger :( 
        Logger newLogger = new Logger(capture);
        assertEquals(String.format("Error%n"), newLogger.getLogs());
    }

    @Test
    void logErrorSuccess()
    {
        Logger newLogger = new Logger(true);
        newLogger.logError("Error");
        assertEquals(String.format("Error%n"), newLogger.getLogs());
    }
}
