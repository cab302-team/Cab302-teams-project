package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.gameScreens.LevelModel;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.shopItems.UpgradeTiles;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;


/**
 * Game Session. holds info of the current session.
 */
public class Session
{
    private Integer handSize = 9;

    private Integer wordWindowSize = 9;

    private Integer redrawWindowSize = 9;

    private final ObservableList<UpgradeTileModel> upgrades = FXCollections.observableArrayList();

    private final IntegerProperty money;

    private final int initialMoney;

    private User loggedInUser;

    private LevelModel levelModel;

    private int levelsBeaten = 0;

    private final int initialLevelRequirement;

    private final int initialRedraws = 4;
    private final ReadOnlyIntegerWrapper currentRedraws = new ReadOnlyIntegerWrapper(initialRedraws);
    private final int initialPlays = 4;
    private final ReadOnlyIntegerWrapper currentPlays = new ReadOnlyIntegerWrapper(initialPlays);

    private static Session instance;

    /**
     * Gets session.
     * @return session instance.
     */
    public static Session getInstance()
    {
        if (instance == null){
            instance = new Session();
        }

        return instance;
    }

    /**
     * points required for the player to score at least to beat the current level.
     */
    private final ReadOnlyIntegerWrapper levelRequirement;

    /**
     * @return points required for the play to score at least to beat the level.
     */
    public ReadOnlyIntegerWrapper getLevelRequirement() {
        return levelRequirement;
    }

    /**
     * Constructor for injecting values in for unit test.
     * @param newHandSize hand size.
     * @param newWordViewSize word length allowed.
     * @param newRedrawWindowSize redraw window size.
     * @param newUpgrades upgrade tiles.
     * @param newUser user.
     * @param newMoney money.
     * @param newLevelsBeaten levels beaten.
     * @param currentLevelRequirement current level requirement.
     * @param newFirstLevelsRequirement first level requirement.
     */
    protected Session(int newHandSize, int newWordViewSize, int newRedrawWindowSize,
                      ObservableList<UpgradeTileModel> newUpgrades, User newUser,
                      int newMoney, int newLevelsBeaten,
                      int currentLevelRequirement, int newFirstLevelsRequirement, int newInitialMoney)
    {
        initialMoney = newInitialMoney;
        money = new ReadOnlyIntegerWrapper(newMoney);
        initialLevelRequirement = newFirstLevelsRequirement;
        handSize = newHandSize;
        wordWindowSize = newWordViewSize;
        redrawWindowSize = newRedrawWindowSize;
        loggedInUser = newUser;
        upgrades.setAll(newUpgrades);
        levelsBeaten = newLevelsBeaten;
        levelRequirement = new ReadOnlyIntegerWrapper(initialLevelRequirement);
    }

    protected int getLevelsBeaten(){
        return levelsBeaten;
    }

    /**
     * sets the session's level model
     * @param levelModel the current level model
     */
    public void setLevelModel(LevelModel levelModel) {
        this.levelModel = levelModel;
    }

    /**
     * @return the current level model
     */
    public LevelModel getLevelModel() {
        return levelModel;
    }

    /**
     * Default constructor.
     */
    public Session()
    {
        initialLevelRequirement = 4;
        levelRequirement = new ReadOnlyIntegerWrapper(initialLevelRequirement);
        initialMoney = 0;
        money = new ReadOnlyIntegerWrapper(initialMoney);
        upgrades.add(UpgradeTiles.getTile(1));
    }

    /**
     * Returns the read-only money property for binding to UI components.
     * This allows UI elements to automatically update when the players money changes.
     * @return ReadOnlyIntegerProperty representing the player's current money amount
     */
    public IntegerProperty getMoneyProperty()
    {
        return money;
    }

    /**
     * Adds an upgrade tile to the player's collection.
     * This will automatically update all UI displays bound to the upgrades property.
     *
     * @param upgrade this upgrades the tile to add it to the players collection
     */
    public void addUpgrade(UpgradeTileModel upgrade)
    {
        upgrades.add(upgrade);
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
    public boolean spendMoney(int amount)
    {
        if (amount < 0)
        {
            throw new IllegalArgumentException("You do not have enough funds");
        }
        if (money.get() >= amount)
        {
            money.set(money.get() - amount);
            return true;
        }
        return false;
    }


    /**
     * set new user.
     * @param newUser user that logged in.
     */
    public void setUser(User newUser) {
        loggedInUser = newUser;
    }

    /**
     * gets hand size.
     * @return returns number of tiles allowed in hand.
     */
    public int getHandSize() {
        return handSize;
    }

    /**
     * gets word size.
     * @return return int word size.
     */
    public int getWordWindowSize() {
        return wordWindowSize;
    }

    /**
     * gets upgrade tile property
     * @return upgrade tiles model list
     */
    public ReadOnlyListProperty<UpgradeTileModel> getUpgradeTilesProperty() {
        return new ReadOnlyListWrapper<>(upgrades);
    }

    /**
     * Increments how many points are required to beat the level.
     */
    public void updateLevelInfo() {
        this.levelsBeaten++;
        var current = this.levelRequirement.get();
        this.levelRequirement.set(current + (int) Math.pow(2, this.levelsBeaten));
    }

    /**
     * Resets the current session when you lose
     */
    public void resetGame() {
        money.set(initialMoney); // resets the players account to their starting amount
        levelsBeaten = 0;
        levelRequirement.set(initialLevelRequirement);
        resetPlaysRedraws();
    }

    /**
     * gets redraw window size (number of slots)
     * @return return int redraw window size.
     */
    public Integer getRedrawWindowSize() {
        return redrawWindowSize;
    }

    /**
     * gets the current plays.
     * @return current plays remaining.
     */
    public ReadOnlyIntegerWrapper getCurrentPlays() {
        return currentPlays;
    }

    /**
     * gets the redraws property.
     * @return the current redraws.
     */
    public ReadOnlyIntegerWrapper getCurrentRedraws(){
        return currentRedraws;
    }

    /**
     * Reset the plays and redraws.
     */
    public void resetPlaysRedraws()
    {
        getCurrentRedraws().set(initialRedraws);
        getCurrentPlays().set(initialPlays);
    }



    private LocalDate lastRewardDate = null;

    /**
     * Adds money to the player's balance.
     * @param amount amount to add
     */
    public void addMoney(int amount) {
        this.money.set(this.money.get() + amount);
    }

    /**
     * Sets the date the daily reward was last claimed.
     * @param date LocalDate of the reward claim
     */
    public void setLastRewardDate(LocalDate date) {
        this.lastRewardDate = date;
    }

    /**
     * Checks if the player already claimed today’s reward.
     * @return true if already claimed today
     */
    public boolean hasClaimedRewardToday() {
        return LocalDate.now().equals(lastRewardDate);
    }
}
