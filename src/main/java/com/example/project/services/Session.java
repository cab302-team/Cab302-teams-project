package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.shopItems.UpgradeTiles;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private final DoubleProperty money;

    private final int initialMoney;

    private User loggedInUser;

    private int levelsBeaten = 0;

    private final int initialLevelRequirement;

    private final Logger logger = new Logger();

    private final int initialRedraws = 4;
    private final ReadOnlyIntegerWrapper currentRedraws = new ReadOnlyIntegerWrapper(initialRedraws);
    private final int initialPlays = 4;
    private final ReadOnlyIntegerWrapper currentPlays = new ReadOnlyIntegerWrapper(initialPlays);
    private final UsersDAO usersDB = new UsersDAO();

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
        money = new ReadOnlyDoubleWrapper(newMoney);
        initialLevelRequirement = newFirstLevelsRequirement;
        handSize = newHandSize;
        wordWindowSize = newWordViewSize;
        redrawWindowSize = newRedrawWindowSize;
        loggedInUser = newUser;
        upgrades.setAll(newUpgrades);
        levelsBeaten = newLevelsBeaten;
        levelRequirement = new ReadOnlyIntegerWrapper(currentLevelRequirement);
    }

    protected int getLevelsBeaten(){
        return levelsBeaten;
    }

    /**
     * Default constructor.
     */
    public Session()
    {
        initialLevelRequirement = 4;
        levelRequirement = new ReadOnlyIntegerWrapper(initialLevelRequirement);
        initialMoney = 0;
        money = new ReadOnlyDoubleWrapper(initialMoney);
    }

    /**
     * Returns the read-only money property for binding to UI components.
     * This allows UI elements to automatically update when the players money changes.
     * @return ReadOnlyIntegerProperty representing the player's current money amount
     */
    public DoubleProperty getMoneyProperty()
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
     * Checks if the player already claimed today’s reward.
     * @return true if already claimed today
     */
    public boolean hasClaimedRewardToday() {
        return LocalDate.now().equals(lastRewardDate);
    }

    /**
     * set new user.
     * @param newUser user that logged in.
     */
    public void setUser(User newUser) {
        loggedInUser = newUser;
    }

    /**
     * Returns logged in user.
     * @return user.
     */
    public User getUser(){
        return loggedInUser;
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
    public ReadOnlyListProperty<UpgradeTileModel> getPlayersUpgradesProperty() {
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
        upgrades.clear();
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
     * Adds or remove money to the player's balance.
     * @param amount amount to add
     */
    public void modifyMoney(double amount) {
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
     * Resets the player's money to the initial state (e.g. 0).
     */
    public void resetMoney() {
        this.money.set(0);
    }

    /**
     * will save a copy of this session data to local drive.
     */
    public void Save()
    {
        // Add save data to users database
        SessionData data = new SessionData();
        data.money = this.money.get();
        data.levelsBeaten = this.levelsBeaten;
        data.levelRequirement = this.levelRequirement.get();
        // TODO: save current initial plays, redraws
        // add a current initial plays redraws variable. as level scene redraws plays should be initialised.
        // to that and any upgrade effects may have changed the max plays/redraws.
        data.currentPlays = this.currentPlays.get();
        data.currentRedraws = this.currentRedraws.get();
        data.lastRewardDate = this.lastRewardDate != null ? this.lastRewardDate.toString() : null;
        data.username = this.loggedInUser.getUsername();

        for (UpgradeTileModel tile : this.upgrades)
        {
            data.upgradeNames.add(tile.getName());
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);
        this.usersDB.saveSessionData(loggedInUser.getUsername(), json);
    }

    /**
     * Load logged in users data.
     */
    public void load()
    {
        try {
            // Get session data from database
            String json = this.usersDB.getSessionDataJson(this.loggedInUser.getUsername());

            // No saved data exists
            if (json == null || json.isEmpty()) {
                this.logger.logError("No save data found for user: " + this.loggedInUser.getUsername());
                return;
            }

            // Parse JSON
            Gson gson = new Gson();
            SessionData data = gson.fromJson(json, SessionData.class);

            // Restore session state
            this.money.set(data.money);
            this.levelsBeaten = data.levelsBeaten;
            this.levelRequirement.set(data.levelRequirement);
            this.currentPlays.set(data.currentPlays);
            this.currentRedraws.set(data.currentRedraws);
            this.lastRewardDate = data.lastRewardDate != null ? LocalDate.parse(data.lastRewardDate) : null;

            // Restore upgrades
            this.upgrades.clear();
            if (data.upgradeNames != null) {
                for (String name : data.upgradeNames)
                {
                    UpgradeTileModel upgrade = UpgradeTiles.getUpgradeByName(name);
                    if (upgrade != null) this.upgrades.add(upgrade);
                }
            }

            this.logger.logMessage(String.format("Successfully loaded session for user: " + this.loggedInUser.getUsername()));

        } catch (Exception e)
        {
            this.logger.logError("Failed to load session data: " + e.getMessage());
        }
    }
}
