package com.example.project.services.sqlite.dAOs;

import com.example.project.services.Logger;
import com.example.project.models.User;
import com.example.project.services.PasswordHasher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.sql.*;

import static org.mockito.Mockito.*;
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
            var dbLogger = new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream());
            var userDAO = new UsersDAO(connection, dbLogger);

            // Assert that the user does not exist.
            var exists = userDAO.doesUserExist(username);
            assertFalse(exists);
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

    @Test
    void getUser_queryByUsername_UserExists_ReturnsUser() throws Exception {
        var username = "testUser";
        var password = "hashedPassword";
        var connection = getConnectionToMockProductionDB();
        var dao = new UsersDAO(connection, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));

        // insert user into DB
        String sql = "INSERT INTO users (username, password, highscore) VALUES (?, ?, ?)";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, 100);
            stmt.executeUpdate();
        }

        User result = dao.getUser(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(100, result.getHighscore());
    }

    @Test
    void getUser_queryByUsername_UserDoesNotExist_ReturnsNull() throws Exception {
        var connection = getConnectionToMockProductionDB();
        var dao = new UsersDAO(connection, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));

        User result = dao.getUser("nonexistentUser");

        assertNull(result);
    }

    @Test
    void getUser_queryByUsername_SQLException_ThrowsRuntimeException() throws Exception {
        // Create a mock connection that throws SQLException
        Connection mockConnection = Mockito.mock(Connection.class);
        UsersDAO dao = new UsersDAO(mockConnection, new Logger(new ByteArrayOutputStream(), new ByteArrayOutputStream()));

        assertThrows(RuntimeException.class, () -> dao.getUser("anyUser"));
    }

    @Test
    void saveSessionData_throwsSQLException()
    {
        String username = "testUser";
        String sessionJson = "{\"score\":100}";

        Connection mockConnection = Mockito.mock(Connection.class);
        var mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        var mockStatement = mock(PreparedStatement.class);
        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenThrow(new SQLException("failed"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> dao.saveSessionData(username, sessionJson), "Failed to save session data");

        // Verify prepared statement was created with correct SQL
        try {
            verify(mockConnection).prepareStatement("UPDATE users SET session_data = ? WHERE username = ?");
            // Verify parameters were set correctly
            verify(mockStatement).setString(1, sessionJson);
            verify(mockStatement).setString(2, username);
            // Verify executeUpdate called
            verify(mockStatement).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveSessionData_userExists()
    {
        String username = "testUser";
        String sessionJson = "{\"score\":100}";

        Connection mockConnection = Mockito.mock(Connection.class);
        var mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        var mockStatement = mock(PreparedStatement.class);
        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dao.saveSessionData(username, sessionJson);

        // Verify prepared statement was created with correct SQL
        try {
            verify(mockConnection).prepareStatement("UPDATE users SET session_data = ? WHERE username = ?");
            verify(mockStatement).setString(1, sessionJson);
            verify(mockStatement).setString(2, username);
            verify(mockStatement).executeUpdate();
            verify(mockLogger, times(1)).logMessage(String.format("Saved session data for user: %s", username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveSessionData_usersDoesNotExist()
    {
        String username = "testUser";
        String sessionJson = "{\"score\":100}";

        Connection mockConnection = Mockito.mock(Connection.class);
        var mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        var mockStatement = mock(PreparedStatement.class);
        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dao.saveSessionData(username, sessionJson);

        // Verify prepared statement was created with correct SQL
        try {
            verify(mockConnection).prepareStatement("UPDATE users SET session_data = ? WHERE username = ?");
            verify(mockStatement).setString(1, sessionJson);
            verify(mockStatement).setString(2, username);
            verify(mockStatement).executeUpdate();
            verify(mockLogger).logError(String.format("No user found with username: %s", username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @Test
    void hasSaveData_userHasData() throws SQLException {
        User user = new User("testUser", "pass", 0);
        Connection mockConnection = Mockito.mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("session_data")).thenReturn("{\"score\":100}");

        boolean result = dao.hasSaveData(user);

        assertTrue(result);
        verify(mockLogger, times(1)).logMessage("User testUser has save data");
        verify(mockStatement).setString(1, user.getUsername());
    }

    @Test
    void hasSaveData_userHasNoData_dataIsNull() throws SQLException {
        User user = new User("testUser", "pass", 0);
        Connection mockConnection = Mockito.mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("session_data")).thenReturn(null); // no save data

        boolean result = dao.hasSaveData(user);

        assertFalse(result);
        verify(mockLogger, times(1)).logMessage("User testUser does not have save data");
    }

    @Test
    void hasSaveData_userHasNoData_dataIsIncorrectlyFormatted() throws SQLException
    {
        User user = new User("testUser", "pass", 0);
        Connection mockConnection = Mockito.mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("session_data")).thenReturn(""); // no save data

        boolean result = dao.hasSaveData(user);

        assertFalse(result);
        verify(mockLogger, times(1)).logMessage("User testUser does not have save data");
    }

    @Test
    void hasSaveData_userNotFound() throws SQLException {
        User user = new User("missingUser", "pass", 0);
        Connection mockConnection = Mockito.mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // user not found

        boolean result = dao.hasSaveData(user);

        assertFalse(result);
        verify(mockLogger, times(1)).logError("User not found: missingUser");
    }

    @Test
    void hasSaveData_sqlException() throws SQLException {
        User user = new User("testUser", "pass", 0);
        Connection mockConnection = Mockito.mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("fail"));

        boolean result = dao.hasSaveData(user);

        assertFalse(result);
        verify(mockLogger, times(1)).logError("Failed to check save data for user: testUser");
        verify(mockLogger, times(1)).logError("SQL Error: fail");
    }

    @Test
    void getSessionData_userHasData() throws SQLException {
        String username = "testUser";
        String expectedData = "{\"level\":5,\"score\":100}";

        Connection mockConnection = mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("session_data")).thenReturn(expectedData);

        String result = dao.getSessionDataJson(username);

        assertEquals(expectedData, result);
        verify(mockLogger, times(1))
                .logMessage("Loaded session data for user: " + username);
    }

    @Test
    void getSessionData_userNotFound() throws SQLException {
        String username = "missingUser";

        Connection mockConnection = mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        String result = dao.getSessionDataJson(username);

        assertNull(result);
        verify(mockLogger, times(1))
                .logMessage("No session data found for user: " + username);
    }

    @Test
    void getSessionData_sqlException() throws SQLException {
        String username = "testUser";

        Connection mockConnection = mock(Connection.class);
        Logger mockLogger = mock(Logger.class);
        UsersDAO dao = new UsersDAO(mockConnection, mockLogger);

        when(mockConnection.prepareStatement(anyString()))
                .thenThrow(new SQLException("fail"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dao.getSessionDataJson(username);
        });

        assertEquals("Failed to load session data", exception.getMessage());
        verify(mockLogger, times(1))
                .logError("Failed to load session data for user: " + username);
        verify(mockLogger, times(1))
                .logError("SQL Error: fail");
    }
}
