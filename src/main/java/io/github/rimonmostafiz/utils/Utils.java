package io.github.rimonmostafiz.utils;

import io.github.rimonmostafiz.model.common.ErrorDetails;
import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.common.SuccessDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Rimon Mostafiz
 */
public class Utils {
    public static <T> RestResponse<T> buildSuccessRestResponse(HttpStatus httpStatus, T klass) {
        return new RestResponse<>(httpStatus, new SuccessDetails<>(klass));
    }

    public static <T> RestResponse<T> buildErrorRestResponse(HttpStatus httpStatus, String filed, String message) {
        if (filed != null) {
            return new RestResponse<>(httpStatus, new ErrorDetails(filed, message));
        } else {
            return new RestResponse<>(httpStatus, new ErrorDetails(message));
        }
    }

    public static <T> ResponseEntity<RestResponse<T>> buildSuccessResponse(HttpStatus httpStatus, T klass) {
        RestResponse<T> restResponse = Utils.buildSuccessRestResponse(httpStatus, klass);
        return ResponseEntity.status(httpStatus).body(restResponse);
    }

    public static <T> ResponseEntity<RestResponse<T>> buildErrorResponse(HttpStatus httpStatus, String filed, String message) {
        RestResponse<T> errorRestResponse = buildErrorRestResponse(httpStatus, filed, message);
        return ResponseEntity.status(httpStatus).body(errorRestResponse);
    }

//    public static UserDetails extractUserDetails(HttpServletRequest request) {
//        return (UserDetails) request.getSession().getAttribute(SessionKey.USER_DETAILS);
//    }

//    public static String getUserNameFromRequest(HttpServletRequest request) {
//        return extractUserDetails(request).getUsername();
//    }
}
