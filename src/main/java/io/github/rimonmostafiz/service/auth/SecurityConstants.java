package io.github.rimonmostafiz.service.auth;

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
}
