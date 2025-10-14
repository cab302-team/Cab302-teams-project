package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.tiles.UpgradeTileModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the session class.
 */
public class SessionTests
{
    @Test
    void getUpgradeTilesPropertyTest()
    {
        ObservableList<UpgradeTileModel> expected = FXCollections.observableArrayList();
        var testInstance = new Session(9, 9,
                9, expected, new User("aLongerName", "SuperSecretPassword2", 1),
                2, 0, 1, 1, 2);

        var actual = testInstance.getUpgradeTilesProperty();
        assertEquals(expected, actual);
    }

    @Test
    void updateLevelInfoTest(){
        ObservableList<UpgradeTileModel> upgrades = FXCollections.observableArrayList();
        var testInstance = new Session(9, 9,
                9, upgrades, new User("aLongerName", "SuperSecretPassword2", 1),
                2, 0, 1, 1 , 2);

        testInstance.updateLevelInfo();
        var actual = testInstance.getLevelRequirement();
        var expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    void resetGameTest()
    {
        var initialMoney = 52;
        var money = 10;
        var levelsBeaten = 12;
        var levelRequirement = 1080;
        var firstLevelScoreRequired = 5;

        ObservableList<UpgradeTileModel> upgrades = FXCollections.observableArrayList();

        var testInstance = new Session(9, 9,
                9, upgrades, new User("aLongerName", "SuperSecretePassword2", 1),
                money, levelsBeaten, levelRequirement, firstLevelScoreRequired, initialMoney);

        testInstance.resetGame();
        assertEquals(initialMoney, testInstance.getMoney());
        assertEquals(0, testInstance.getLevelsBeaten());
        assertEquals(firstLevelScoreRequired, testInstance.getLevelRequirement());
    }
}
