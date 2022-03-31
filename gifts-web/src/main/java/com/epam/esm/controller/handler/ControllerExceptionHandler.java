package com.epam.esm.controller.handler;

import com.epam.esm.controller.localizer.Localizer;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.controller.handler.EntityErrorCode.*;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final Localizer localizer;
    private static final String NUMBER_FORMAT_ERROR_MESSAGE = "invalid.number.value.was.entered";
    private static final String INVALID_CERTIFICATE_MESSAGE = "invalid.certificate";

    @Autowired
    public ControllerExceptionHandler(Localizer localizer) {
        this.localizer = localizer;
    }

    @ExceptionHandler(TypedServiceException.class)
    public ResponseEntity<ErrorResponse> handleTypedServiceException(TypedServiceException e) {
        if (e.getEntityClass().equals(Certificate.class) && e.getClass().equals(InvalidEntityException.class)) {
            e = new TypedServiceException(e.getEntityClass(),
                        String.format(localizer.toLocale(INVALID_CERTIFICATE_MESSAGE), e.getMessage()));
            }

        return createErrorResponse(e.getMessage(), ExceptionErrorStatus.findHttpStatusByException(e),
                EntityErrorCode.findErrorCodeByEntityClass(e.getEntityClass()).getErrorCode());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleNumberFormatException(NumberFormatException e) {
        return createErrorResponse(NUMBER_FORMAT_ERROR_MESSAGE, ExceptionErrorStatus.findHttpStatusByException(e),
                DEFAULT_ERROR_CODE.getErrorCode());
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughMoneyException(NotEnoughMoneyException e) {
        return createErrorResponse(e.getMessage(), ExceptionErrorStatus.findHttpStatusByException(e),
                USER_ERROR_CODE.getErrorCode());
    }

    @ExceptionHandler(MissingPageNumberException.class)
    public ResponseEntity<ErrorResponse> handleMissingPageNumberException(MissingPageNumberException e) {
        return createErrorResponse(e.getMessage(), ExceptionErrorStatus.findHttpStatusByException(e),
                DEFAULT_ERROR_CODE.getErrorCode());
    }

    @ExceptionHandler(InvalidSortParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortParameterException(InvalidSortParameterException e) {
        return createErrorResponse(e.getMessage(), ExceptionErrorStatus.findHttpStatusByException(e),
                DEFAULT_ERROR_CODE.getErrorCode());
    }

    @ExceptionHandler(InvalidPaginationDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPaginationDataException(InvalidPaginationDataException e) {
        return createErrorResponse(e.getMessage(), ExceptionErrorStatus.findHttpStatusByException(e),
                DEFAULT_ERROR_CODE.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(String messageCode, HttpStatus status, String errorCode) {
        String errorMessage = localizer.toLocale(messageCode);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("errorMessage", errorMessage);
        responseBody.put("errorCode", status.value() + errorCode);

        ErrorResponse error = new ErrorResponse();
        error.setResponseBody(responseBody);

        return new ResponseEntity<>(error, status);
    }
}