package io.github.rimonmostafiz.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rimon Mostafiz
 */
public class HttpUtils {
    private HttpUtils() { }

    public static String determineTargetUrl(HttpServletRequest request) {
        String context = request.getContextPath();
        String fullURL = request.getRequestURI();
        String url = fullURL.replaceAll("/\\z", "");
        return url.substring(fullURL.indexOf(context) + context.length());
    }
}
