package com.mistermicheels.cymi.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.mistermicheels.cymi.component.user.SessionDataIncoming;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private SecurityProperties securityProperties;

    protected TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
            SecurityProperties securityProperties) {
        super(requiresAuthenticationRequestMatcher);
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String sessionToken = this.getCookieValue(request,
                this.securityProperties.getSessionTokenCookieName());

        SessionDataIncoming sessionData;

        if (this.isStateChangingRequest(request)) {
            String csrfToken = this.getHeaderValue(request,
                    this.securityProperties.getCsrfTokenHeaderName());

            sessionData = SessionDataIncoming.forStateChangingRequest(sessionToken, csrfToken);
        } else {
            sessionData = SessionDataIncoming.forNonStateChangingRequest(sessionToken);
        }

        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(sessionData,
                sessionData);

        return this.getAuthenticationManager().authenticate(requestAuthentication);
    }

    /*
     * override getAuthenticationManager in order to expose it to test classes
     */
    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return super.getAuthenticationManager();
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        String missingCookieMessage = "Missing " + name + " cookie";
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new CustomAuthenticationException(missingCookieMessage);
        }

        Stream<Cookie> cookiesStream = Arrays.stream(cookies);

        Cookie matchingCookie = cookiesStream.filter(cookie -> cookie.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new CustomAuthenticationException(missingCookieMessage));

        return matchingCookie.getValue();
    }

    private boolean isStateChangingRequest(HttpServletRequest request) {
        return !request.getMethod().equalsIgnoreCase(HttpMethod.GET.toString())
                && !request.getMethod().equalsIgnoreCase(HttpMethod.HEAD.toString())
                && !request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString());
    }

    private String getHeaderValue(HttpServletRequest request, String name) {
        Optional<String> headerValue = Optional.ofNullable(request.getHeader(name));

        return headerValue.orElseThrow(
                () -> new CustomAuthenticationException("Missing " + name + " header"));
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // move on to the next filter in the chain
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        // make sure the AuthenticationException is actually thrown from this filter
        throw failed;
    }

}
