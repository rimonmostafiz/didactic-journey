package io.github.rimonmostafiz.service.auth.jwt;

import io.github.rimonmostafiz.service.auth.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author Rimon Mostafiz
 */
@Component
@RequiredArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtHelper jwtHelper;

    @Override
    public void configure(HttpSecurity http) {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtHelper);
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
