package com.epam.esm.service.exception;

public class InvalidPaginationDataException extends ServiceException {
    public InvalidPaginationDataException() {
        super();
    }

    public InvalidPaginationDataException(String message) {
        super(message);
    }

    public InvalidPaginationDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPaginationDataException(Throwable cause) {
        super(cause);
    }

    protected InvalidPaginationDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
