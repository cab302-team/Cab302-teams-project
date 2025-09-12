package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.shopItems.UpgradeTiles;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Game Session. holds info of the current session.
 */
public class Session
{
    private static final Integer handSize = 9;

    private static final Integer wordViewSize = 9;

    private final static ObservableList<UpgradeTile> upgrades = FXCollections.observableArrayList();

    private static User loggedInUser;

    private static Integer money = 2;

    private static Session instance;

    private int levelsBeaten = 0;

    /**
     * points required for the player to score at least to beat the current level.
     */
    private int levelRequirement = 1;

    private final int firstLevelScoreNeededToBeatIt = 1;

    /**
     * @return points required for the play to score at least to beat the level.
     */
    public int getPointsRequired()
    {
        return levelRequirement;
    }

    public static Session getInstance()
    {
        if (instance == null)
        {
            instance = new Session();
        }

        return instance;
    }

    public double getMoney() { return money; }

    private Session()
    {
        // any initialising session stuff.
        // TODO: remove after implementing SHOP
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

    public static ReadOnlyListProperty<UpgradeTile> upgradeTilesProperty(){
        return new ReadOnlyListWrapper<>(upgrades).getReadOnlyProperty();
    }

    public void incrementLevelPoints()
    {
        this.levelsBeaten++;
        this.levelRequirement += (int) Math.pow(2, this.levelsBeaten);
    }

    public void resetGame()
    {
        money = 0;
        levelsBeaten = 0;
        levelRequirement = firstLevelScoreNeededToBeatIt;
    }
}
