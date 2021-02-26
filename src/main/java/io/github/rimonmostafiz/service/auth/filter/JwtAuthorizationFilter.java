package io.github.rimonmostafiz.service.auth.filter;

import io.github.rimonmostafiz.component.exception.InvalidJwtAuthenticationException;
import io.github.rimonmostafiz.service.auth.jwt.JwtService;
import io.github.rimonmostafiz.utils.ResponseUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
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
import static io.github.rimonmostafiz.service.auth.SecurityConstants.AUTHORIZATION_HEADER;
import static io.github.rimonmostafiz.utils.HttpUtils.determineTargetUrl;
import static io.github.rimonmostafiz.utils.ResponseUtils.INVALID_TOKEN;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends GenericFilterBean {

    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String randomCode = UUID.randomUUID().toString().substring(0, 6);
        MDC.put("random_code", randomCode);
        try {
            String url = determineTargetUrl((HttpServletRequest) request);
            log.debug("inside JwtAuthorizationFilter with url[{}], resolve access token from request", url);
            String accessToken = jwtService.resolveToken((HttpServletRequest) request);
            String tokenType = jwtService.getTokenType(accessToken);
            if (accessToken != null && !tokenType.equalsIgnoreCase(ACCESS_TOKEN)) {
                throw new AuthenticationServiceException(INVALID_TOKEN);
            }
            log.debug("accessToken: {}, tokenType: {}", accessToken, tokenType);
            log.debug("resolve claims from request");
            Claims claims = jwtService.resolveClaims((HttpServletRequest) request);
            if (accessToken != null && claims != null
                    && jwtService.validateClaims(claims) /*&& jwtHelper.tokenNotBlackListed(accessToken)*/) {
                MDC.put("logged_user", jwtService.getUsername(claims));
                log.debug("calling getAuthentication with claims, httpRequest and token");
                var authentication = jwtService.getAuthentication(claims, (HttpServletRequest) request, accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (InvalidJwtAuthenticationException | AuthenticationServiceException ex) {
            var errorRestResponse = ResponseUtils.buildErrorRestResponse(
                    HttpStatus.UNAUTHORIZED, AUTHORIZATION_HEADER, ex.getMessage()
            );
            ResponseUtils.createCustomResponse((HttpServletResponse) response, errorRestResponse);
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.warn("Global Exception caught", e);
            throw e;
        } finally {
            MDC.remove("logged_user");
            MDC.remove("random_code");
        }
    }
}
