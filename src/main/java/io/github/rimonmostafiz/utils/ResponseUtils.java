package io.github.rimonmostafiz.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rimonmostafiz.model.common.ErrorDetails;
import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.common.SuccessDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author Rimon Mostafiz
 */
public class ResponseUtils {

    public static final String INVALID = "invalid";
    public static final String EXPIRED = "expired";
    public static final String INVALID_TOKEN = "Invalid Token";
    public static final String EXPIRED_TOKEN = "Expired Token";
    public static final String LOGOUT_SUCCESS = "Logout Successful";
    public static final String LOGOUT_FAILED = "Logout Failed";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    public static <T> RestResponse<T> buildSuccessRestResponse(HttpStatus httpStatus, T klass) {
        return new RestResponse<>(httpStatus, new SuccessDetails<>(klass));
    }

    public static <T> RestResponse<T> buildErrorRestResponse(HttpStatus httpStatus, String filed, String message) {
        if (filed != null) {
            return new RestResponse<>(httpStatus, Collections.singletonList(new ErrorDetails(filed, message)));
        } else {
            return new RestResponse<>(httpStatus, Collections.singletonList(new ErrorDetails(message)));
        }
    }

    public static <T> RestResponse<T> buildErrorRestResponse(HttpStatus httpStatus, List<ErrorDetails> errors) {
        return new RestResponse<>(httpStatus, errors);
    }

    public static <T> ResponseEntity<RestResponse<T>> buildSuccessResponse(HttpStatus httpStatus, T klass) {
        RestResponse<T> restResponse = ResponseUtils.buildSuccessRestResponse(httpStatus, klass);
        return ResponseEntity.status(httpStatus).body(restResponse);
    }

    public static <T> ResponseEntity<RestResponse<T>> buildErrorResponse(HttpStatus httpStatus,
                                                                         String filed, String message) {
        RestResponse<T> errorRestResponse = buildErrorRestResponse(httpStatus, filed, message);
        return ResponseEntity.status(httpStatus).body(errorRestResponse);
    }

    public static <T> void createCustomResponse(HttpServletResponse response, RestResponse<T> apiResponse)
            throws IOException {
        response.setContentType("application/json");
        response.setStatus(apiResponse.getStatus().value());
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, apiResponse);
        out.flush();
    }
}
