package com.example.project;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * com.example.project.Logger for Application and to show up in test and build CI workflow.
 */
public class Logger
{
    private final PrintStream consoleStream;
    private final PrintStream captureStream;
    private final ByteArrayOutputStream capturedLogContent = new ByteArrayOutputStream();
    private boolean captureLogs = false;
    private boolean printToConsole = true;

    /**
     * Default constructor writes to console does not capture.
     */
    public Logger()
    {
        this(false, true);
    }

     /**
      * Use this constructor in unit tests. Or wherever logs should be captured.
      * @param captureLogs if Logs should be captured.
      * @param printToConsole If logs should be printed to console.
      */
    private Logger(boolean captureLogs, boolean printToConsole)
    {
        this.printToConsole = printToConsole;
        this.captureLogs = captureLogs;
        this.consoleStream = System.err;
        this.captureStream = new PrintStream(capturedLogContent);
    }

    /**
     * Use this constructor in unit tests. Or wherever logs should be captured.
     * @param inUnitTest If in unit test. Should use. to capture and not print logs.
     */
    public Logger(boolean inUnitTest)
    {
        this(true, false);
    }

    public Logger(PrintStream captureStream) {
        this.printToConsole = false;
        this.captureLogs = true;
        this.consoleStream = System.err;
        this.captureStream = captureStream;
    }

    /**
     * @return Gets the log messages.
     */
    public String getLogs()
    {
        return this.capturedLogContent.toString();
    }

    /**
     * Logs an error message to standard error. And adds a newline.
     * <p>
     * The message can include format specifiers like in {@link String#format(String, Object...)}.
     *
     * @param message the error message format string (e.g., "Failed to connect to %s")
     */
    public void logError(String message)
    {
        this.logErrorWithCapture(message);
    }

    /**
     * @param message Message raw string.
     * @param args args for string foramtting.
     */
    public void logError(String message, Object... args)
    {
        this.logErrorWithCapture(String.format(message, args));
    }

    /**
     * Clears captured logs use in tests teardown.
     */
    public void clearLogs() {
        capturedLogContent.reset();
    }

    private synchronized void logErrorWithCapture(String formattedMessage)
    {
        if (this.captureLogs) {
            captureStream.printf(formattedMessage + "%n"); // capture in memory
        }
        if (printToConsole)
        {
            consoleStream.printf(formattedMessage + "%n"); // optional console output
        }
    }
}
