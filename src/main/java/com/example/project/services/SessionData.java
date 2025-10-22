package com.example.project.services;

import java.util.ArrayList;
import java.util.List;

/**
 * Session data.
 */
public class SessionData
{
    /**
     * current money.
     */
    public double money;

    /**
     * levels beaten.
     */
    public int levelsBeaten;

    /**
     * level requirement.
     */
    public int levelRequirement;

    /**
     * current max plays.
     */
    public int currentPlays;

    /**
     * current max redraws.
     */
    public int currentRedraws;

    /**
     * last reward date.
     */
    public String lastRewardDate;

    /**
     * username.
     */
    public String username;

    /**
     * upgrade names.
     */
    public List<String> upgradeNames = new ArrayList<>();
}
