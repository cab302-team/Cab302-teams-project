package com.example.project.services.sqlite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.MockedStatic;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * tests.
 */
public class SQLiteConnectionTests
{
    /**
     * Testable class.
     */
    public static class InvalidPathClass extends SQLiteConnection
    {
        @Override
        protected Connection getSQLiteInstance() {
            return null;
        }

        @Override
        protected void setSQLiteInstance(Connection newInstance) {

        }

        @Override
        protected String getDatabasePath() {
            return "nonExistantpath!!!!!";
        }
    }

    @Test
    void getInstance_pathNotFound()
    {
        var test = new InvalidPathClass();
        assertThrows(RuntimeException.class, test::getInstance);
    }

    /**
     * Testable class.
     */
    public static class ValidPathClass extends SQLiteConnection
    {
        @Override
        protected Connection getSQLiteInstance() {
            return null;
        }

        @Override
        protected void setSQLiteInstance(Connection newInstance) {

        }

        @Override
        protected String getDatabasePath() {
            return "";
        }
    }

    @Test
    void getInstance_DriverManagerThrowsSQLException()
    {
        var connection = new ValidPathClass();
        SQLiteException ex = new SQLiteException("failed.", SQLiteErrorCode.SQLITE_ERROR);

        try (MockedStatic<DriverManager> mockedDriver = mockStatic(DriverManager.class))
        {
            mockedDriver.when(() -> DriverManager.getConnection(anyString())).thenThrow(ex);
            assertThrows(RuntimeException.class, connection::getInstance);
        }
    }
}
