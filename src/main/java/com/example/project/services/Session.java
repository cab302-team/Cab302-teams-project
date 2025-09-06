package com.example.project.services;

import com.example.project.models.User;

/**
 * Game Session. holds info of the current session.
 */
public class Session
{
    private User loggedInUser;

    private Integer currency;

    public void setUser(User newUser)
    {
        this.loggedInUser = newUser;
    }

}
