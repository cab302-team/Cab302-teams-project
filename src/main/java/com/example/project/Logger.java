package com.example.project;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * com.example.project.Logger for Application and to show up in test and build CI workflow.
 */
public class Logger
{
    private final PrintStream consoleErrorStream;
    private final PrintStream captureErrorStream;
    private ByteArrayOutputStream capturedErrorLogs = new ByteArrayOutputStream();

    private final PrintStream consoleStdOutStream;
    private final PrintStream captureStdOutStream;
    private ByteArrayOutputStream capturedStdOutLogs = new ByteArrayOutputStream();

    private boolean printToConsole = true;

    /**
     * Default constructor writes to console does not capture. For the project files. Use below for unit tests.
     */
    public Logger()
    {
        this.consoleErrorStream = System.err;
        this.captureErrorStream = new PrintStream(capturedErrorLogs);

        this.consoleStdOutStream = System.out;
        this.captureStdOutStream = new PrintStream(capturedStdOutLogs);
    }

    /**
     * Constructor for unit tests. Logger with constructor to input the byte array output stream to write to. (for mocking a log to check get methods.)
     * @param mockCapturedErrorLog byte array to store error logs.
     * @param mockCapturedStdOutLog byte array to store standard output logs.
     */
    public Logger(ByteArrayOutputStream mockCapturedErrorLog, ByteArrayOutputStream mockCapturedStdOutLog) {
        this.setPrintToConsole(false);
        this.consoleErrorStream = System.err;
        this.capturedErrorLogs = mockCapturedErrorLog;
        this.captureErrorStream = new PrintStream(this.capturedErrorLogs);

        this.consoleStdOutStream = System.out;
        this.capturedStdOutLogs = mockCapturedStdOutLog;
        this.captureStdOutStream = new PrintStream(this.capturedStdOutLogs);
    }

    /**
     * @param value if this logger will also print to the console.
     */
    public void setPrintToConsole(boolean value){
        this.printToConsole = value;
    }

    /**
     * @return Gets the error log messages.
     */
    public String getErrorLogs()
    {
        return this.capturedErrorLogs.toString();
    }


    /**
     * @return returns the standard log messages.
     */
    public String getLogs(){ return this.capturedStdOutLogs.toString(); }

    /**
     * Logs an error message to standard error. And adds a newline.
     * <p>
     * The message can include format specifiers like in {@link String#format(String, Object...)}.
     *
     * @param message the error message format string (e.g., "Failed to connect to %s")
     */
    public void logError(String message)
    {
        this.logWithCapture(message, captureErrorStream, consoleErrorStream);
    }

    /**
     * Clears captured logs use in tests teardown.
     */
    public void clearLogs() {
        capturedErrorLogs.reset();
    }

    /**
     * Log message to System.out.
     * @param formattedMessage message.
     */
    public void logMessage(String formattedMessage)
    {
        this.logWithCapture(formattedMessage, captureStdOutStream, consoleStdOutStream);
    }

    private synchronized void logWithCapture(String formattedMessage, PrintStream captureStream, PrintStream consoleStream)
    {
        captureStream.printf(formattedMessage + "%n"); // capture in memory
        if (printToConsole)
        {
            consoleStream.printf(formattedMessage + "%n"); // optional console output
        }
    }
}
