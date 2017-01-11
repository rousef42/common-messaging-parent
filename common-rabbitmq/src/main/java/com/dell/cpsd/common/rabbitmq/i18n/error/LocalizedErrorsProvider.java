package com.dell.cpsd.common.rabbitmq.i18n.error;

import java.util.List;

/**
 * Provides related localized errors. Intended for exceptions.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface LocalizedErrorsProvider
{

    List<LocalizedError> getLocalizedErrors();
}
