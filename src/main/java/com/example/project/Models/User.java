package com.example.project.Models;

public class User {

    private final String username;

    private final String password;

    private final Integer highscore;

    public User(String username, String password, int highscore)
    {
        this.highscore = highscore;
        this.username = username;
        this.password = password;
    }

    public User(String uname, String password)
    {
        this.username = uname;
        this.password = password;
        this.highscore = 0;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public int getHighscore(){
        return this.highscore;
    }

}
