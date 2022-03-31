package com.epam.esm.service.exception;

public class MissingPageNumberException extends ServiceException {
    public MissingPageNumberException() {
        super();
    }

    public MissingPageNumberException(String message) {
        super(message);
    }

    public MissingPageNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingPageNumberException(Throwable cause) {
        super(cause);
    }

    protected MissingPageNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
