/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.consumer.error;

/**
 * Includes details of failed message error transformation.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class TransformationException extends RuntimeException
{
    private static final long serialVersionUID = 5845888385564440160L;

    private Exception internalError;

    public TransformationException(String message, Throwable cause, Exception internalError)
    {
        super(message, cause);
        this.internalError = internalError;
    }

    public Exception getInternalError()
    {
        return internalError;
    }
}
