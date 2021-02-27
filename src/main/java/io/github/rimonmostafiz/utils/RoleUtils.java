package io.github.rimonmostafiz.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Rimon Mostafiz
 */
public class RoleUtils {
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    public static boolean hasPrivilege(HttpServletRequest request, String role) {
        return Utils.extractUserDetails(request)
                .getAuthorities()
                .stream()
                .map(Objects::toString)
                .anyMatch(grantedAuthority -> grantedAuthority.equals(role));
    }
}
