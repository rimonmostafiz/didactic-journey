package io.github.rimonmostafiz.config;

import io.github.rimonmostafiz.service.auth.AuthEntryPoint;
import io.github.rimonmostafiz.service.auth.AuthFailureHandler;
import io.github.rimonmostafiz.service.auth.filter.JwtRefreshFilter;
import io.github.rimonmostafiz.service.auth.filter.LogoutFilter;
import io.github.rimonmostafiz.service.auth.jwt.JwtConfigurer;
import io.github.rimonmostafiz.service.auth.jwt.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static io.github.rimonmostafiz.utils.UrlHelper.AUTH_LOGIN;
import static io.github.rimonmostafiz.utils.UrlHelper.AUTH_REFRESH;

/**
 * @author Rimon Mostafiz
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtHelper jwtHelper;

    private final LogoutFilter logoutFilter;

    private final JwtRefreshFilter jwtRefreshFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //@formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

            .httpBasic()
                .disable()
            .cors()
            .and()
            .csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_LOGIN).permitAll()
            .and()
                .antMatcher(AUTH_REFRESH)
                .addFilterBefore(jwtRefreshFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated()
//            .and()
//                .antMatcher(AUTH_LOGOUT)
//                .addFilterBefore(logoutFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .anyRequest().authenticated()
            .and()
                .apply(new JwtConfigurer(jwtHelper))
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthEntryPoint())
                .accessDeniedHandler(new AuthFailureHandler())
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    // @formatter:on

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api-docs/**",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/swagger-ui/**");

    }
}
