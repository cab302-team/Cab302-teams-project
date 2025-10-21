package com.example.project.models.gameScreens;

import com.example.project.services.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * Model for handling daily reward logic and state.
 */
public class DailyRewardModel {

    private static final List<DailyRewardType> possibleRewards = List.of(
            DailyRewardType.Daily_Reward_Won_1Dollar,
            DailyRewardType.Daily_Reward_Won_5Dollars,
            DailyRewardType.Daily_Reward_Won_NOTHING
    );

    private static final Random random = new Random();

    /**
     * Randomly selects a daily reward.
     * @return a randomly selected reward
     */
    public static DailyRewardType rollReward() {
        return possibleRewards.get(random.nextInt(possibleRewards.size()));
    }

    /**
     * Applies the effect of the given reward to the session and marks it as claimed.
     * @param reward the reward to apply
     */
    public static void applyReward(DailyRewardType reward) {
        Session session = Session.getInstance();

        switch (reward) {
            case Daily_Reward_Won_1Dollar -> session.modifyMoney(1);
            case Daily_Reward_Won_5Dollars -> session.modifyMoney(5);
            case Daily_Reward_Won_NOTHING -> {
                // no-op
            }
        }

        session.setLastRewardDate(LocalDate.now());
    }
}