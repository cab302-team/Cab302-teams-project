package com.example.project.services;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;


public class PasswordHasherTests
{
    @Test
    void hashPasswordTest()
    {
        var hasher = new PasswordHasher();

        var password = "goodPassword";
        var hashed = hasher.hashPassword(password);

        String rawPassword = "myPassword";
        String expectedHash = "$2a$10$hashedPasswordValue";

        try (MockedStatic<BCrypt> MockedBCrypt = Mockito.mockStatic(BCrypt.class))
        {
            var salt = "$2a$10$salt";
            MockedBCrypt.when(BCrypt::gensalt).thenReturn(salt);

            // Mock hashpw() to return expected hash
            MockedBCrypt.when(() -> BCrypt.hashpw(rawPassword, salt)).thenReturn(expectedHash);


            // Test method
            String result = hasher.hashPassword(rawPassword);

            // assert
            assertEquals(expectedHash, result);
        }
    }

    @Test
    void doesPasswordMatch_ThrowsIllegalArgumentException_ReturnsFalse()
    {
        String rawPassword = "myPassword";
        String expectedHash = "$2a$10$hashedPassword";

        var hasher = new PasswordHasher();

        try (MockedStatic<BCrypt> mockedBCrypt = Mockito.mockStatic(BCrypt.class)) {
            mockedBCrypt.when(() -> BCrypt.checkpw(rawPassword, expectedHash)).thenThrow(IllegalArgumentException.class);

            assertFalse(hasher.doesPasswordMatch(rawPassword, expectedHash));
        }

    }

    @Test
    void doesPasswordMatch_True(){
        String rawPassword = "myPassword";
        String expectedHash = "$2a$10$hashedPasswordValue";

        var hasher = new PasswordHasher();

        try (MockedStatic<BCrypt> mockedBCrypt = Mockito.mockStatic(BCrypt.class))
        {
            mockedBCrypt.when(() -> BCrypt.checkpw(rawPassword, expectedHash)).thenReturn(true);
            assertTrue(hasher.doesPasswordMatch(rawPassword, expectedHash));
        }
    }

    @Test
    void doesPasswordMatch_False(){
        String rawPassword = "myPassword";
        String expectedHash = "$2a$10$hashedPasswordValue";

        var hasher = new PasswordHasher();

        try (MockedStatic<BCrypt> mockedBCrypt = Mockito.mockStatic(BCrypt.class))
        {
            mockedBCrypt.when(() -> BCrypt.checkpw(rawPassword, expectedHash)).thenReturn(false);
            assertFalse(hasher.doesPasswordMatch(rawPassword, expectedHash));
        }
    }
}
