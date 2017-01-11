package com.dell.cpsd.common.rabbitmq.i18n.error;

/**
 * Allows to get localized error from resource bundle.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface LocalizedErrorCode
{
    LocalizedError getLocalizedError(Object... params);
}
