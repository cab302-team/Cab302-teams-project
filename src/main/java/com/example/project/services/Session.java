package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.shopItems.UpgradeTiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Game Session. holds info of the current session.
 */
public class Session
{
    private static Integer handSize = 9;

    private static Integer wordViewSize = 9;

    private static Integer redrawWindowSize = 9;

    private static List<UpgradeTile> upgrades = new ArrayList<>();

    private static User loggedInUser;

    private static Integer money = 2;

    private static Session instance;

    /**
     * Gets the singleton instance of the session.
     * @return session active.
     */
    public static Session getInstance()
    {
        if (instance == null)
        {
            instance = new Session();
        }

        return instance;
    }

    /**
     * returns money in this session.
     * @return money.
     */
    public double getMoney() {return money;}

    private Session()
    {
        // any initialising session stuff.
        // TODO: remove
        for (int i = 0; i < 3; i++){
            upgrades.add(UpgradeTiles.getTile(i));
        }
    }

    /**
     * set new user.
     * @param newUser user that logged in.
     */
    public void setUser(User newUser)
    {
        loggedInUser = newUser;
    }

    /**
     * gets hand size.
     * @return returns number of tiles allowed in hand.
     */
    public int getHandSize(){
        return handSize;
    }

    /**
     * gets word size.
     * @return return int word size.
     */
    public int getWordSize(){
        return wordViewSize;
    }

    /**
     * gets redraw window size (number of slots)
     * @return return int redraw window size.
     */
    public Integer getRedrawWindowSize() { return redrawWindowSize; }

    /**
     * gets upgrades
     * @return list of upgrades.
     */
    public List<UpgradeTile> getUpgrades(){
        return List.copyOf(upgrades);
    }
}
