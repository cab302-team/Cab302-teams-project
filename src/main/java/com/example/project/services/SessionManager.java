package com.example.project.services;

/**
 * class to manage the game session.
 */
public class SessionManager
{
    private static Session currentSession;

    /**
     * Gets current game session.
     * @return session.
     */
    public static Session getSession()
    {
        if (currentSession == null){
            currentSession = new Session();
        }

        return currentSession;
    }

    // TODO: save, load quit here.
}
