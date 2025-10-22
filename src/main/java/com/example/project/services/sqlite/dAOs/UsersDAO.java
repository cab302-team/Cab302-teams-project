package com.example.project.services.sqlite.dAOs;

import com.example.project.services.Logger;
import com.example.project.services.PasswordHasher;
import com.example.project.models.User;
import com.example.project.services.sqlite.SQLiteUsersConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
