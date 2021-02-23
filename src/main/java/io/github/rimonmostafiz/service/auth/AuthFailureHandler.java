package io.github.rimonmostafiz.service.auth;

import io.github.rimonmostafiz.utils.HttpUtils;
import io.github.rimonmostafiz.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
public class AuthFailureHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {
        log.debug("authentication failed for target URL: {}", HttpUtils.determineTargetUrl(request));
        log.debug("Inside AuthFailureHandler handle, creating custom error response");
        var error
                = ResponseUtils.buildErrorRestResponse(HttpStatus.UNAUTHORIZED, SecurityConstants.AUTHORIZATION_HEADER, ex.getMessage());
        ResponseUtils.createCustomResponse(response, error);
    }
}
