package com.example.project.services.sqlite.dAOs;

import com.example.project.services.Logger;
import com.example.project.models.User;
import com.example.project.services.PasswordHasher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * User database tests. Unit tests. Not using the actual database.
 */
public class UsersDAOTests
{
    private static final Logger testLogger = new Logger();
    private static final String prodDBPath = "databases/users.db";

    private Connection getConnectionToMockProductionDB(){
        return SQLiteDBCreator.getConnectionToMockProductionDB(prodDBPath, "users");
    }

    @Test
    void addUser()
    {
        var username = "testUsername";
        var password = "asdf";
        var dbLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
        var connection = getConnectionToMockProductionDB();
        var mockHasher = Mockito.mock(PasswordHasher.class);
        Mockito.when(mockHasher.hashPassword(password)).thenReturn("hashedPassword");
        var userDAO = new UsersDAO(mockHasher, connection, dbLogger);
        var user = new User(username, password, 1);

        // call method
        userDAO.addUser(user);

        // Assert user was added.
        assertUserExists(username, connection);
    }

    @Test
    void addUser_ThrowsSQLException()
    {
        var username = "testUsername";
        var dbLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
        var emptyDBConnection = SQLiteDBCreator.getConnectionToEmptyDB();
        var userDAO = new UsersDAO(emptyDBConnection, dbLogger);
        var user = new User(username, "asdf", 1);
        assertThrows(RuntimeException.class, () -> userDAO.addUser(user));
    }

    @Test
    void doesUserExist_DoesExist()
    {
        var username = "a username";
        var password = "aReallyGoodPassword";
        var newUser = new User(username, password, 1);
        var connection = getConnectionToMockProductionDB();

        this.addUser(newUser, connection);

        var dbLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
        var userDAO = new UsersDAO(connection, dbLogger);

        var actual = userDAO.doesUserExist(username);
        assertTrue(actual);
    }

    @Test
    void doesUserExist_DoesNotExist()
    {
        var connection = getConnectionToMockProductionDB();
        String sql = "SELECT username, password, highscore FROM users WHERE username = ?";
        var username = "nonexistent";
        try
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            // Assert that the user does not exist.
            assertFalse(result.next());
        }
        catch (SQLException e)
        {
            Assertions.fail(String.format("user not added. SQL Exception: %s", e.getMessage()));
        }
    }

    @Test
    void getUser_UserDoesExist()
    {
        var username = "a username";
        var pass = "aReallyGoodPassword";
        var newUser = new User(username, pass, 1);
        var connection = getConnectionToMockProductionDB();
        this.addUser(newUser, connection);

        var usersDAO = new UsersDAO(connection, new Logger());
        assertNotNull(usersDAO.getUser(username));
    }

    @Test
    void getUser_UserDoesNotExist()
    {
        var pass = "aReallyGoodPassword";
        var username = "a username";
        var connection = getConnectionToMockProductionDB();

        var usersDAO = new UsersDAO(connection, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));
        assertNull(usersDAO.getUser(username));
    }

    @Test
    void getUser_ThrowsSQLException()
    {
        var username = "a username";
        var pass = "aReallyGoodPassword";
        var connection = SQLiteDBCreator.getConnectionToEmptyDB();

        var usersDAO = new UsersDAO(connection, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));
        assertThrows(RuntimeException.class, () -> usersDAO.getUser(username), "Test getUser_UserDoesNotExist Failed");
    }

    /**
     * Test helper method.
     */
    private void addUser(User user, Connection conectionToDB)
    {
        String sql = "INSERT INTO users (username, password, highscore) VALUES (?, ?, ?)";

        try
        {
            PreparedStatement query = conectionToDB.prepareStatement(sql);
            query.setString(1, user.getUsername());
            query.setString(2, "hashedPassword");
            query.setInt(3, user.getHighscore());
            query.executeUpdate();
        }
        catch (SQLException e)
        {
            Assertions.fail("Failed to add user.");
        }
    }

    /**
     * Test helper method.
     */
    private void assertUserExists(String username, Connection connectionToTheDBToCheck)
    {
        String sql = "SELECT username, password, highscore FROM users WHERE username = ?";

        try
        {
            PreparedStatement statement = connectionToTheDBToCheck.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            String passwordHash = result.getString("password");
            int highscore = result.getInt("highscore");

            if (!result.next())
            {
                Assertions.fail("user not added.");
            }
        }
        catch (SQLException e)
        {
            Assertions.fail(String.format("user not added. SQL Exception: %s", e.getMessage()));
        }
    }
}
