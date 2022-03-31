package com.epam.esm.service.exception;

import com.epam.esm.entity.BaseEntity;

public class TypedServiceException extends ServiceException {
    private Class<? extends BaseEntity> entityClass;

    public TypedServiceException() {
        super();
    }

    public TypedServiceException(Class<? extends BaseEntity> entityClass, String message) {
        super(message);
        this.entityClass = entityClass;
    }

    public TypedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypedServiceException(Throwable cause) {
        super(cause);
    }

    protected TypedServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }
}
