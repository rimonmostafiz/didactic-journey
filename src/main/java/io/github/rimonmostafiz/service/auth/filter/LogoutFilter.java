package io.github.rimonmostafiz.service.auth.filter;


import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.service.auth.NoOpAuthManager;
import io.github.rimonmostafiz.service.auth.jwt.JwtService;
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

import static io.github.rimonmostafiz.utils.HttpUtils.determineTargetUrl;
import static io.github.rimonmostafiz.utils.UrlHelper.AUTH_LOGOUT;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Component
public class LogoutFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtService jwtService;

    public LogoutFilter(JwtService jwtService) {
        super(AUTH_LOGOUT);
        setAuthenticationManager(new NoOpAuthManager());
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException {
        log.debug("inside logout attempt authentication");
        log.debug("calling jwtHelper to resolve refresh token from request");
        String token = jwtService.resolveToken(request);
        log.debug("token : {}", token);
        log.debug("calling jwtHelper to resolve claims from request");
        Claims claims = jwtService.resolveClaims(request);
        if (token != null && jwtService.validateClaims(claims) /*&& jwtHelper.tokenNotBlackListed(token)*/) {
            Authentication authentication = jwtService.getAuthentication(claims, request, token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        }
        throw new AuthenticationServiceException("INVALID_TOKEN");
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
                                              AuthenticationException failed) throws IOException, ServletException {
        log.debug("authentication failed for target URL: {}", determineTargetUrl(request));
        log.debug("failed msg : {}", failed.getMessage());
        RestResponse<?> restResponse = failed.getMessage().equals(ResponseUtils.EXPIRED_TOKEN)
                ? ResponseUtils.buildSuccessRestResponse(HttpStatus.OK, ResponseUtils.EXPIRED_TOKEN)
                : ResponseUtils.buildSuccessRestResponse(HttpStatus.OK, ResponseUtils.INVALID_TOKEN);
        ResponseUtils.createCustomResponse(response, restResponse);
    }
}
