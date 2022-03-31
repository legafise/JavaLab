package com.epam.esm.controller.handler;

import com.epam.esm.service.exception.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public enum ExceptionErrorStatus {
    NOT_FOUNT_EXCEPTION(Collections.singletonList(UnknownEntityException.class), HttpStatus.NOT_FOUND),
    CONFLICT_EXCEPTION(Collections.singletonList(EntityDuplicationException.class), HttpStatus.CONFLICT),
    BAD_REQUEST_EXCEPTION(Arrays.asList(InvalidEntityException.class, NumberFormatException.class,
            MissingPageNumberException.class, InvalidSortParameterException.class,
            InvalidPaginationDataException.class), HttpStatus.BAD_REQUEST),
    PAYMENT_REQUIRED_EXCEPTION(Collections.singletonList(NotEnoughMoneyException.class), HttpStatus.PAYMENT_REQUIRED),
    UNKNOWN_EXCEPTION(Collections.singletonList(RuntimeException.class), HttpStatus.BAD_REQUEST);

    private final List<Class<? extends RuntimeException>> exceptionClasses;
    private final HttpStatus httpStatus;

    ExceptionErrorStatus(List<Class<? extends RuntimeException>> exceptionClasses, HttpStatus httpStatus) {
        this.exceptionClasses = exceptionClasses;
        this.httpStatus = httpStatus;
    }

    public List<Class<? extends RuntimeException>> getExceptionClasses() {
        return exceptionClasses;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static HttpStatus findHttpStatusByException(RuntimeException exception) {
        return Arrays.stream(ExceptionErrorStatus.values())
                .filter(errorStatus -> errorStatus.getExceptionClasses().contains(exception.getClass()))
                .findFirst()
                .map(ExceptionErrorStatus::getHttpStatus)
                .orElse(UNKNOWN_EXCEPTION.getHttpStatus());
    }
}
