package com.example.project.controllers.gameScreens;

import com.example.project.GameScenes;
import com.example.project.Logger;
import com.example.project.models.sqlite.dAOs.UsersDAO;
import com.example.project.SceneManager;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.mockito.Mockito.*;
import com.example.project.initJavaToolkit;

/**
 * Login controller tests.
 */
public class LoginControllerTests
{
    ByteArrayOutputStream logBytes = new ByteArrayOutputStream();
    ByteArrayOutputStream errorLogBytes = new ByteArrayOutputStream();

    private final Logger loggerToInject = new Logger(errorLogBytes, logBytes);

    @BeforeAll
    static void beforeAll()
    {
        initJavaToolkit.initJavaFX();
    }

    @BeforeEach
    void beforeEach(){
        logBytes.reset();
        errorLogBytes.reset();
    }

    @Test
    void onSceneChangedToThis()
    {
        UsersDAO UsersDAOMock = mock(UsersDAO.class);
        var loginC = new LoginController(loggerToInject, UsersDAOMock, new TextField(), new Label());
        loginC.onSceneChangedToThis();
        Assertions.assertEquals(String.format("Login page loaded.%n"), logBytes.toString());
    }

    @Test
    void onLoginButtonClick_UserExists()
    {
        // Mock the DAO
        UsersDAO mockUsersDAO = mock(UsersDAO.class);
        when(mockUsersDAO.doesUserExist("alice")).thenReturn(true);

        var usernameTextField = new TextField();
        usernameTextField.setText("alice");

        Label welcomeText = new Label();
        var loginC = new LoginController(loggerToInject, mockUsersDAO, usernameTextField, welcomeText);

        // Mock scene manager dont want to call any java fx methods.
        SceneManager.injectForTests(mock(SceneManager.class), null, null, null);
        var mockSceneManager = SceneManager.getInstance();

        // call test method.
        loginC.onLoginButtonClick();

        // Assert
        Assertions.assertEquals("logged in!", welcomeText.getText());
        verify(mockSceneManager).switchScene(GameScenes.LEVEL);
    }

    @Test
    void onLoginButtonClick_UserDoesNotExist(){
        // Mock the DAO
        UsersDAO mockUsersDAO = mock(UsersDAO.class);
        when(mockUsersDAO.doesUserExist("alice")).thenReturn(false);

        var usernameTextField = new TextField();
        usernameTextField.setText("alice");

        Label welcomeText = new Label();
        var loginC = new LoginController(loggerToInject, mockUsersDAO, usernameTextField, welcomeText);

        // Mock scene manager dont want to call any java fx methods.
        SceneManager.injectForTests(mock(SceneManager.class), null, null, null);
        var mockSceneManager = SceneManager.getInstance();

        // call test method.
        loginC.onLoginButtonClick();

        // Assert
        Assertions.assertEquals("not a user you need to sign up.", welcomeText.getText());
    }
}
