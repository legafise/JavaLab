package com.epam.esm.service.exception;

import com.epam.esm.entity.BaseEntity;

public class EntityDuplicationException extends TypedServiceException {
    public EntityDuplicationException() {
        super();
    }

    public EntityDuplicationException(Class<? extends BaseEntity> entityClass, String message) {
        super(entityClass, message);
    }

    public EntityDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityDuplicationException(Throwable cause) {
        super(cause);
    }

    protected EntityDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
