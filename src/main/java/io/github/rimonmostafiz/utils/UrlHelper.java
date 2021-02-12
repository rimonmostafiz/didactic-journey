package io.github.rimonmostafiz.utils;

/**
 * @author Rimon Mostafiz
 */
final public class UrlHelper {
    private UrlHelper() {
    }

    public static final String AUTH_LOGIN = "/rest/auth/login";
    public static final String AUTH_REFRESH = "/rest/auth/refresh";
    public static final String AUTH_LOGOUT = "/rest/auth/logout";
    public static final String REST = "/rest";
    public static final String V2_APIDOCS = "/v2/api-docs";
    public static final String SWAGGERUI = "/swagger-ui.html";
    public static final String MINIO_WEB_HOOK = "/rest/minio/event";

    public static String all(String url) {
        return url + "/**";
    }

    public static String process(String url) {
        return url + "/process";
    }

    public static String prepare(String host, int port, String contextPath, String context) {
        return "http://" + host + ":" + port + "/" + contextPath + "/" + context;
    }
}
