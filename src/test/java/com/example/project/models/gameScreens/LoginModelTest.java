package com.example.project.models.gameScreens;

import com.example.project.models.User;
import com.example.project.services.*;
import com.example.project.services.sqlite.dAOs.UsersDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoginModelTest {

    @Mock private UsersDAO mockUsersDAO;
    @Mock private PasswordHasher mockPasswordHasher;
    @Mock private Session mockSession;
    @Mock private SceneManager mockSceneManager;
    @InjectMocks private LoginModel loginModel;

    private final String Username1 = "Gustave";
    private final String Password1 = "ForThoseWhoComeAfter!33";


    @Test
    void getWelcomeTextProperty() {

    }

    @Test
    void onLoginClicked_ValidUser() {

        User mockUser = new User(Username1, Password1, 0);


        when(mockUsersDAO.doesUserExist(Username1))
                .thenReturn(true);
        when(mockUsersDAO.getUser(Username1)).thenReturn(mockUser);
        when(mockPasswordHasher.checkPassword(Password1, mockUser.getPassword()))
                .thenReturn(true);

        loginModel.onLoginClicked(Username1, Password1);

        verify(mockUsersDAO).getUser(Username1);
        verify(mockSession).setUser(mockUser);
        verify(mockSceneManager).switchScene(GameScenes.LEVEL);
    }


    @Test
    void onSignUpClicked() {
    }


}