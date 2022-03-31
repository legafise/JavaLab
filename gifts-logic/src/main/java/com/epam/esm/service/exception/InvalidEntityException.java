package com.epam.esm.service.exception;

import com.epam.esm.entity.BaseEntity;

public class InvalidEntityException extends TypedServiceException {
    public InvalidEntityException() {
        super();
    }

    public InvalidEntityException(Class<? extends BaseEntity> entityClass, String message) {
        super(entityClass, message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEntityException(Throwable cause) {
        super(cause);
    }

    protected InvalidEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
