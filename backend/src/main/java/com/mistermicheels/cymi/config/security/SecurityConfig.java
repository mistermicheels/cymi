package com.mistermicheels.cymi.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(
            new AntPathRequestMatcher("/authentication/**"));

    private final AuthenticationProvider provider;
    private final SecurityProperties securityProperties;
    private final HandlerExceptionResolver resolver;

    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider,
            SecurityProperties securityProperties, HandlerExceptionResolver resolver) {
        super();
        this.provider = authenticationProvider;
        this.securityProperties = securityProperties;
        this.resolver = resolver;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.provider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // disable Spring Security defaults more suitable for MVC
        http.csrf().disable();
        http.formLogin().disable();
        http.httpBasic().disable();
        http.logout().disable();

        // disable Spring Security session management
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // require authentication only for protected URLs
        http.authorizeRequests().requestMatchers(PROTECTED_URLS).authenticated();
        http.authorizeRequests().anyRequest().permitAll();

        // use token authentication and pass exceptions to FilterChainExceptionHandler
        http.addFilterBefore(this.tokenAuthenticationFilter(), AnonymousAuthenticationFilter.class);
        http.addFilterBefore(this.chainExceptionHandler(), TokenAuthenticationFilter.class);
    }

    // we do not declare the below filters as beans
    // this prevents them from automatically being registered in the container
    // an alternative would be the use of FilterRegistrationBean

    TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS,
                this.securityProperties);

        filter.setAuthenticationManager(this.authenticationManager());
        return filter;
    }

    FilterChainExceptionHandler chainExceptionHandler() {
        FilterChainExceptionHandler exceptionHandler = new FilterChainExceptionHandler(
                this.resolver);

        return exceptionHandler;
    }

}
