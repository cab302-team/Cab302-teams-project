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
    public static void initJavaFX()
    {
        if (!javafxStarted)
        {
            Platform.startup(() -> {}); // start JavaFX runtime once
            javafxStarted = true;
        }
    }
}
