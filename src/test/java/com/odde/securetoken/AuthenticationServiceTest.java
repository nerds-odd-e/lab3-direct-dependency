package com.odde.securetoken;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    private static final String PASSWORD = "91";
    private static final String TOKEN = "607934";
    ProfileDao stubProfileDao = mock(ProfileDao.class);
    RsaTokenDao stubRsaTokenDao = mock(RsaTokenDao.class);
    AuthenticationService target = new AuthenticationService(stubProfileDao, stubRsaTokenDao);

    @Test
    public void is_valid_test() {
        givenAccountPassword(PASSWORD);
        givenRsaToken(TOKEN);

        boolean actual = target.isValid("joey", PASSWORD + TOKEN);

        assertTrue(actual);
    }

    private void givenRsaToken(String token) {
        when(stubRsaTokenDao.getRandom(anyString())).thenReturn(token);
    }

    private void givenAccountPassword(String password) {
        when(stubProfileDao.getPassword(anyString())).thenReturn(password);
    }

}
