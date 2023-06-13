package com.example.library.model.validation;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PasswordConstraintValidatorTest {
    private static PasswordConstraintValidator underTest;

    private final static ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    private final static ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

    @BeforeAll
    static void initialSetUp(){
        underTest = new PasswordConstraintValidator();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    @BeforeEach
    void setUp(){
        clearInvocations(context);
    }

    @Test
    @DisplayName("Should return false with proper message because of password length")
    void shouldReturnFalseWhenPasswordIncorrectLength(){
        String tooLongPassword = "This_is_really_long_password_and_it_shouldn't_be_accepted_##123";
        assertFalse(underTest.isValid(tooLongPassword, context));
        verify(context).buildConstraintViolationWithTemplate(eq("Password must be no more than 50 characters in length."));

        clearInvocations(context);
        String tooShortPassword = "1#Ab";
        assertFalse(underTest.isValid(tooShortPassword, context));
        verify(context).buildConstraintViolationWithTemplate(eq("Password must be 8 or more characters in length."));
    }

    @Test
    @DisplayName("Should return false with proper communicate if password without any uppercase letter")
    void shouldReturnFalseWhenPasswordWithoutAnyUppercaseLetter(){
        String incorrectPassword = "without_any_great_letter11##";

        assertFalse(underTest.isValid(incorrectPassword, context));
        verify(context).buildConstraintViolationWithTemplate(eq("Password must contain 1 or more uppercase characters."));
    }

    @Test
    @DisplayName("Should return false with proper communicate if password without any digit")
    void shouldReturnFalseWhenPasswordWithoutAnyDigit(){
        String incorrectPassword = "Without_Any_Digit##";

        assertFalse(underTest.isValid(incorrectPassword, context));
        verify(context).buildConstraintViolationWithTemplate(eq("Password must contain 1 or more digit characters."));
    }

    @Test
    @DisplayName("Should return false with proper communicate if password without any special character")
    void shouldReturnFalseWhenPasswordWithoutAnySpecialCharacter(){
        String incorrectPassword = "WithoutAnySpecialCharacter1";

        assertFalse(underTest.isValid(incorrectPassword, context));
        verify(context).buildConstraintViolationWithTemplate(eq("Password must contain 1 or more special characters."));
    }

    @Test
    @DisplayName("Should return false with proper communicate if password without any lowercase character")
    void shouldReturnFalseWhenPasswordWithoutAnyLowercaseCharacter(){
        String incorrectPassword = "WITHOUT_ANY_LOWERCASE_CHARACTER_11##";

        assertFalse(underTest.isValid(incorrectPassword, context));
        verify(context).buildConstraintViolationWithTemplate(eq("Password must contain 1 or more lowercase characters."));
    }

    @Test
    @DisplayName("Should return true if valid password")
    void shouldReturnTrueIfValidPassword() {
        String correctPassword = "ThisPasswordIsCorrect123##";
        assertTrue(underTest.isValid(correctPassword, context));
    }
}