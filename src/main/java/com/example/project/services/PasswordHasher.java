package com.example.project.services;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordHasher {

    private static Logger logger = new Logger();

    /**
     * Hashes a raw password using BCrypt.
     *
     * @param rawPassword the plain text password
     * @return the hashed password string
     */
    public static String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * Verifies a candidate password against a hashed password.
     *
     * @param candidate the plain text password entered by the user
     * @param hashed    the hashed password stored in the database
     * @return true if the password matches, false otherwise
     */
    public static boolean checkPassword(String candidate, String hashed)
    {
        try
        {
            return BCrypt.checkpw(candidate, hashed);
        }
        catch (IllegalArgumentException e)
        {
            logger.logMessage(String.format("Password check failed, caught exception: %s", e.getMessage()));
            logger.logMessage(String.format("case: %s", e.getCause()));
            return false;
        }
    }
}
