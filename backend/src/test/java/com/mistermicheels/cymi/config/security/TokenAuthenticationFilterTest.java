package com.mistermicheels.cymi.config.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TokenAuthenticationFilterTest {

    @Mock
    RequestMatcher requiresAuthenticationRequestMatcherMock;

    @Mock
    SecurityProperties securityPropertiesMock;

    @Mock
    AuthenticationManager authenticationManagerMock;

    private final String sessionTokenCookieName = "sessionTokenCookieName";
    private final String csrfTokenHeaderName = "csrfTokenHeaderName";

    private final MockHttpServletResponse response = new MockHttpServletResponse();

    private TokenAuthenticationFilter filter;

    @BeforeEach
    public void beforeEach() {
        when(this.securityPropertiesMock.getSessionTokenCookieName())
                .thenReturn(this.sessionTokenCookieName);
                
        when(this.securityPropertiesMock.getCsrfTokenHeaderName())
                .thenReturn(this.csrfTokenHeaderName);

        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(
                this.requiresAuthenticationRequestMatcherMock, this.securityPropertiesMock);

        this.filter = spy(filter);

        when(this.filter.getAuthenticationManager()).thenReturn(this.authenticationManagerMock);
    }

    @Test
    public void FilterFailsForNoCookies() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThrows(CustomAuthenticationException.class,
                () -> this.filter.attemptAuthentication(request, this.response));
    }

    @Test
    public void FilterFailsForMissingSessionTokenCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie otherCookie = new Cookie("someName", "someValue");
        request.setCookies(otherCookie);

        assertThrows(CustomAuthenticationException.class,
                () -> this.filter.attemptAuthentication(request, this.response));
    }

    @Test
    public void FilterFailsForMisingCsrfTokenHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie otherCookie = new Cookie(this.sessionTokenCookieName, "someValue");
        request.setCookies(otherCookie);

        assertThrows(CustomAuthenticationException.class,
                () -> this.filter.attemptAuthentication(request, this.response));
    }

    @Test
    public void FilterSucceedsIfCookieAndHeaderPresent()
            throws AuthenticationException, IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie otherCookie = new Cookie(this.sessionTokenCookieName, "someValue");
        request.setCookies(otherCookie);
        request.addHeader(this.csrfTokenHeaderName, "someValue");

        this.filter.attemptAuthentication(request, this.response);
    }

}
