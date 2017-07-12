/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */


package com.dell.cpsd.common.rabbitmq.exceptions;

import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedErrorCode;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedErrorsProvider;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Base implementation of application-specific exception with localized messages.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class ApplicationException extends RuntimeException implements LocalizedErrorsProvider
{
    private static final long serialVersionUID = -580003631209011147L;

    private List<LocalizedError> errors;

    public ApplicationException(LocalizedErrorCode localizedErrorCode, Object... params)
    {
        this(null, localizedErrorCode.getLocalizedError(params));
    }

    public ApplicationException(Throwable cause, LocalizedErrorCode localizedErrorCode, Object... params)
    {
        this(cause, localizedErrorCode.getLocalizedError(params));
    }

    public ApplicationException(LocalizedError error)
    {
        this(null, asList(error));
    }

    public ApplicationException(Throwable cause, LocalizedError error)
    {
        this(cause, asList(error));
    }

    public ApplicationException(List<LocalizedError> errors)
    {
        this(null, errors);
    }

    public ApplicationException(Throwable cause, List<LocalizedError> errors)
    {
        super(getFirstLocalizedMessage(errors), cause);
        this.errors = new ArrayList<>(errors);
    }

    protected static String getFirstLocalizedMessage(List<LocalizedError> errors)
    {
        return (errors == null || errors.isEmpty()) ? null : errors.get(0).getMessage();
    }

    @Override
    public List<LocalizedError> getLocalizedErrors()
    {
        return errors;
    }
}
