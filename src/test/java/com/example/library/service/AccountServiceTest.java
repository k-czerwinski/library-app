package com.example.library.service;

import com.example.library.exceptions.AlreadyExistException;
import com.example.library.exceptions.PasswordsNotMatchingException;
import com.example.library.model.User;
import com.example.library.model.UserDTO;
import com.example.library.model.enums.UserRole;
import com.example.library.repository.UserRepository;
import jakarta.validation.constraints.Email;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.rmi.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    AccountService accountService;

    @BeforeEach
    void setUp(){
        accountService = new AccountService(userRepository,passwordEncoder);
    }

    @Test
    @DisplayName("saveUser should throw AlreadyExistException if user already exist")
    void saveUserShouldThrowException() {
        String email = "johnsmith@gmail.com";
        UserDTO user = new UserDTO("John","Smith",email,"123Password##","123Password##");
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(AlreadyExistException.class, ()->accountService.saveUser(user));
    }

    @Test
    @DisplayName("saveUser should throw PasswordNotMatchingException when password not matching")
    void saveUserShouldThrowExceptionWhenPasswordNodMatching(){
        String email = "johnsmith@gmail.com";
        String password = "123Password##";
        String notMatchingPassword = "123NotMatchingPassword##";
        UserDTO user = new UserDTO("John","Smith",email,password,notMatchingPassword);
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThrows(PasswordsNotMatchingException.class,() -> accountService.saveUser(user));
    }

    @Test
    @DisplayName("saveUser should succeed")
    void saveUserShouldSucceed(){
        String email = "johnsmith@gmail.com";
        UserDTO user = new UserDTO("John","Smith",email,"123matchingPassword##","123matchingPassword##");
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> accountService.saveUser(user));
        Mockito.verify(userRepository, Mockito.times(1)).save(notNull(User.class));
    }
}