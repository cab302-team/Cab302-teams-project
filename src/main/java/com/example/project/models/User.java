package com.example.project.models;

/**
 * Represents a user in the application that can log in.
 */
public class User {

    private final String username;

    private final String password;

    private final Integer highscore;

    /**
     * Constructor.
     * @param username username.
     * @param password password.
     * @param highscore highscore.
     */
    public User(String username, String password, int highscore)
    {
        this.highscore = highscore;
        this.username = username;
        this.password = password;
    }

    /**
     * get username.
     * @return username.
     */
    public String getUsername(){
        return username;
    }

    /**
     * get password.
     * @return password.
     */
    public String getPassword(){
        return password;
    }

    /**
     * get highscore.
     * @return returns highscore.
     */
    public int getHighscore(){
        return this.highscore;
    }

}
