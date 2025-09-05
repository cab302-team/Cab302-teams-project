package com.example.project;

import javafx.application.Platform;

/**
 * for tests only. to initialise java fx tookit for some tests.
 */
public class initJavaToolkit
{
    private static boolean javafxStarted = false;

    /**
     * Initialise once per test run.
     */
    public static synchronized void initJavaFX()
    {
        if (!javafxStarted) {
            try {
                Platform.startup(() -> {});
                javafxStarted = true;
            } catch (IllegalStateException e) {
                if (e.getMessage().contains("Toolkit already initialized")) {
                    javafxStarted = true;
                } else {
                    throw e;
                }
            }
        }
    }
}
