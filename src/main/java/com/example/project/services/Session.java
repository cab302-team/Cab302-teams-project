package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.tiles.UpgradeTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Game Session. holds info of the current session.
 */
public class Session
{
    private static Integer handSize = 9;

    private static Integer wordViewSize = 9;

    private static List<UpgradeTile> upgrades = new ArrayList<>();

    private static User loggedInUser;

    private static Integer currency;

    private static Session instance;

    public static Session getInstance()
    {
        if (instance == null)
        {
            instance = new Session();
        }

        return instance;
    }

    private Session()
    {
        addPlaceHolderUpgradeCards();
    }

    private void addPlaceHolderUpgradeCards()
    {
        // TODO actual implementation of upgrade tiles then remove.
        var tileExample = new UpgradeTile("Simplicity", "Add +1 to multiplier for every empty letter slot in the word.", "/com/example/project/upgradeTileImages/Monk_29.png");
        upgrades.add(tileExample);
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

    public List<UpgradeTile> getUpgrades(){
        return List.copyOf(upgrades);
    }
}
