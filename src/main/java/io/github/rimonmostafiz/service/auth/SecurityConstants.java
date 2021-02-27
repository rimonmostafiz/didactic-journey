package io.github.rimonmostafiz.service.auth;

import java.util.Collections;
import java.util.List;

/**
 * @author Rimon Mostafiz
 */
public class SecurityConstants {
    public static final String AUTH_LOGIN_URL = "/auth/login";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "typ";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    public static final String WHITE_LIST_ACCESS_TOKEN_PREFIX = "WL:AT:";
    public static final String WHITE_LIST_REFRESH_TOKEN_PREFIX = "WL:RT:";

    public static final String BLACK_LIST_ACCESS_TOKEN_PREFIX = "BL:AT:";
    public static final String BLACK_LIST_REFRESH_TOKEN_PREFIX = "BL:RT:";

    public static final String PORTAL_USER_TOKEN_KEY = "PORTAL:USER:TOKEN";
    public static final String PARTNER_APP_TOKEN_KEY = "PARTNER:APP:TOKEN";

    public static final String PORTAL_API_ROLE = "PORTAL_API";
    public static final String PARTNER_APP_API_ROLE = "PARTNER_APP_API";

    public static final String PORTAL_USERNAME = "portal_user";
    public static final String PARTNER_APP_USERNAME = "partner_app_user";

    public static final List<String> PORTAL_ROLE = Collections.singletonList(PORTAL_API_ROLE);
    public static final List<String> PARTNER_APP_ROLE = Collections.singletonList(PARTNER_APP_API_ROLE);
}
