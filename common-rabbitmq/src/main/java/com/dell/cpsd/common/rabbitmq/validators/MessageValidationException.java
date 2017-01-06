/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.validators;

import java.util.List;

/**
 * Exception signaling that message validation failed.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class MessageValidationException extends RuntimeException
{
    private static final long serialVersionUID = -2326041223674348892L;

    private ValidationResult validationResult;

    public MessageValidationException(ValidationResult validationResult)
    {
        super(getFirstError(validationResult));
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult()
    {
        return validationResult;
    }

    public String getFirstError()
    {
        return getFirstError(validationResult);
    }

    private static String getFirstError(ValidationResult result)
    {
        if (result == null)
        {
            return null;
        }
        List<String> errors = result.getErrors();
        if (errors == null || errors.isEmpty())
        {
            return null;
        }
        return errors.get(0);
    }
}
