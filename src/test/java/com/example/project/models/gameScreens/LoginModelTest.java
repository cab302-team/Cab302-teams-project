package com.example.project.models.gameScreens;

import com.example.project.models.User;
import com.example.project.services.*;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoginModelTest {

    private final String Username1 = "Gustave";
    private final String Password1 = "ForThoseWhoComeAfter!33";

    @Test
    void onLoginClicked_ValidUser() {
        var mockUsersDAO = mock(UsersDAO.class);
        var mockPasswordHasher = mock(PasswordHasher.class);
        var mockSession = mock(Session.class);
        var mockSceneManager = mock(SceneManager.class);

        var loginModel = new LoginModel(mockSession, mockUsersDAO, mockPasswordHasher);
        var user = new User(Username1, Password1, 0);
        var sceneManager = new SceneManager(mockSceneManager);

        when(mockUsersDAO.doesUserExist(Username1))
                .thenReturn(true);
        when(mockUsersDAO.getUser(Username1)).thenReturn(user);
        when(mockPasswordHasher.doesPasswordMatch(Password1, user.getPassword()))
                .thenReturn(true);

        loginModel.onLoginClicked(Username1, Password1);

        verify(mockUsersDAO, times(2)).getUser(Username1);
        verify(mockSession).setUser(user);
        verify(mockSceneManager).switchScene(GameScenes.LEVEL);
    }

    @Test
    void onLoginClicked_InvalidUser() {
        var mockUsersDAO = mock(UsersDAO.class);
        var mockPasswordHasher = mock(PasswordHasher.class);
        var mockSession = mock(Session.class);

        var loginModel = new LoginModel(mockSession, mockUsersDAO, mockPasswordHasher);

        when(mockUsersDAO.doesUserExist(Username1))
                .thenReturn(false);

        loginModel.onLoginClicked(Username1, Password1);
        verify(mockUsersDAO).doesUserExist(Username1);
        assertEquals("No account found. Sign up first.", loginModel.getWelcomeTextProperty().get());
    }

    @Test
    void onLoginClicked_IllegalInput() {
        var mockUsersDAO = mock(UsersDAO.class);
        var mockPasswordHasher = mock(PasswordHasher.class);
        var mockSession = mock(Session.class);

        var loginModel = new LoginModel(mockSession, mockUsersDAO, mockPasswordHasher);

        loginModel.onLoginClicked(" ", " ");
        assertEquals("Fields cannot be empty.", loginModel.getWelcomeTextProperty().get());
    }


    @Test
    void onSignUpClicked_Valid() {
        var mockUsersDAO = mock(UsersDAO.class);
        var mockPasswordHasher = mock(PasswordHasher.class);
        var mockSession = mock(Session.class);

        var loginModel = new LoginModel(mockSession, mockUsersDAO, mockPasswordHasher);

        when(mockUsersDAO.doesUserExist(Username1))
                .thenReturn(false);

        loginModel.onSignUpClicked(Username1, Password1);
        verify(mockUsersDAO).addUser(argThat(user -> user.getUsername().equals(Username1)
                && user.getPassword().equals(Password1)));
        assertEquals("User added successfully.", loginModel.getWelcomeTextProperty().get());
    }

    @Test
    void onSignUpClicked_Invalid() {
        var mockUsersDAO = mock(UsersDAO.class);
        var mockPasswordHasher = mock(PasswordHasher.class);
        var mockSession = mock(Session.class);

        var loginModel = new LoginModel(mockSession, mockUsersDAO, mockPasswordHasher);

        when(mockUsersDAO.doesUserExist(Username1))
                .thenReturn(true);

        loginModel.onSignUpClicked(Username1, Password1);
        assertEquals("Already signed up. Click login.", loginModel.getWelcomeTextProperty().get());
    }

    @Test
    void onSignUpClicked_IllegalInput() {
        var mockUsersDAO = mock(UsersDAO.class);
        var mockPasswordHasher = mock(PasswordHasher.class);
        var mockSession = mock(Session.class);

        var loginModel = new LoginModel(mockSession, mockUsersDAO, mockPasswordHasher);

        loginModel.onSignUpClicked(" ", " ");
        assertEquals("Fields cannot be empty.", loginModel.getWelcomeTextProperty().get());
    }

}