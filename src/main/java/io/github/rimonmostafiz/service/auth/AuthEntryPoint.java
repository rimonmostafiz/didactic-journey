package io.github.rimonmostafiz.service.auth;

import io.github.rimonmostafiz.model.common.ErrorDetails;
import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.utils.HttpUtils;
import io.github.rimonmostafiz.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException, ServletException {
        log.debug("authentication failed for target URL: {}", HttpUtils.determineTargetUrl(request));
        log.debug("Inside AuthEntryPoint commence, creating custom error response");
        ErrorDetails errorDetails = new ErrorDetails(SecurityConstants.AUTHORIZATION_HEADER, ex.getMessage());
        RestResponse<?> error = new RestResponse<>(HttpStatus.UNAUTHORIZED, errorDetails);
        ResponseUtils.createCustomResponse(response, error);
    }
}
