package io.github.rimonmostafiz.service.auth.filter;

import io.github.rimonmostafiz.component.exception.InvalidJwtAuthenticationException;
import io.github.rimonmostafiz.service.auth.jwt.JwtHelper;
import io.github.rimonmostafiz.utils.ResponseUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static io.github.rimonmostafiz.service.auth.SecurityConstants.ACCESS_TOKEN;
import static io.github.rimonmostafiz.service.auth.SecurityConstants.TOKEN_HEADER;
import static io.github.rimonmostafiz.utils.ResponseUtils.INVALID_TOKEN;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
public class JwtAuthorizationFilter extends GenericFilterBean {

    private final JwtHelper jwtHelper;

    public JwtAuthorizationFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException, InvalidJwtAuthenticationException {
        String randomCode = UUID.randomUUID().toString().substring(0, 6);
        MDC.put("random_code", randomCode);
        try {
            log.debug("Calling jwtHelper to resolve access token from request");
            String accessToken = jwtHelper.resolveToken((HttpServletRequest) request);
            String tokenType = jwtHelper.getTokenType(accessToken);
            if (accessToken != null && !tokenType.equalsIgnoreCase(ACCESS_TOKEN)) {
                throw new AuthenticationServiceException(INVALID_TOKEN);
            }
            log.debug("accessToken: {}, tokenType: {}", accessToken, tokenType);
            log.debug("Calling jwtHelper to resolve claims from request");
            Claims claims = jwtHelper.resolveClaims((HttpServletRequest) request);
            if (accessToken != null && claims != null
                    && jwtHelper.validateClaims(claims) /*&& jwtHelper.tokenNotBlackListed(accessToken)*/) {
                MDC.put("logged_user", jwtHelper.getUsername(claims));
                log.debug("Calling getAuthentication with claims, httpRequest and token");
                Authentication authentication = jwtHelper.getAuthentication(claims, (HttpServletRequest) request, accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (InvalidJwtAuthenticationException | AuthenticationServiceException ex) {
            ResponseUtils.createCustomResponse((HttpServletResponse) response,
                    ResponseUtils.buildErrorRestResponse(HttpStatus.UNAUTHORIZED, TOKEN_HEADER, ex.getMessage()));
        }
        try {
            chain.doFilter(request, response);
        }
        catch (Exception e) {
            log.warn("Global Exception caught", e);
            throw e;
        } finally {
            MDC.remove("logged_user");
            MDC.remove("random_code");
        }
    }
}
