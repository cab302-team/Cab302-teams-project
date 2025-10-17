package com.example.project.services;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        var mockStdErrStream = mock(PrintStream.class);
        var mockStdOutputStream = mock(PrintStream.class);

        var capturedSystemStdOutByteArray = new ByteArrayOutputStream();
        var capturedErrorOutBytes = new ByteArrayOutputStream();

        Logger newLogger = new Logger(capturedErrorOutBytes, capturedSystemStdOutByteArray, mockStdErrStream, mockStdOutputStream);
        newLogger.setPrintToConsole(true);
        var testMsg = "testing logging errors to console success.";

        // Act
        newLogger.logError(testMsg);

        // Assert
        var message = String.format("%s%n", testMsg);
        assertEquals(message, capturedErrorOutBytes.toString());
        verify(mockStdErrStream).printf(testMsg + "%n");
    }

    @Test
    void logMessage()
    {
        var mockStdErrStream = mock(PrintStream.class);
        var mockStdOutputStream = mock(PrintStream.class);

        var capturedSystemStdOutByteArray = new ByteArrayOutputStream();

        Logger newLogger = new Logger(new ByteArrayOutputStream(), capturedSystemStdOutByteArray, mockStdErrStream, mockStdOutputStream);
        newLogger.setPrintToConsole(true);
        var testMsg = "testing logging to console success.";

        // Act
        newLogger.logMessage(testMsg);

        // Assert
        var message = String.format("%s%n", testMsg);
        assertEquals(message, capturedSystemStdOutByteArray.toString());
        verify(mockStdOutputStream).printf(testMsg + "%n");
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
