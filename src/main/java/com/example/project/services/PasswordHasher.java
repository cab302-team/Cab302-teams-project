package com.example.project.services;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordHasher {

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
    public static boolean checkPassword(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }
}
