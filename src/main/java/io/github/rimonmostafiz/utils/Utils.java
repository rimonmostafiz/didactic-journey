package io.github.rimonmostafiz.utils;

import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rimon Mostafiz
 */
public class Utils {
    public static UserDetails extractUserDetails(HttpServletRequest request) {
        return (UserDetails) request.getSession().getAttribute(SessionKey.USER_DETAILS);
    }

    public static String getUserNameFromRequest(HttpServletRequest request) {
        return extractUserDetails(request).getUsername();
    }
}
