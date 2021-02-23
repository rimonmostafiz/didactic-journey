package io.github.rimonmostafiz.config;

import io.github.rimonmostafiz.service.auth.AuthEntryPoint;
import io.github.rimonmostafiz.service.auth.AuthFailureHandler;
import io.github.rimonmostafiz.service.auth.filter.JwtAuthorizationFilter;
import io.github.rimonmostafiz.service.auth.filter.JwtRefreshFilter;
import io.github.rimonmostafiz.service.user.TaskManagerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static io.github.rimonmostafiz.utils.UrlHelper.*;

/**
 * @author Rimon Mostafiz
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRefreshFilter jwtRefreshFilter;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final TaskManagerUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //@formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
                .csrf().disable()
                .cors()
            .and()
                .authorizeRequests()
                .antMatchers(AUTH_LOGIN).permitAll()
                .anyRequest().authenticated()
            .and()
                .antMatcher(AUTH_REFRESH)
                .addFilterBefore(jwtRefreshFilter, UsernamePasswordAuthenticationFilter.class)
                .antMatcher(ALL)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new AuthEntryPoint())
                .accessDeniedHandler(new AuthFailureHandler())
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers( all(API_DOCS),
                                    all(V2_API_DOCS),
                                    all(CONFIGURATION),
                                    CONFIGURATION_UI,
                                    all(WEB_JARS),
                                    all(SWAGGER_RESOURCES),
                                    SWAGGER_UI_HTML,
                                    SWAGGER_UI
        );
    }
    // @formatter:on

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Remove the ROLE_ prefix
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
