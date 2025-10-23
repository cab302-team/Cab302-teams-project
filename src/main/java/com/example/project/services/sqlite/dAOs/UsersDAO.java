package com.example.project.services.sqlite.dAOs;

import com.example.project.services.Logger;
import com.example.project.services.PasswordHasher;
import com.example.project.models.User;
import com.example.project.services.sqlite.SQLiteUsersConnection;

import java.sql.*;

/**
 * SQLite Users database. with a table `users` 2 columns. 'username', 'password'. Which are both defined as unique
 * not null Strings in sqlite.
 */
public class UsersDAO
{
    private final Logger logger;
    private final Connection connection;
    PasswordHasher passwordHasher = new PasswordHasher();

    /**
     * Constructor for this class SQLLiteDictionary.
     */
    public UsersDAO()
    {
        this.logger = new Logger();
        this.connection = new SQLiteUsersConnection().getInstance();
    }

    protected UsersDAO(PasswordHasher hasher, Connection connection, Logger logger)
    {
        this(connection, logger);
        this.passwordHasher = hasher;
    }

    /**
     * Constructor with injection for unit tests.
     * @param connection Connection
     * @param logger logger.
     */
    public UsersDAO(Connection connection, Logger logger)
    {
        this.logger = logger;
        this.connection = connection;
    }

    /**
     * Adds user to the user.db.
     * password will be hashed before storing to ensure greater security (no plain text passwords)
     * @param user user to add.
     */
    public void addUser(User user)
    {
        String sql = "INSERT INTO users (username, password, highscore) VALUES (?, ?, ?)";

        try
        {
            PreparedStatement query = this.connection.prepareStatement(sql);
            query.setString(1, user.getUsername());

            query.setString(2, this.passwordHasher.hashPassword(user.getPassword())); //hashes password before storing

            query.setInt(3, user.getHighscore());
            query.executeUpdate();
            this.logger.logMessage(String.format("added user: %s to the database", user.getUsername()));
        }
        catch (SQLException e)
        {
            this.logger.logError(String.format("Failed to add user: %s to the database", user.getUsername()));
            throw new RuntimeException(String.format("Failed to add user to the database. SQL Error details: %s", e.getMessage()));
        }
    }

    /**
     * @param username username
     * @return returns bool indicating whether use is in database already.
     */
    public boolean doesUserExist(String username)
    {
        return queryByUsername(username) != null;
    }

    private User queryByUsername(String username)
    {
        String sql = "SELECT username, password, highscore FROM users WHERE username = ?";
        try
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if (!result.next()){ // if user does not exist in database return null.
                return null;
            }

            int highscore = result.getInt("highscore");
            String usersPassword = result.getString("password");
            return new User(username, usersPassword, highscore);
        }
        catch (SQLException e)
        {
            this.logger.logError(String.format("SQL Exception. Failed to get user username: %s", username));
            this.logger.logError(String.format("%s", e.getMessage()));
            throw new RuntimeException("Failed to get user by username", e);
        }
    }

    /**
     * @param username username.
     * @return returns user with matching username.
     */
    public User getUser(String username)
    {
        return queryByUsername(username);
    }

    /**
     * Saves the session data as JSON for a specific user.
     * @param username the username to save data for
     * @param sessionJson the JSON string containing session data
     */
    public void saveSessionData(String username, String sessionJson)
    {
        this.addSessionDataColumn();
        String sql = "UPDATE users SET session_data = ? WHERE username = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, sessionJson);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                logger.logMessage(String.format("Saved session data for user: %s", username));
            } else {
                logger.logError(String.format("No user found with username: %s", username));
            }

        } catch (SQLException e) {
            logger.logError(String.format("Failed to save session data for user: %s", username));
            logger.logError(String.format("SQL Error: %s", e.getMessage()));
            throw new RuntimeException("Failed to save session data", e);
        }
    }

    /**
     * Get session saved data.
     * @param username user.
     * @return returns string.
     */
    public String getSessionDataJson(String username) {
        String sql = "SELECT session_data FROM users WHERE username = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String sessionData = result.getString("session_data");
                logger.logMessage(String.format("Loaded session data for user: %s", username));
                return sessionData;
            } else {
                logger.logMessage(String.format("No session data found for user: %s", username));
                return null;
            }

        } catch (SQLException e) {
            logger.logError(String.format("Failed to load session data for user: %s", username));
            logger.logError(String.format("SQL Error: %s", e.getMessage()));
            throw new RuntimeException("Failed to load session data", e);
        }
    }

    private void addSessionDataColumn() {
        String sql = "ALTER TABLE users ADD COLUMN session_data TEXT";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            logger.logMessage("Added session_data column to users table");
        } catch (SQLException e) {
            // Column might already exist
            if (e.getMessage().contains("duplicate column")) {
                logger.logMessage("session_data column already exists");
            } else {
                logger.logError("Error adding session_data column: " + e.getMessage());
                throw new RuntimeException("Failed to add session_data column", e);
            }
        }
    }

    /**
     * Does user have save data.
     * @param user user.
     * @return returns true if save data exists.
     */
    public boolean hasSaveData(User user)
    {
        addSessionDataColumn();

        String sql = "SELECT session_data FROM users WHERE username = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String sessionData = result.getString("session_data");
                // Check if session_data is not null and not empty
                boolean hasSave = sessionData != null && !sessionData.trim().isEmpty();
                logger.logMessage(String.format("User %s %s save data",
                        user.getUsername(), hasSave ? "has" : "does not have"));
                return hasSave;
            } else {
                logger.logError(String.format("User not found: %s", user.getUsername()));
                return false;
            }

        } catch (SQLException e) {
            logger.logError(String.format("Failed to check save data for user: %s", user.getUsername()));
            logger.logError(String.format("SQL Error: %s", e.getMessage()));
            return false; // Return false instead of throwing to handle missing column gracefully
        }
    }
}
