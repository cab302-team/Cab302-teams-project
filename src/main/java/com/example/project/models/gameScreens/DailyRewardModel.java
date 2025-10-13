package com.example.project.models.gameScreens;

import com.example.project.services.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * Model that handles the logic for generating and applying daily rewards.
 */
public class DailyRewardModel {

    /**
     * List of all possible rewards a player can receive from the daily spinner.
     */
    private static final List<DailyRewardType> possibleRewards = List.of(
            DailyRewardType.FREE_SHOP_ITEM,
            DailyRewardType.DIAMOND_40,
            DailyRewardType.LUCKY_WORD
    );

    private static final Random random = new Random();

    /**
     * Randomly selects one reward from the available list.
     *
     * @return the randomly selected {@link DailyRewardType}
     */
    public static DailyRewardType rollReward() {
        return possibleRewards.get(random.nextInt(possibleRewards.size()));
    }

    /**
     * Applies the effects of the given reward to the current session.
     * Also records the date the reward was claimed.
     *
     * @param reward the {@link DailyRewardType} to apply
     */
    public static void applyReward(DailyRewardType reward) {
        Session session = Session.getInstance();

        switch (reward) {
            case DIAMOND_40 -> session.addMoney(40);
            case FREE_SHOP_ITEM -> session.markNextShopItemFree();
            case LUCKY_WORD -> session.activateLuckyWord();
        }

        session.setLastRewardDate(LocalDate.now());
    }
}
