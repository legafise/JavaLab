package com.epam.esm.service.exception;

import com.epam.esm.entity.BaseEntity;

public class UnknownEntityException extends TypedServiceException {
    public UnknownEntityException() {
        super();
    }

    public UnknownEntityException(Class<? extends BaseEntity> entityClass, String message) {
        super(entityClass, message);
    }

    public UnknownEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownEntityException(Throwable cause) {
        super(cause);
    }

    protected UnknownEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
