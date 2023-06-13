package com.example.library.security;

import com.example.library.model.User;
import com.example.library.model.enums.UserRole;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class CustomerDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomerDetailsService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerDetailsService(userRepository);
    }

    @Test
    @DisplayName("Should throw exception if username not found")
    void shouldThrowExceptionIfUsernameNotFound() {
        String email = "billgates@microsoft.com";
        User userModel = new User(UserRole.CUSTOMER,"Bill","Gates",email,"123Password##");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userModel));

        UserDetails userDetails = assertDoesNotThrow(()->underTest.loadUserByUsername(email));
        assertEquals(userModel.getEmail(),userDetails.getUsername());
        assertEquals(userModel.getPassword(),userDetails.getPassword());
        assertEquals("[ROLE_CUSTOMER]",userDetails.getAuthorities().toString());
    }

    @Test
    @DisplayName("Should succeed if user was found")
    void shouldSucceedIfUserWasFound(){
        String email = "simple@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class,() -> underTest.loadUserByUsername(email));
        assertEquals("Check your password and email and try again",exception.getMessage());
    }
}