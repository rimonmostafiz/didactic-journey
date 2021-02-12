package io.github.rimonmostafiz.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
public class NoOpAuthManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("No Operation Authentication manager is invoked");
        return authentication;
    }

}
