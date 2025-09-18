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
    private Integer handSize = 9;

    private Integer wordViewSize = 9;

    private Integer redrawWindowSize = 9;

    private final ObservableList<UpgradeTile> upgrades = FXCollections.observableArrayList();

    private User loggedInUser;

    private final int initialMoney;

    private Integer money;

    private static Session instance;

    private int levelsBeaten = 0;

    private final int initialLevelRequirement;

    /**
     * points required for the player to score at least to beat the current level.
     */
    private int levelRequirement;

    /**
     * @return points required for the play to score at least to beat the level.
     */
    public int getLevelRequirement() {
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
                      ObservableList<UpgradeTile> newUpgrades, User newUser,
                      int newMoney, int newLevelsBeaten,
                      int currentLevelRequirement, int newFirstLevelsRequirement, int newInitialMoney)
    {
        initialMoney = newInitialMoney;
        handSize = newHandSize;
        wordViewSize = newWordViewSize;
        redrawWindowSize = newRedrawWindowSize;
        loggedInUser = newUser;
        upgrades.setAll(newUpgrades);
        money = newMoney;
        levelsBeaten = newLevelsBeaten;
        levelRequirement = currentLevelRequirement;
        initialLevelRequirement = newFirstLevelsRequirement;
        instance = this;
    }

    protected int getLevelsBeaten(){
        return levelsBeaten;
    }

    /**
     * Gets singleton instance.
     * @return session instance.
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }

        return instance;
    }

    private Session()
    {
        initialLevelRequirement = 4;
        levelRequirement = initialLevelRequirement;
        initialMoney = 2;

        // TODO: remove after implementing SHOP
        for (int i = 0; i < 3; i++) {
            upgrades.add(UpgradeTiles.getTile(i));
        }
    }

    /**
     * returns money in this session.
     * @return money.
     */
    public double getMoney() {
        return money;
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
    public int getWordSize() {
        return wordViewSize;
    }

    /**
     * gets upgrade tiles property.
     * @return upgrade tiles model list.
     */
    public ReadOnlyListProperty<UpgradeTile> getUpgradeTilesProperty() {
        return new ReadOnlyListWrapper<>(upgrades).getReadOnlyProperty();
    }

    /**
     * Increments how many points are required to beat the level.
     */
    public void updateLevelInfo() {
        this.levelsBeaten++;
        this.levelRequirement += (int) Math.pow(2, this.levelsBeaten);
    }

    /**
     * Reset the current session when you lose.
     */
    public void resetGame()
    {
        money = initialMoney;
        levelsBeaten = 0;
        levelRequirement = initialLevelRequirement;
    }

    /**
     * gets redraw window size (number of slots)
     * @return return int redraw window size.
     */
    public Integer getRedrawWindowSize() {
        return redrawWindowSize;
    }
}
