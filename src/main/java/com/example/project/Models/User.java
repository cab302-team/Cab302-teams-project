package com.example.project.Models;

/**
 * Represents a user in the application that can login.
 */
public class User {

    private final String username;

    private final String password;

    private final Integer highscore;

    /**
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
     * @param uname username.
     * @param password password.
     */
    public User(String uname, String password)
    {
        this.username = uname;
        this.password = password;
        this.highscore = 0;
    }

    /**
     * @return username.
     */
    public String getUsername(){
        return username;
    }

    /**
     * @return password.
     */
    public String getPassword(){
        return password;
    }

    /**
     * @return returns highscore.
     */
    public int getHighscore(){
        return this.highscore;
    }

}
