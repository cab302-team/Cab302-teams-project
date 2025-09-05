package com.example.project.models.sqlite.dAOs;

import com.example.project.Logger;
import com.example.project.StringHasher;
import com.example.project.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SQliteUser databse tests. Unit tests. Not using the actual database.
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
        var dbLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
        var connection = getConnectionToMockProductionDB();
        var userDAO = new UsersDAO(connection, dbLogger);
        var user = new User(username, "asdf", 1);
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
        var newUser = new User(username, "aReallyGoodPassword", 1);
        var connection = getConnectionToMockProductionDB();
        this.addUser(newUser, connection);
        var dbLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
        var userDAO = new UsersDAO(connection, dbLogger);
        assertTrue(userDAO.doesUserExist(username));
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
        var newUser = new User(username, "aReallyGoodPassword", 1);
        var connection = getConnectionToMockProductionDB();
        this.addUser(newUser, connection);

        var usersDAO = new UsersDAO(connection, new Logger());
        usersDAO.getUser(username);
    }

    @Test
    void getUser_UserDoesNotExist()
    {
        var username = "a username";
        var connection = getConnectionToMockProductionDB();

        var usersDAO = new UsersDAO(connection, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));
        assertNull(usersDAO.getUser(username));
    }

    @Test
    void getUser_ThrowsSQLException()
    {
        var username = "a username";
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
            query.setString(2, StringHasher.hash(user.getPassword()));
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
            String passwordUnhashed = StringHasher.UndoHash(passwordHash);
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
