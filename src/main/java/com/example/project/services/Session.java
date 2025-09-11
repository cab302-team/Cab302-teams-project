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

    public static Session getInstance()
    {
        if (instance == null)
        {
            instance = new Session();
        }

        return instance;
    }

    public double getMoney(){return money;}

    private Session()
    {
        // any initialising session stuff.
        // TODO: remove
        for (int i = 0; i < 3; i++){
            upgrades.add(UpgradeTiles.getTile(i));
        }
    }

    public void setUser(User newUser)
    {
        loggedInUser = newUser;
    }

    public Integer getHandSize(){
        return handSize;
    }

    public Integer getWordSize(){
        return wordViewSize;
    }

    public Integer getRedrawWindowSize() { return redrawWindowSize; }

    public List<UpgradeTile> getUpgrades(){
        return List.copyOf(upgrades);
    }
}
