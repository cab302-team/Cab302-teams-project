package com.example.project.models.gameScreens;

/**
 * Enum representing the different types of daily rewards the player can win from the spinning wheel.
 */
public enum DailyRewardType {

    /**
     * The player gets one free item from the shop (next item is free).
     */
    FREE_SHOP_ITEM,

    /**
     * The player receives $40 — the rarest reward.
     */
    DIAMOND_40,

    /**
     * The next word played earns double score and money.
     */
    LUCKY_WORD,

    /**
     * The player receives a small bonus of $1.
     */
    BONUS_1,

    /**
     * The player receives a moderate bonus of $5.
     */
    BONUS_5,

    /**
     * A funny or joke reward — purely cosmetic or humorous.
     */
    FUNNY_LOL
}