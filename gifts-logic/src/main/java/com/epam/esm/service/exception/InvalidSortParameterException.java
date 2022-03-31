package com.epam.esm.service.exception;

public class InvalidSortParameterException extends ServiceException {
    public InvalidSortParameterException() {
        super();
    }

    public InvalidSortParameterException(String message) {
        super(message);
    }

    public InvalidSortParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSortParameterException(Throwable cause) {
        super(cause);
    }

    protected InvalidSortParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
