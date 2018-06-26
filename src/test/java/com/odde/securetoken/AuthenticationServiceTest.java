package com.odde.securetoken;

import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    private ProfileDao stubProfileDao = mock(ProfileDao.class);
    private RsaTokenDao stubRsaTokenDao = mock(RsaTokenDao.class);
    InvalidLogger mockInvalidLogger = mock(InvalidLogger.class);
    AuthenticationService target = new AuthenticationService(stubProfileDao, stubRsaTokenDao, mockInvalidLogger);

    @Test
    public void is_valid_test() {
        givenAccountWithPassword("joey", "91");
        givenToken("000000");

        assertTrue(target.isValid("joey", "91000000"));
    }

    @Test
    public void should_not_login_anything() {
        givenAccountWithPassword("joey", "91");
        givenToken("000000");

        target.isValid("joey", "91000000");

        verify(mockInvalidLogger, never()).save(anyString());
    }

    @Test
    public void not_valid() {
        givenAccountWithPassword("joey", "91");
        givenToken("111111");

        assertFalse(target.isValid("joey", "91000000"));
    }

    @Test
    public void log_invalid_login() {
        givenAccountWithPassword("joey", "91");
        givenToken("111111");

        target.isValid("joey", "invalidPassword");

        verify(mockInvalidLogger).save(
                should(str -> assertThat(str).contains("joey", "login failed")));
    }

    public static <T> T should(Consumer<T> assertion) {
        return argThat(argument -> {
            assertion.accept(argument);
            return true;
        });
    }

    private void givenToken(String token) {
        when(stubRsaTokenDao.getRandom(anyString())).thenReturn(token);
    }

    private void givenAccountWithPassword(String account, String password) {
        when(stubProfileDao.getPassword(account)).thenReturn(password);
    }

}
