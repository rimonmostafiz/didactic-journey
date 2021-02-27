package io.github.rimonmostafiz.utils;

/**
 * @author Rimon Mostafiz
 */
final public class UrlHelper {
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_REFRESH = "/auth/refresh";
    public static final String AUTH_LOGOUT = "/auth/logout";
    public static final String ALL = "/**";
    public static final String API_DOCS = "/api-docs";
    public static final String V2_API_DOCS = "/v2/api-docs";
    public static final String CONFIGURATION = "/configuration";
    public static final String CONFIGURATION_UI = "/configuration/ui";
    public static final String WEB_JARS = "/webjars";
    public static final String SWAGGER_RESOURCES = "/swagger-resources";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final String SWAGGER_UI = "/swagger-ui";

    private UrlHelper() {
    }

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
