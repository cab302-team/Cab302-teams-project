package com.example.project.services;

import com.example.project.models.User;
import com.example.project.models.tiles.UpgradeTileModel;
import com.example.project.services.shopItems.UpgradeTiles;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        var actual = testInstance.getPlayersUpgradesProperty();
        assertEquals(expected, actual);
    }

    @Test
    void updateLevelInfoTest(){
        ObservableList<UpgradeTileModel> upgrades = FXCollections.observableArrayList();
        var testInstance = new Session(9, 9,
                9, upgrades, new User("aLongerName", "SuperSecretPassword2", 1),
                2, 0, 1, 1 , 2);

        testInstance.updateLevelInfo();
        var actual = testInstance.getLevelRequirement().get();
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
        assertEquals(initialMoney, testInstance.getMoneyProperty().get());
        assertEquals(0, testInstance.getLevelsBeaten());
        assertEquals(firstLevelScoreRequired, testInstance.getLevelRequirement().get());
    }

    @Test
    void resetMoney()
    {
        var ses = new Session();
        ses.modifyMoney(10);
        ses.resetMoney();
        assertEquals(0, ses.getMoneyProperty().get());
    }

    @Test
    void setWordWindowSize()
    {
        var session = new Session();

        assertEquals(9, session.getWordWindowSize());
    }

    @Test
    void redrawWindowSize()
    {
        var session = new Session();

        assertEquals(9, session.getRedrawWindowSize());
    }

    @Test
    void handSize()
    {
        var session = new Session();

        assertEquals(9, session.getHandSize());
    }

    @Test
    void hasClaimedRewardToday_true()
    {
        var session = new Session();
        session.setLastRewardDate(LocalDate.now());

        assertTrue(session.hasClaimedRewardToday());
    }

    @Test
    void hasClaimedRewardToday_false()
    {
        var session = new Session();
        assertFalse(session.hasClaimedRewardToday());
    }

    @Test
    void save()
    {
        var mockDao = mock(UsersDAO.class);

        var ses = new Session(mockDao);

        ses.addUpgrade(UpgradeTiles.getRandomUpgradeTile());
        ses.addUpgrade(UpgradeTiles.getRandomUpgradeTile());
        ses.addUpgrade(UpgradeTiles.getRandomUpgradeTile());

        var user = new User("grace", "asdf", 12);
        ses.setUser(user);
        assertEquals(user, ses.getUser());

        ses.save();

        verify(mockDao, times(1)).saveSessionData(anyString(), anyString());
    }

    @Test
    void save_withLastRewardDate()
    {
        var mockDao = mock(UsersDAO.class);

        var ses = new Session(mockDao);

        ses.addUpgrade(UpgradeTiles.getRandomUpgradeTile());
        ses.addUpgrade(UpgradeTiles.getRandomUpgradeTile());
        ses.addUpgrade(UpgradeTiles.getRandomUpgradeTile());

        ses.setLastRewardDate(LocalDate.now());

        var user = new User("grace", "asdf", 12);
        ses.setUser(user);

        ses.save();

        verify(mockDao, times(1)).saveSessionData(anyString(), anyString());
    }

    @Test
    void load_noData()
    {
        var mockDao = mock(UsersDAO.class);
        var mockLogger = mock(Logger.class);
        var ses = new Session(mockDao, mockLogger);
        var user = new User("grace", "asdf", 12);
        ses.setUser(user);
        when(mockDao.getSessionDataJson(anyString())).thenReturn("");

        ses.load();

        // assert
        verify(mockDao, times(1)).getSessionDataJson(user.getUsername());
        verify(mockLogger, times(1)).logError("No save data found for user: grace");
    }

    @Test
    void load_nullJson()
    {
        var mockDao = mock(UsersDAO.class);
        var mockLogger = mock(Logger.class);
        var ses = new Session(mockDao, mockLogger);
        var user = new User("grace", "asdf", 12);
        ses.setUser(user);
        when(mockDao.getSessionDataJson(anyString())).thenReturn(null);

        ses.load();

        // assert
        verify(mockDao, times(1)).getSessionDataJson(user.getUsername());
        verify(mockLogger, times(1)).logError("No save data found for user: grace");
    }

    @Test
    void load_success_hasNoLastRewardDate()
    {
        // mock return correct json.
        var correctJson = """
                {
                  "money": 9.0,
                  "levelsBeaten": 1,
                  "levelRequirement": 6,
                  "currentPlays": 4,
                  "currentRedraws": 4,
                  "username": "grace4",
                  "upgradeNames": [
                    "Friendship Bracelet",
                    "Compact Mirror",
                    "Loaded Dice"
                  ]
                }""";

        var mockDao = mock(UsersDAO.class);
        var mockLogger = mock(Logger.class);
        var ses = new Session(mockDao, mockLogger);
        var user = new User("grace", "asdf", 12);
        ses.setUser(user);
        when(mockDao.getSessionDataJson("grace")).thenReturn(correctJson);

        ses.load();

        // assert
        verify(mockDao, times(1)).getSessionDataJson(user.getUsername());
        verify(mockLogger, times(1)).logMessage("Successfully loaded session for user: grace");
    }

    @Test
    void load_success_hasLastRewardDate()
    {
        // mock return correct json.
        var correctJson = """
                {
                  "money": 9.0,
                  "levelsBeaten": 1,
                  "levelRequirement": 6,
                  "currentPlays": 4,
                  "currentRedraws": 4,
                  "lastRewardDate": 2025-10-23,
                  "username": "grace4",
                  "upgradeNames": [
                    "Friendship Bracelet",
                    "Compact Mirror",
                    "Loaded Dice"
                  ]
                }""";

        var mockDao = mock(UsersDAO.class);
        var mockLogger = mock(Logger.class);
        var ses = new Session(mockDao, mockLogger);
        var user = new User("grace", "asdf", 12);
        ses.setUser(user);
        when(mockDao.getSessionDataJson("grace")).thenReturn(correctJson);

        ses.load();

        // assert
        verify(mockDao, times(1)).getSessionDataJson(user.getUsername());
        verify(mockLogger, times(1)).logMessage("Successfully loaded session for user: grace");
    }

    @Test
    void load_success_hasNullUpgrades()
    {
        // mock return correct json.
        var correctJson = """
                {
                  "money": 9.0,
                  "levelsBeaten": 1,
                  "levelRequirement": 6,
                  "currentPlays": 4,
                  "currentRedraws": 4,
                  "username": "grace4",
                  "upgradeNames": [
                    "NON EXISTANT UPGRADE!!!",
                    "Compact Mirror",
                    "Loaded Dice"
                  ]
                }""";

        var mockDao = mock(UsersDAO.class);
        var mockLogger = mock(Logger.class);
        var ses = new Session(mockDao, mockLogger);
        var user = new User("grace", "asdf", 12);
        ses.setUser(user);
        when(mockDao.getSessionDataJson("grace")).thenReturn(correctJson);

        ses.load();

        // assert
        verify(mockDao, times(1)).getSessionDataJson(user.getUsername());
        verify(mockLogger, times(1)).logMessage("Successfully loaded session for user: grace");
    }

    @Test
    void load_fromJsonThrowsException()
    {
        // mock data to return malformed json
        // mock return correct json.
        var badJson = "{adfaasdf;";

        var mockDao = mock(UsersDAO.class);
        var mockLogger = mock(Logger.class);
        var ses = new Session(mockDao, mockLogger);
        var user = new User("grace", "asdf", 12);
        ses.setUser(user);
        when(mockDao.getSessionDataJson("grace")).thenReturn(badJson);

        ses.load();

        // assert
        verify(mockDao, times(1)).getSessionDataJson(user.getUsername());
        verify(mockLogger, times(1)).logError("Failed to load session data: com.google.gson.stream.MalformedJsonException: Expected ':' at line 1 column 11 path $.adfaasdferrorcom.google.gson.stream.MalformedJsonException: Expected ':' at line 1 column 11 path $.adfaasdf");
    }
}
