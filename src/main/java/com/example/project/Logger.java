package com.example.project;

/**
 * com.example.project.Logger for Application and to show up in test and build CI workflow.
 */
public class Logger
{
    /**
     * Logs with arguments an error message to standard error. And adds a newline.
     * <p>
     * The message can include format specifiers like in {@link String#format(String, Object...)}.
     *
     * @param message the error message format string (e.g., "Failed to connect to %s")
     * @param args optional arguments referenced by the format specifiers in the message
     */
    public static void logError(String message, Object... args) {
        System.err.printf(message + "%n", args);
    }

    /**
     * Logs an error message to standard error. And adds a newline.
     * <p>
     * The message can include format specifiers like in {@link String#format(String, Object...)}.
     *
     * @param message the error message format string (e.g., "Failed to connect to %s")
     */
    public static void logError(String message) {
        System.err.printf(message + "%n");
    }
}
