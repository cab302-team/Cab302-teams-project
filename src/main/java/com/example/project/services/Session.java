package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.tiles.UpgradeTile;
import com.example.project.services.shopItems.UpgradeTiles;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
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

    private static final Integer redrawWindowSize = 9;

    private final static ObservableList<UpgradeTile> upgrades = FXCollections.observableArrayList();

    private static User loggedInUser;

    //changed money to use IntegerProperty for automatic UI updates
    private static final ReadOnlyIntegerWrapper money = new ReadOnlyIntegerWrapper(2);

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

    /**
     * Gets instance
     * @return session instance
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }

        return instance;
    }

    /**
     * Returns the read-only money property for binding to UI components.
     * This allows UI elements to automatically update when the players money changes.
     *
     * @return ReadOnlyIntegerProperty representing the player's current money amount
     */
    public static ReadOnlyIntegerProperty getMoneyProperty()
    {
        return money.getReadOnlyProperty();
    }

    /**
     * Returns the current money value as an integer.
     *
     * @return the current amount of money the player has
     */
    public static int getMoney()
    {
        return money.get();
    }

    /**
     *
     * @param upgrade this upgrades the tile to add it to the players collection
     */
    public static void addUpgrade(UpgradeTile upgrade)
    {
        upgrades.add(upgrade);
    }


    /**
     * Adds the specified amount of money to the player's account.
     * This will trigger updates to any UI elements bound to the money property.
     *
     * @param amount the amount of money to add (cannot be negative)
     * @throws IllegalArgumentException if amount is negative
     */
    public static void addMoney(int amount)
    {
        if (amount < 0)
        {
            throw new IllegalArgumentException("Cannot add a negative number");
        }
        money.set(money.get() + amount);
    }

    /**
     * This attempts to spend the specified amount of money from the player's account.
     * This operation should only succeed if the player has enough money.
     *
     * @param amount the amount of money to spend (cannot be negative)
     * @return true if the transaction was successful (player had enough money),
     *         false if the player is a brokey
     * @throws IllegalArgumentException if the amount is negative
     */
    public static boolean spendMoney(int amount)
    {
        if (amount < 0)
        {
            throw new IllegalArgumentException("Amount to spend cannot be a negative number");
        }
        if (money.get() >= amount)
        {
            money.set(money.get() - amount);
            return true;
        }
        return false;
    }


    private Session() {
        // any initialising session stuff.
        // TODO: remove after implementing SHOP
        for (int i = 0; i < 3; i++) {
            upgrades.add(UpgradeTiles.getTile(i));
        }
    }

    /**
     * set new user.
     *
     * @param newUser user that logged in.
     */
    public void setUser(User newUser) {
        loggedInUser = newUser;
    }

    /**
     * gets hand size.
     *
     * @return returns number of tiles allowed in hand.
     */
    public int getHandSize() {
        return handSize;
    }

    /**
     * gets word size.
     *
     * @return return int word size.
     */
    public int getWordSize() {
        return wordViewSize;
    }

    /**
     * gets upgrade tile property
     * @return upgrade tiles model list
     */
    public static ReadOnlyListProperty<UpgradeTile> getUpgradeTilesProperty() {
        return new ReadOnlyListWrapper<>(upgrades).getReadOnlyProperty();
    }

    /**
     * Increments how many points are required to beat the level.
     */
    public void incrementLevelPoints() {
        this.levelsBeaten++;
        this.levelRequirement += (int) Math.pow(2, this.levelsBeaten);
    }

    /**
     * Resets the current session when you lose
     */
    public void resetGame() {
        money.set(2); // resets the players account to their starting amount
        levelsBeaten = 0;
        levelRequirement = firstLevelScoreNeededToBeatIt;
    }

    /**
     * gets redraw window size (number of slots)
     *
     * @return return int redraw window size.
     */
    public Integer getRedrawWindowSize() {
        return redrawWindowSize;
    }
}
