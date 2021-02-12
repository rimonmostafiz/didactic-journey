package io.github.rimonmostafiz.api;

import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.request.AuthRequest;
import io.github.rimonmostafiz.model.response.AuthResponse;
import io.github.rimonmostafiz.service.auth.jwt.JwtHelper;
import io.github.rimonmostafiz.service.user.UserService;
import io.github.rimonmostafiz.utils.ResponseUtils;
import io.github.rimonmostafiz.utils.SessionKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static io.github.rimonmostafiz.service.auth.SecurityConstants.ACCESS_TOKEN;
import static io.github.rimonmostafiz.service.auth.SecurityConstants.REFRESH_TOKEN;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtHelper jwtHelper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

//    @Qualifier("redisTemplate")
//    private final RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @Operation(summary = "Login", description = "Login Using Username And Password")
    public ResponseEntity<RestResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest, Locale locale) {
        log.debug("Inside /auth/login with with username {}", authRequest.getUsername());

        authRequest.setUsername(authRequest.getUsername().trim().toLowerCase());
        String username = authRequest.getUsername().trim().toLowerCase();
        String password = authRequest.getPassword();

        log.debug("Authentication Start");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        log.debug("Authentication Complete");

        User user = userService.getUserByUsername(username);
        userService.ifDisableThrowException(user);
        List<String> roles = userService.getUserRoles(user);

        Date tokenCreateTime = new Date();

        String accessToken = jwtHelper.createToken(username, roles, ACCESS_TOKEN, tokenCreateTime);
        String refreshToken = jwtHelper.createToken(username, roles, REFRESH_TOKEN, tokenCreateTime);

        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, new AuthResponse(username, accessToken, refreshToken));
    }

    @PostMapping(path = "/refresh")
    @Operation(summary = "Refresh", security = @SecurityRequirement(name = "bearer-auth"),
            description = "Need to Provide Refresh Token as Bearer Token In Authorization Header")
    public ResponseEntity<RestResponse<AuthResponse>> refresh(HttpServletRequest request) {
        log.debug("Inside /auth/refresh of AuthController");

        UserDetails userDetails = (UserDetails) request.getSession().getAttribute(SessionKey.USER_DETAILS);
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);
        userService.ifDisableThrowException(user);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>(AuthorityUtils.authorityListToSet(authorities));

        Date tokenCreateTime = new Date();
        String accessToken = jwtHelper.createToken(username, roles, ACCESS_TOKEN, tokenCreateTime);

        AuthResponse authResponse = new AuthResponse(username, accessToken);

        return ResponseUtils.buildSuccessResponse(HttpStatus.CREATED, authResponse);
    }

//    @RequestMapping(path = "/logout", method = RequestMethod.POST)
//    @Operation(summary = "Logout",
//            security = @SecurityRequirement(name = "bearer-auth"),
//            description = "Need to Provide Bearer Token In Authorization Header")
//    public ResponseEntity<RestResponse<String>> logout(HttpServletRequest request, Locale locale) {
//        log.debug("Inside /auth/logout for logout of AuthController");
//        UserDetails userDetails = (UserDetails) request.getSession().getAttribute(SessionKey.USER_DETAILS);
//        String tokenType = (String) request.getSession().getAttribute(SessionKey.TYPE_OF_TOKEN);
//        log.debug("username : {} and tokenType from session : {}", userDetails.getUsername(), tokenType);
//
//        RestResponse<String> restApiResponse;
//        boolean inserted = jwtHelper.insertTokenToBlackList(request, tokenType);
//
//        if (inserted) {
//            log.debug("Logout Operation Success, Returning Response");
//            restApiResponse = ResponseUtils.buildSuccessRestResponse(HttpStatus.OK, ResponseUtils.LOGOUT_SUCCESS);
//        } else {
//            log.debug("Logout Operation Failed, Returning Response");
//            restApiResponse = ResponseUtils.buildErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR,
//                    messageSource.getMessage("field.logout", null, locale), ResponseUtils.LOGOUT_FAILED);
//        }
//
//        return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
//    }
}