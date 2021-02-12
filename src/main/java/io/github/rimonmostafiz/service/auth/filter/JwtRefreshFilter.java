package io.github.rimonmostafiz.service.auth.filter;

import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.service.auth.NoOpAuthManager;
import io.github.rimonmostafiz.service.auth.jwt.JwtHelper;
import io.github.rimonmostafiz.utils.ResponseUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.github.rimonmostafiz.service.auth.SecurityConstants.REFRESH_TOKEN;
import static io.github.rimonmostafiz.service.auth.SecurityConstants.TOKEN_HEADER;
import static io.github.rimonmostafiz.utils.HttpUtils.determineTargetUrl;
import static io.github.rimonmostafiz.utils.ResponseUtils.INVALID_TOKEN;
import static io.github.rimonmostafiz.utils.UrlHelper.AUTH_REFRESH;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Component
public class JwtRefreshFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtHelper jwtHelper;

    public JwtRefreshFilter(JwtHelper jwtHelper) {
        super(AUTH_REFRESH);
        setAuthenticationManager(new NoOpAuthManager());
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        log.debug("Calling jwtHelper to resolve token from request");
        String refreshToken = jwtHelper.resolveToken(request);
        String tokenType = jwtHelper.getTokenType(refreshToken);
        if (refreshToken != null && !tokenType.equalsIgnoreCase(REFRESH_TOKEN)) {
            throw new AuthenticationServiceException(INVALID_TOKEN);
        }
        log.debug("refreshToken: {}, tokenType: {}", refreshToken, tokenType);
        log.debug("Calling jwtHelper to resolve claims from request");
        Claims claims = jwtHelper.resolveClaims(request);
        if (refreshToken != null && jwtHelper.validateClaims(claims) /*&& jwtHelper.tokenNotBlackListed(refreshToken)*/) {
            log.debug("Calling getAuthentication with claims, httpRequest and token");
            Authentication authentication = jwtHelper.getAuthentication(claims, request, refreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        }
        throw new AuthenticationServiceException(INVALID_TOKEN);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String url = determineTargetUrl(request);
        log.debug("permission ok; path: {}", url);
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException ex) throws IOException, ServletException {
        log.debug("authentication failed for target URL: {}", determineTargetUrl(request));
        RestResponse<?> error = ResponseUtils.buildErrorRestResponse(HttpStatus.UNAUTHORIZED, TOKEN_HEADER, ex.getMessage());
        ResponseUtils.createCustomResponse(response, error);
    }
}
